package com.remotivi.mytripmyadventure.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.remotivi.mytripmyadventure.ui.components.ReviewData
import com.remotivi.mytripmyadventure.ui.theme.DarkGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDetailScreen(
    reviewId: String,
    navController: NavHostController,
    allReviews: List<ReviewData>
) {
    val review = allReviews.find { it.id == reviewId }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail Ulasan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { innerPadding ->
        if (review == null) {
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ulasan tidak ditemukan", color = Color.Gray)
            }
            return@Scaffold
        }
        
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // User Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(50.dp).background(Color(0xFFD5E8D4), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = DarkGreen)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Verified Explorer", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.Verified, contentDescription = "Verified", tint = Color(0xFF3498db), modifier = Modifier.size(16.dp))
                    }
                    Text(review.date, color = Color.Gray, fontSize = 12.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Trip Info & Rating
            Text("Trip: ${review.tripTitle}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DarkGreen)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                repeat(review.rating) {
                    Icon(Icons.Default.Star, null, tint = Color(0xFFF1C40F), modifier = Modifier.size(20.dp))
                }
                repeat(5 - review.rating) {
                    Icon(Icons.Default.StarBorder, null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Comment
            Text(review.comment, fontSize = 15.sp, lineHeight = 24.sp, color = Color.Black)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Tags
            if (review.tags.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    review.tags.forEach { tag ->
                        Surface(
                            color = Color(0xFFD5E8D4),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text(
                                tag,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontSize = 12.sp,
                                color = DarkGreen
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Image
            if (review.imageUri != null) {
                Text("Foto Review", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))
                com.remotivi.mytripmyadventure.ui.components.ReviewImageDisplay(
                    imageUri = review.imageUri,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }
        }
    }
}
