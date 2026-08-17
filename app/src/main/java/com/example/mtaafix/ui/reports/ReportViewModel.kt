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

    var allReports by mutableStateOf<List<Report>>(emptyList())
        private set

    var myUpdates by mutableStateOf<List<StatusUpdate>>(emptyList())
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


    fun fetchAllReports() {
        isLoadingReports = true
        errorMessage = null

        firestore.collection("reports")
            .get()
            .addOnSuccessListener { snapshot ->
                val results = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Report::class.java)?.copy(id = doc.id)
                }
                allReports = results.sortedByDescending { it.createdAt }
                isLoadingReports = false
            }
            .addOnFailureListener { exception ->
                isLoadingReports = false
                errorMessage = "Failed to load reports: ${exception.message}"
            }
    }

    fun updateReportStatus(reportId: String, newStatus: String) {
        val report = allReports.find { it.id == reportId } ?: selectedReport
        val oldStatus = report?.status ?: ""
        val reportOwnerId = report?.userId ?: ""
        val reportTitle = report?.title ?: ""

        firestore.collection("reports").document(reportId)
            .update("status", newStatus)
            .addOnSuccessListener {
                // Reflect the change locally so the UI updates without a full re-fetch
                allReports = allReports.map {
                    if (it.id == reportId) it.copy(status = newStatus) else it
                }
                selectedReport = selectedReport?.let {
                    if (it.id == reportId) it.copy(status = newStatus) else it
                }

                // Log this change so the citizen sees it in their Updates screen
                val update = StatusUpdate(
                    reportId = reportId,
                    reportTitle = reportTitle,
                    userId = reportOwnerId,
                    oldStatus = oldStatus,
                    newStatus = newStatus
                )
                firestore.collection("statusUpdates").add(update)
            }
            .addOnFailureListener { exception ->
                errorMessage = "Failed to update status: ${exception.message}"
            }
    }

    fun fetchMyUpdates() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("statusUpdates")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val results = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(StatusUpdate::class.java)
                }
                myUpdates = results.sortedByDescending { it.timestamp }
            }
    }


    fun deleteReport(reportId: String, onComplete: () -> Unit) {
        firestore.collection("reports").document(reportId)
            .delete()
            .addOnSuccessListener {
                myReports = myReports.filter { it.id != reportId }
                allReports = allReports.filter { it.id != reportId }
                selectedReport = null
                onComplete()
            }
            .addOnFailureListener { exception ->
                errorMessage = "Failed to delete report: ${exception.message}"
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