package com.remotivi.mytripmyadventure.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.database.FirebaseDatabase
import com.remotivi.mytripmyadventure.ui.components.TripData
import com.remotivi.mytripmyadventure.ui.components.ReviewData
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.remotivi.mytripmyadventure.ui.theme.DarkGreen
import com.remotivi.mytripmyadventure.ui.theme.LightGrey
import com.remotivi.mytripmyadventure.ui.theme.PriceOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(tripId: String, navController: NavHostController, allTrips: List<TripData>, allReviews: List<ReviewData> = emptyList()) {
    // Cari data trip berdasarkan judul (tripId)
    val trip = allTrips.find { it.title == tripId } ?: allTrips[0]
    val currentUser = remember { com.google.firebase.auth.FirebaseAuth.getInstance().currentUser }
    val isCreator = trip.creatorId.isNotEmpty() && trip.creatorId == currentUser?.uid

    Scaffold(
        bottomBar = {
            Surface(tonalElevation = 8.dp, color = Color.White) {
                if (isCreator) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Anda Pembuat Trip", fontSize = 12.sp, color = Color.Gray)
                            Text(trip.price, color = PriceOrange, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                        Button(
                            onClick = { 
                                val encodedTitle = android.net.Uri.encode(trip.title)
                                navController.navigate("edit_trip/$encodedTitle") 
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.height(52.dp).width(140.dp)
                        ) {
                            Text("EDIT TRIP", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                } else if (!trip.isJoined) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Harga", fontSize = 12.sp, color = Color.Gray)
                            Text("${trip.price}/orang", color = PriceOrange, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                        Button(
                            onClick = { 
                                if (trip.id.isNotEmpty()) {
                                    FirebaseDatabase.getInstance().getReference("trips").child(trip.id).child("joined").setValue(true)
                                }
                                val encodedTitle = android.net.Uri.encode(trip.title)
                                navController.navigate("payment/$encodedTitle") 
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.height(52.dp).width(140.dp)
                        ) {
                            Text("JOIN TRIP", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                } else if (!trip.paid) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Status", fontSize = 12.sp, color = Color.Gray)
                            Text("Menunggu Pembayaran", color = Color(0xFFE67E22), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Button(
                            onClick = { 
                                val encodedTitle = android.net.Uri.encode(trip.title)
                                navController.navigate("payment/$encodedTitle") 
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE67E22)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.height(52.dp).width(160.dp)
                        ) {
                            Text("BAYAR SEKARANG", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                } else if (trip.isCompleted) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Status", fontSize = 12.sp, color = Color.Gray)
                            Text("Trip Selesai", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Button(
                            onClick = { 
                                val encodedTitle = android.net.Uri.encode(trip.title)
                                navController.navigate("review/$encodedTitle") 
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE67E22)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.height(52.dp).width(160.dp)
                        ) {
                            Text("Beri Ulasan", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                } else {
                    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Status", fontSize = 12.sp, color = Color.Gray)
                            Text("Sudah Dibayar / Active", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { 
                                    val encodedTitle = android.net.Uri.encode(trip.title)
                                    navController.navigate("e_ticket/$encodedTitle") 
                                },
                                modifier = Modifier.weight(1f).height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE67E22)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ConfirmationNumber, null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("E-Ticket", fontWeight = FontWeight.Bold)
                                }
                            }
                            OutlinedButton(
                                onClick = { 
                                    if (trip.id.isNotEmpty()) {
                                        val database = FirebaseDatabase.getInstance()
                                        database.getReference("trips").child(trip.id).child("completed").setValue(true)
                                    }
                                    navController.navigate(com.remotivi.mytripmyadventure.Screen.MyTrips.route) {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                    }
                                },
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkGreen)
                            ) {
                                Text("Selesai Trip", color = DarkGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                val context = LocalContext.current
                if (trip.imageName.startsWith("data:image")) {
                    val decodedBitmap: android.graphics.Bitmap? = remember(trip.imageName) {
                        try {
                            val base64String = trip.imageName.substringAfter("base64,")
                            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
                            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    if (decodedBitmap != null) {
                        Image(
                            bitmap = decodedBitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize().background(Color.LightGray))
                    }
                } else {
                    val resolvedImageRes = if (trip.imageRes != 0) trip.imageRes 
                        else if (trip.imageName.isNotEmpty()) {
                            context.resources.getIdentifier(trip.imageName, "drawable", context.packageName)
                        } else 0
                        
                    if (resolvedImageRes != 0) {
                        Image(
                            painter = androidx.compose.ui.res.painterResource(id = resolvedImageRes),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize().background(Color.LightGray))
                    }
                }
                
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(16.dp).background(Color.White.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
            }
            
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-30).dp),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    val tripReviews = allReviews.filter { it.tripTitle == trip.title }
                    val avgRating = if (tripReviews.isNotEmpty()) tripReviews.map { it.rating }.average() else 5.0
                    val reviewCountText = if (tripReviews.isNotEmpty()) "${tripReviews.size} review" else "100 review"
                    
                    Text(trip.title, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                        Icon(Icons.Default.Star, null, tint = Color(0xFFF1C40F), modifier = Modifier.size(18.dp))
                        Text(" %.1f ($reviewCountText) | Open Trip".format(avgRating), fontSize = 13.sp, color = Color.Gray)
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = LightGrey.copy(alpha = 0.2f))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InfoItemDetailRow(Icons.Default.LocationOn, trip.location)
                            InfoItemDetailRow(Icons.Default.CalendarToday, trip.date.ifEmpty { "01 Mei - 04 Mei" })
                            val participants = trip.maxSlots - trip.availableSlots
                            InfoItemDetailRow(Icons.Default.People, "$participants/${trip.maxSlots} Peserta")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = LightGrey.copy(alpha = 0.2f))
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Harga per orang", fontSize = 14.sp)
                                Text("${trip.price}/orang", fontWeight = FontWeight.Bold, color = PriceOrange, fontSize = 18.sp)
                            }
                            Surface(color = Color(0xFFD5E8D4), shape = RoundedCornerShape(8.dp)) {
                                Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ConfirmationNumber, null, modifier = Modifier.size(14.dp), tint = DarkGreen)
                                    Text(" Sisa ${trip.availableSlots} slot", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Tentang Trip", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Nikmati pengalaman tak terlupakan menjelajahi keindahan ${trip.title} di ${trip.location}. Trip ini dirancang khusus untuk kamu yang ingin melepas penat dan menikmati alam.",
                        fontSize = 14.sp, color = Color.Gray, lineHeight = 20.sp
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Itinerary", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    ItineraryItemDetail("Hari ke- 1 | Arrival", "08.00 - 18.00", "Penjemputan di meeting point dan menuju lokasi utama.")
                    ItineraryItemDetail("Hari ke- 2 | Explore Day", "04.30 - 20.00", "Full day tour menikmati spot terbaik di ${trip.title}.")
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Fasilitas", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        FacilityChipDetail(Icons.Default.DirectionsBus, "Transport")
                        FacilityChipDetail(Icons.Default.Hotel, "Penginapan")
                        FacilityChipDetail(Icons.Default.ConfirmationNumber, "Tiket")
                        FacilityChipDetail(Icons.Default.Restaurant, "Makan")
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Ulasan Penjelajah", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (tripReviews.isEmpty()) {
                        Text("Belum ada ulasan untuk trip ini.", color = Color.Gray, fontSize = 14.sp)
                    } else {
                        tripReviews.take(3).forEach { review ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                                    .clickable { navController.navigate("review_detail/${review.id}") },
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = androidx.compose.foundation.BorderStroke(1.dp, LightGrey)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier.size(40.dp).background(Color(0xFFD5E8D4), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Person, null, tint = DarkGreen)
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text("Verified Explorer", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Row {
                                                repeat(review.rating) {
                                                    Icon(Icons.Default.Star, null, tint = Color(0xFFF1C40F), modifier = Modifier.size(12.dp))
                                                }
                                                repeat(5 - review.rating) {
                                                    Icon(Icons.Default.StarBorder, null, tint = Color.LightGray, modifier = Modifier.size(12.dp))
                                                }
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        review.comment,
                                        fontSize = 13.sp,
                                        color = Color.DarkGray,
                                        maxLines = 2,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                    if (review.imageUri != null) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        com.remotivi.mytripmyadventure.ui.components.ReviewImageDisplay(
                                            imageUri = review.imageUri,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(100.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
fun InfoItemDetailRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, modifier = Modifier.size(20.dp), tint = Color.Gray)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, fontSize = 14.sp, color = Color.DarkGray)
    }
}

@Composable
fun ItineraryItemDetail(title: String, time: String, desc: String) {
    Row(modifier = Modifier.padding(vertical = 10.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(14.dp).background(DarkGreen, CircleShape))
            Box(modifier = Modifier.width(1.dp).height(80.dp).background(Color.Gray))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = LightGrey.copy(alpha = 0.15f))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(desc, fontSize = 13.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Text(" $time", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun RowScope.FacilityChipDetail(icon: ImageVector, label: String) {
    Surface(
        color = LightGrey.copy(alpha = 0.5f), 
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.weight(1f) 
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp), 
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
            Spacer(modifier = Modifier.width(4.dp))
            Text(label, fontSize = 9.sp, color = Color.DarkGray, fontWeight = FontWeight.Medium, maxLines = 1)
        }
    }
}
