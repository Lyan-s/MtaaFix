package com.example.mtaafix.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.mtaafix.R

// ------------------------------------------------------------
// MtaaFix Colors
// ------------------------------------------------------------

private val MtaaBlue = Color(0xFF0B5FA5)
private val MtaaDarkBlue = Color(0xFF073B73)
private val MtaaOrange = Color(0xFFFF8A00)
private val MtaaLightBlue = Color(0xFFEAF4FC)
private val MtaaGray = Color(0xFF6B7280)
private val MtaaLightGray = Color(0xFFD9E0E8)


// ------------------------------------------------------------
// Onboarding Data
// ------------------------------------------------------------

private data class OnboardingPage(
    val title: String,
    val orangeTitle: String,
    val description: String
)

private val pages = listOf(

    OnboardingPage(
        title = "Spot a problem?",
        orangeTitle = "Report it.",
        description =
            "Report potholes, broken streetlights,\n" +
            "water leaks, garbage and other\n" +
            "local issues in seconds."
    ),

    OnboardingPage(
        title = "Snap and submit",
        orangeTitle = "",
        description =
            "Add a photo, pick a category,\n" +
            "and mark how urgent it is —\n" +
            "MtaaFix handles the rest."
    ),

    OnboardingPage(
        title = "Track it to resolution",
        orangeTitle = "",
        description =
            "Follow your report's progress from\n" +
            "Pending to Resolved, with updates\n" +
            "along the way."
    )
)


// ------------------------------------------------------------
// Main Onboarding Screen
// ------------------------------------------------------------

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit = {}
) {

    val pagerState = rememberPagerState(
        pageCount = { pages.size }
    )

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // ----------------------------------------------------
        // Pager
        // ----------------------------------------------------

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { pageIndex ->

            val page = pages[pageIndex]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // --------------------------------------------
                // Logo
                // --------------------------------------------

                Spacer(
                    modifier = Modifier.height(40.dp)
                )

                Image(
                    painter = painterResource(
                        id = R.drawable.splash_logo
                    ),
                    contentDescription = "MtaaFix",
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .height(90.dp),
                    contentScale = ContentScale.Fit
                )

                // --------------------------------------------
                // Illustration (built from shapes/icons, no
                // external image files needed)
                // --------------------------------------------

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    PageIllustration(pageIndex)
                }

                // --------------------------------------------
                // Title
                // --------------------------------------------

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = page.title,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MtaaDarkBlue,
                        textAlign = TextAlign.Center
                    )

                    if (page.orangeTitle.isNotEmpty()) {

                        Text(
                            text = page.orangeTitle,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = MtaaOrange,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = page.description,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = MtaaDarkBlue,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }
        }

        // ----------------------------------------------------
        // Page Indicators
        // ----------------------------------------------------

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 18.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            repeat(pages.size) { index ->

                val selected =
                    pagerState.currentPage == index

                Box(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .size(
                            if (selected) 12.dp else 10.dp
                        )
                        .clip(CircleShape)
                        .background(
                            if (selected)
                                MtaaBlue
                            else
                                MtaaLightGray
                        )
                )
            }
        }

        // ----------------------------------------------------
        // Navigation Buttons
        // ----------------------------------------------------

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // --------------------------------------------
            // Skip / Back
            // --------------------------------------------

            if (pagerState.currentPage == 1) {

                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    }
                ) {

                    Text(
                        text = "Back",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MtaaBlue
                    )
                }

            } else {

                TextButton(
                    onClick = onFinished
                ) {

                    Text(
                        text = "Skip",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MtaaBlue
                    )
                }
            }

            // --------------------------------------------
            // Next / Get Started
            // --------------------------------------------

            Button(
                onClick = {

                    if (pagerState.currentPage < pages.lastIndex) {

                        coroutineScope.launch {

                            pagerState.animateScrollToPage(
                                pagerState.currentPage + 1
                            )
                        }

                    } else {

                        onFinished()
                    }
                },

                modifier = Modifier
                    .height(58.dp)
                    .width(190.dp),

                shape = RoundedCornerShape(12.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = MtaaBlue
                )
            ) {

                Text(
                    text =
                        if (
                            pagerState.currentPage ==
                            pages.lastIndex
                        )
                            "Get Started"
                        else
                            "Next",

                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }
}

// ------------------------------------------------------------
// Illustrations built purely from Compose shapes + icons
// (no external drawable files required)
// ------------------------------------------------------------

@Composable
private fun PageIllustration(pageIndex: Int) {
    when (pageIndex) {
        0 -> ReportIllustration()
        1 -> SubmitIllustration()
        else -> TrackIllustration()
    }
}

@Composable
private fun ReportIllustration() {
    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(MtaaLightBlue)
        )
        Icon(
            imageVector = Icons.Filled.Place,
            contentDescription = null,
            tint = MtaaBlue,
            modifier = Modifier.size(90.dp)
        )
        Box(
            modifier = Modifier
                .padding(start = 90.dp, bottom = 90.dp)
                .size(56.dp)
                .clip(CircleShape)
                .background(MtaaOrange),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
private fun SubmitIllustration() {
    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(200.dp, 150.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MtaaLightBlue)
        )
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(MtaaBlue)
        )
        Box(
            modifier = Modifier
                .padding(start = 110.dp, top = 90.dp)
                .size(50.dp)
                .clip(CircleShape)
                .background(MtaaOrange),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun TrackIllustration() {
    val stages = listOf(true, true, false, false)
    Column(horizontalAlignment = Alignment.Start) {
        stages.forEachIndexed { index, done ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (done) MtaaBlue else MtaaLightGray),
                    contentAlignment = Alignment.Center
                ) {
                    if (done) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(50))
                        .background(if (done) MtaaOrange.copy(alpha = 0.5f) else MtaaLightGray)
                )
            }
            if (index != stages.lastIndex) {
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}