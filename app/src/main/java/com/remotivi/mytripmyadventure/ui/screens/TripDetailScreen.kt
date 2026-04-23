package com.remotivi.mytripmyadventure.ui.screens

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.remotivi.mytripmyadventure.Screen
import com.remotivi.mytripmyadventure.ui.theme.DarkGreen
import com.remotivi.mytripmyadventure.ui.theme.LightCream
import com.remotivi.mytripmyadventure.ui.theme.LightGrey
import com.remotivi.mytripmyadventure.ui.theme.PriceOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(tripId: String, navController: NavHostController) {
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
                        Text("Rp3.000.000/orang", color = PriceOrange, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                    Button(
                        onClick = { navController.navigate("payment/$tripId") },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(48.dp).width(120.dp)
                    ) {
                        Text("JOIN TRIP", fontWeight = FontWeight.Bold)
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
            Box(modifier = Modifier.fillMaxWidth().height(250.dp).background(Color.LightGray)) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(16.dp).background(Color.White.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
            }
            
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Bromo & Malang Trip", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = Color(0xFFF1C40F), modifier = Modifier.size(16.dp))
                    Text(" 4.8 (100 review) | Open Trip", fontSize = 12.sp, color = Color.Gray)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = LightGrey.copy(alpha = 0.3f))
                ) {
                    Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        InfoColumn(Icons.Default.LocationOn, "Malang, East Java")
                        InfoColumn(Icons.Default.CalendarToday, "01 Mei - 04 Mei")
                        InfoColumn(Icons.Default.People, "8/12 Peserta")
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = LightGrey.copy(alpha = 0.3f))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Harga per orang", fontSize = 14.sp)
                            Text("Rp3.000.000/orang", fontWeight = FontWeight.Bold, color = PriceOrange)
                        }
                        Surface(color = Color(0xFFD5E8D4), shape = RoundedCornerShape(8.dp)) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ConfirmationNumber, null, modifier = Modifier.size(14.dp))
                                Text(" Sisa 2 slot", fontSize = 10.sp)
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                Text("Tentang Trip", fontWeight = FontWeight.Bold)
                Text(
                    "Trip ini merupakan perjalanan selama 3 hari 2 malam yang akan mengajak kamu menjelajahi keindahan alam dan wisata populer di Bromo dan Malang.",
                    fontSize = 13.sp, color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                Text("Itinerary", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                ItineraryDetailItem("Hari ke- 1 | Malang City Tour", "08.00 - 18.00", "Tiba di Malang, museum, alun-alun, dan wisata kuliner.")
                ItineraryDetailItem("Hari ke- 2 | Bromo Sunrise Tour", "00.30 - 14.00", "Berangkat dini hari ke Bromo, sunrise, kawah, padang savana.")
                ItineraryDetailItem("Hari ke- 3 | Air Terjun & Kembali", "07.00 - 16.00", "Mengunjungi air terjun Madakaripura, oleh-oleh, kembali ke kota.")
                
                Spacer(modifier = Modifier.height(24.dp))
                Text("Fasilitas", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FacilityChip(Icons.Default.DirectionsBus, "Transportasi")
                    FacilityChip(Icons.Default.Hotel, "Penginapan")
                    FacilityChip(Icons.Default.ConfirmationNumber, "Tiket Masuk")
                    FacilityChip(Icons.Default.Restaurant, "Makan")
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun InfoColumn(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontSize = 10.sp, color = Color.Gray)
    }
}

@Composable
fun ItineraryDetailItem(title: String, time: String, desc: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(12.dp).background(DarkGreen, CircleShape))
            Box(modifier = Modifier.width(1.dp).height(60.dp).background(Color.Gray))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = LightGrey.copy(alpha = 0.2f))) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(desc, fontSize = 12.sp, color = Color.Gray)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                    Text(" $time", fontSize = 11.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun FacilityChip(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Surface(color = LightGrey, shape = RoundedCornerShape(8.dp)) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
            Spacer(modifier = Modifier.width(4.dp))
            Text(label, fontSize = 9.sp, color = Color.Gray)
        }
    }
}
