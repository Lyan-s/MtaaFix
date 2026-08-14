package com.example.mtaafix.ui.reports

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ReportViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    var isSubmitting by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var submitSuccess by mutableStateOf(false)
        private set

    var myReports by mutableStateOf<List<Report>>(emptyList())
        private set

    var isLoadingReports by mutableStateOf(false)
        private set

    var selectedReport by mutableStateOf<Report?>(null)
        private set

    fun submitReport(
        title: String,
        description: String,
        category: String,
        severity: String,
        locationLabel: String,
        photoUri: Uri?
    ) {
        errorMessage = null

        if (title.isBlank() || description.isBlank() || locationLabel.isBlank()) {
            errorMessage = "Please fill in title, description, and location"
            return
        }
        if (photoUri == null) {
            errorMessage = "Please add a photo"
            return
        }

        isSubmitting = true

        MediaManager.get().upload(photoUri)
            .unsigned("mtaafix_unsigned")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {}
                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String, resultData: MutableMap<Any?, Any?>) {
                    val photoUrl = resultData["secure_url"] as? String ?: ""
                    saveReportToFirestore(title, description, category, severity, locationLabel, photoUrl)
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    isSubmitting = false
                    errorMessage = "Photo upload failed: ${error.description}"
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {}
            })
            .dispatch()
    }

    private fun saveReportToFirestore(
        title: String,
        description: String,
        category: String,
        severity: String,
        locationLabel: String,
        photoUrl: String
    ) {
        val userId = auth.currentUser?.uid ?: ""

        val report = Report(
            userId = userId,
            title = title,
            description = description,
            category = category,
            severity = severity,
            locationLabel = locationLabel,
            photoUrl = photoUrl
        )

        firestore.collection("reports")
            .add(report)
            .addOnSuccessListener {
                isSubmitting = false
                submitSuccess = true
            }
            .addOnFailureListener { exception ->
                isSubmitting = false
                errorMessage = "Failed to save report: ${exception.message}"
            }
    }

    fun fetchMyReports() {
        val userId = auth.currentUser?.uid ?: return
        isLoadingReports = true
        errorMessage = null

        firestore.collection("reports")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val results = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Report::class.java)?.copy(id = doc.id)
                }
                // Sort newest first on the client, avoiding the need for a Firestore composite index
                myReports = results.sortedByDescending { it.createdAt }
                isLoadingReports = false
            }
            .addOnFailureListener { exception ->
                isLoadingReports = false
                errorMessage = "Failed to load reports: ${exception.message}"
            }
    }

    fun selectReport(report: Report) {
        selectedReport = report
    }

    fun resetSubmitState() {
        submitSuccess = false
        errorMessage = null
    }
}