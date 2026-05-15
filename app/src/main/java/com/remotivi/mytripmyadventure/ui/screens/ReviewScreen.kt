package com.remotivi.mytripmyadventure.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.remotivi.mytripmyadventure.Screen
import com.remotivi.mytripmyadventure.ui.components.ReviewData
import com.remotivi.mytripmyadventure.ui.theme.DarkGreen
import com.remotivi.mytripmyadventure.ui.theme.LightGrey
import com.remotivi.mytripmyadventure.ui.theme.PriceOrange

// ==========================================================
// REVIEW SCREEN
// ==========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    tripId: String,
    navController: NavHostController,
    onSaveReview: (ReviewData) -> Unit
) {
    var rating by remember { mutableIntStateOf(0) }
    var reviewText by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(setOf<String>()) }

    val reviewTags = listOf(
        "Pemandangan Indah", "Guide Profesional", "Tepat Waktu",
        "Fasilitas Bagus", "Rekomendasi Banget", "Harga Sesuai"
    )

    val isFormValid = rating > 0 && reviewText.isNotBlank()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Beri Ulasan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Trip info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD5E8D4)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(56.dp).background(Color.LightGray, RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(tripId, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("Bagaimana pengalamanmu?", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Star rating
            Text("Rating Keseluruhan *", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                when (rating) {
                    1 -> "Sangat Buruk"
                    2 -> "Kurang Baik"
                    3 -> "Cukup"
                    4 -> "Bagus"
                    5 -> "Luar Biasa!"
                    else -> "Tap bintang untuk memberi rating"
                },
                fontSize = 12.sp,
                color = if (rating > 0) PriceOrange else Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (1..5).forEach { star ->
                    Icon(
                        imageVector = if (star <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Bintang $star",
                        modifier = Modifier.size(40.dp).clickable { rating = star },
                        tint = if (star <= rating) Color(0xFFF1C40F) else Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tags
            Text("Hal yang paling berkesan", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(8.dp))
            // Manual wrap - lebih aman dari FlowRow
            val chunkedTags = reviewTags.chunked(3)
            chunkedTags.forEach { rowTags ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowTags.forEach { tag ->
                        val isSelected = selectedTags.contains(tag)
                        Surface(
                            modifier = Modifier.weight(1f).clickable {
                                selectedTags = if (isSelected) selectedTags - tag else selectedTags + tag
                            },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Color(0xFFD5E8D4) else Color.White,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) DarkGreen else Color.LightGray
                            )
                        ) {
                            Text(
                                tag,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                                color = if (isSelected) DarkGreen else Color.Gray
                            )
                        }
                    }
                    // Fill empty slots if row has fewer than 3 items
                    repeat(3 - rowTags.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Review text
            Text("Ceritakan pengalamanmu *", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = reviewText,
                onValueChange = { reviewText = it },
                placeholder = { Text("Tulis ulasanmu di sini...") },
                modifier = Modifier.fillMaxWidth().height(140.dp),
                shape = RoundedCornerShape(12.dp),
                isError = reviewText.isEmpty() && rating > 0,
                supportingText = {
                    Text("${reviewText.length}/500", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End, fontSize = 10.sp)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Photo upload placeholder
            Text("Tambah Foto (Opsional)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    .background(LightGrey.copy(0.3f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.AddPhotoAlternate, null, tint = Color.Gray, modifier = Modifier.size(24.dp))
                    Text("Tambah", fontSize = 9.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (isFormValid) {
                        onSaveReview(
                            ReviewData(
                                tripTitle = tripId,
                                rating = rating,
                                comment = reviewText,
                                date = "Today",
                                tags = selectedTags.toList()
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) DarkGreen else Color.Gray
                ),
                shape = RoundedCornerShape(28.dp),
                enabled = isFormValid
            ) {
                Text("Kirim Ulasan", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==========================================================
// REVIEW SUCCESS SCREEN
// ==========================================================
@Composable
fun ReviewSuccessScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Animated check icon
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFFD5E8D4), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Star,
                null,
                modifier = Modifier.size(64.dp),
                tint = DarkGreen
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Ulasan Terkirim!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Terima kasih atas ulasanmu. Ulasanmu membantu penjelajah lain untuk memilih trip terbaik!",
            textAlign = TextAlign.Center,
            color = Color.Gray,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // XP reward card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3CD)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.EmojiEvents, null, tint = PriceOrange, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("+50 XP", fontWeight = FontWeight.Bold, color = PriceOrange, fontSize = 18.sp)
                    Text("Kamu mendapat poin dari ulasan ini!", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { navController.navigate(Screen.MyReviews.route) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Lihat Semua Ulasanku", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Kembali ke Home", color = DarkGreen)
        }
    }
}

// ==========================================================
// MY REVIEWS SCREEN
// ==========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyReviewsScreen(navController: NavHostController, allReviews: List<ReviewData>) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Ulasan Saya", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { innerPadding ->
        if (allReviews.isEmpty()) {
            Box(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.RateReview,
                        null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Belum ada ulasan", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text("Ulasanmu akan muncul di sini setelah menyelesaikan trip.", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate(Screen.MyTrips.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Lihat Trip Saya")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(innerPadding).fillMaxSize().padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 20.dp)
            ) {
                // Summary card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = DarkGreen),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Rating Rata-rata",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 12.sp
                                )
                                val avgRating = allReviews.map { it.rating }.average()
                                Text(
                                    "%.1f".format(avgRating),
                                    color = Color.White,
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Row {
                                    repeat(5) { i ->
                                        Icon(
                                            if (i < avgRating.toInt()) Icons.Default.Star else Icons.Default.StarBorder,
                                            null,
                                            tint = Color(0xFFF1C40F),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "${allReviews.size}",
                                    color = Color.White,
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Total Ulasan", color = Color.White.copy(0.7f), fontSize = 12.sp)
                            }
                        }
                    }
                }

                items(allReviews) { review ->
                    MyReviewCard(review)
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
fun MyReviewCard(review: ReviewData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, LightGrey),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(48.dp).background(Color(0xFFD5E8D4), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Terrain, null, tint = DarkGreen)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(review.tripTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Row {
                        repeat(review.rating) {
                            Icon(Icons.Default.Star, null, tint = Color(0xFFF1C40F), modifier = Modifier.size(14.dp))
                        }
                        repeat(5 - review.rating) {
                            Icon(Icons.Default.StarBorder, null, tint = Color.LightGray, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(review.comment, fontSize = 13.sp, color = Color.Gray, lineHeight = 20.sp)
            if (review.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    review.tags.take(3).forEach { tag ->
                        Surface(
                            color = Color(0xFFD5E8D4),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                tag,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                fontSize = 10.sp,
                                color = DarkGreen
                            )
                        }
                    }
                }
            }
        }
    }
}
