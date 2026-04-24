package com.remotivi.mytripmyadventure.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.remotivi.mytripmyadventure.ui.components.TripData
import com.remotivi.mytripmyadventure.ui.theme.DarkGreen
import com.remotivi.mytripmyadventure.ui.theme.LightGrey
import com.remotivi.mytripmyadventure.ui.theme.PriceOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(tripId: String, navController: NavHostController, allTrips: List<TripData>) {
    // Cari data trip berdasarkan judul (tripId)
    val trip = allTrips.find { it.title == tripId } ?: allTrips[0]

    Scaffold(
        bottomBar = {
            Surface(tonalElevation = 8.dp, color = Color.White) {
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
                        onClick = { navController.navigate("payment/${trip.title}") },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(52.dp).width(140.dp)
                    ) {
                        Text("JOIN TRIP", fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                // Header Image from Drawable
                if (trip.imageRes != 0) {
                    Image(
                        painter = painterResource(id = trip.imageRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize().background(Color.LightGray))
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
                    Text(trip.title, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                        Icon(Icons.Default.Star, null, tint = Color(0xFFF1C40F), modifier = Modifier.size(18.dp))
                        Text(" 4.8 (100 review) | Open Trip", fontSize = 13.sp, color = Color.Gray)
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
                            InfoItemDetailRow(Icons.Default.People, "8/12 Peserta")
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
                                    Text(" Sisa 2 slot", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
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
