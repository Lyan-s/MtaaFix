package com.example.mtaafix.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val MtaaBlue = Color(0xFF0B5FA5)
private val MtaaDarkBlue = Color(0xFF073B73)
private val MtaaOrange = Color(0xFFFF8A00)
private val MtaaLightBlue = Color(0xFFEAF4FC)
private val MtaaText = Color(0xFF263238)

private data class SplashFeature(
    val icon: ImageVector,
    val title: String,
    val description: String
)

private val splashFeatures = listOf(
    SplashFeature(
        Icons.Filled.Warning,
        "REPORT",
        "See a problem?\nReport it easily."
    ),
    SplashFeature(
        Icons.Filled.Place,
        "TRACK",
        "Track progress\nin real time."
    ),
    SplashFeature(
        Icons.Filled.Notifications,
        "GET UPDATES",
        "Receive updates\nevery step."
    ),
    SplashFeature(
        Icons.Filled.CheckCircle,
        "RESOLVE",
        "We work to\nget it fixed."
    )
)

@Composable
fun SplashScreen(
    onFinished: () -> Unit = {}
) {

    LaunchedEffect(Unit) {
        delay(2600)
        onFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        /*
         * =========================================================
         * MAIN CONTENT
         * =========================================================
         */

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(45.dp))

            /*
             * LOGO
             *
             * Put your generated MtaaFix logo in:
             *
             * app/src/main/res/drawable/splash_logo.xml
             *
             * or use splash_logo.png
             */

            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(
                    id = com.example.mtaafix.R.drawable.splash_logo
                ),
                contentDescription = "MtaaFix Logo",
                modifier = Modifier
                    .size(180.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            /*
             * BRAND NAME
             */

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Mtaa",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MtaaBlue
                )

                Text(
                    text = "Fix",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MtaaOrange
                )
            }

            /*
             * TAGLINE
             */

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Report",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = MtaaBlue
                )

                Text(
                    text = ".",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MtaaOrange
                )

                Text(
                    text = " Track",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = MtaaBlue
                )

                Text(
                    text = ".",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MtaaOrange
                )

                Text(
                    text = " Resolve",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = MtaaBlue
                )

                Text(
                    text = ".",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MtaaOrange
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            /*
             * INTRODUCTION
             */

            Text(
                text = "Together, let's build cleaner, safer\nand better communities.",
                fontSize = 17.sp,
                lineHeight = 25.sp,
                color = MtaaText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(27.dp))

            /*
             * FEATURE ROW
             */

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top
            ) {

                splashFeatures.forEach { feature ->
                    SplashFeatureItem(feature)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            /*
             * SIMPLE COMMUNITY ILLUSTRATION
             *
             * This recreates the lower city/park portion of
             * the generated splash screen using Compose.
             */

            CommunityIllustration(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }

        /*
         * =========================================================
         * FOOTER
         * =========================================================
         */

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MtaaDarkBlue)
                .padding(
                    horizontal = 24.dp,
                    vertical = 25.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Every report makes our",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Row {

                Text(
                    text = "community",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MtaaOrange
                )

                Text(
                    text = " a better place.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MtaaOrange
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            /*
             * LOADING BAR
             */

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(7.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.25f))
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Loading...",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}


/*
 * =============================================================
 * FEATURE ITEM
 * =============================================================
 */

@Composable
private fun SplashFeatureItem(
    feature: SplashFeature
) {

    Column(
        modifier = Modifier.width(78.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(52.dp)
                .background(
                    color = MtaaLightBlue,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = feature.icon,
                contentDescription = feature.title,
                modifier = Modifier.size(27.dp),
                tint = MtaaBlue
            )
        }

        Spacer(modifier = Modifier.height(7.dp))

        Text(
            text = feature.title,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MtaaBlue,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = feature.description,
            fontSize = 9.sp,
            lineHeight = 12.sp,
            color = MtaaText.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}


/*
 * =============================================================
 * COMMUNITY ILLUSTRATION
 * =============================================================
 *
 * A lightweight Compose illustration instead of another image.
 */

@Composable
private fun CommunityIllustration(
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {

        /*
         * SKY
         */

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MtaaLightBlue)
        )

        /*
         * CITY BUILDINGS
         */

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 25.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {

            Building(height = 70.dp)
            Building(height = 105.dp)
            Building(height = 82.dp)
            Building(height = 125.dp)
            Building(height = 90.dp)
            Building(height = 112.dp)
            Building(height = 75.dp)
        }

        /*
         * TREES
         */

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {

            Tree()

            Tree()

            Tree()
        }

        /*
         * ROAD
         */

        Box(
            modifier = Modifier
                .width(210.dp)
                .height(45.dp)
                .align(Alignment.BottomCenter)
                .clip(
                    RoundedCornerShape(
                        topStart = 100.dp,
                        topEnd = 100.dp
                    )
                )
                .background(MtaaDarkBlue)
        )

        /*
         * ROAD MARKING
         */

        Box(
            modifier = Modifier
                .width(6.dp)
                .height(25.dp)
                .align(Alignment.BottomCenter)
                .offset(y = (-4).dp)
                .background(Color.White)
        )
    }
}


/*
 * =============================================================
 * BUILDING
 * =============================================================
 */

@Composable
private fun Building(
    height: androidx.compose.ui.unit.Dp
) {

    Column(
        modifier = Modifier
            .width(35.dp)
            .height(height)
            .padding(horizontal = 2.dp)
            .background(
                Color(0xFFC7DCEE)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(8.dp))

        repeat(3) {

            Row {

                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .background(Color.White)
                )
            }

            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}


/*
 * =============================================================
 * TREE
 * =============================================================
 */

@Composable
private fun Tree() {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(38.dp)
                .background(
                    Color(0xFF65A947),
                    CircleShape
                )
        )

        Box(
            modifier = Modifier
                .width(7.dp)
                .height(22.dp)
                .background(
                    Color(0xFF795548)
                )
        )
    }
}