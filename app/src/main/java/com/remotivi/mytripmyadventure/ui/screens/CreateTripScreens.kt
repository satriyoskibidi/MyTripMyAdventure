package com.remotivi.mytripmyadventure.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.remotivi.mytripmyadventure.Screen
import com.remotivi.mytripmyadventure.ui.components.TripItemCard
import com.remotivi.mytripmyadventure.ui.components.TripData
import com.remotivi.mytripmyadventure.ui.theme.DarkGreen
import com.remotivi.mytripmyadventure.ui.theme.LightCream
import com.remotivi.mytripmyadventure.ui.theme.LightGrey
import com.remotivi.mytripmyadventure.ui.theme.PriceOrange

@Composable
fun CreateTripIntroScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Buat Open Trip", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Mulai Buat Trip!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Rencanakan perjalananmu dan ajak penjelajah lain bergabung.", color = Color.Gray)
            }
            Box(modifier = Modifier.size(100.dp).background(Color(0xFFAED6F1), RoundedCornerShape(16.dp))) // Placeholder image
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text("Tentang Trip", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, LightGrey)) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).background(Color(0xFFD5E8D4), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Groups, null, tint = DarkGreen)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Open Trip", fontWeight = FontWeight.Bold)
                    Text("Trip yang bisa diikuti oleh siapa saja dan akan ditampilkan di halaman explore.", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = LightGrey.copy(alpha = 0.3f))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Tentang Trip", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                InfoItem(Icons.Default.Public, "Trip kamu akan ditampilkan di halaman explore.")
                InfoItem(Icons.Default.People, "Penjelajah lain bisa melihat dan bergabung dengan trip.")
                InfoItem(Icons.Default.DirectionsBus, "Cocok untuk berbagi pengalaman perjalanan.")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        DashedInfoBox("Hanya 4 langkah untuk membuat open trip!\nMudah, cepat, dan siap untuk dipublish.")
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navController.navigate(Screen.CreateTripStep1.route) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Mulai Buat Trip", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun InfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, modifier = Modifier.size(20.dp), tint = DarkGreen)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, fontSize = 12.sp)
    }
}

@Composable
fun DashedInfoBox(text: String) {
    Box(modifier = Modifier.fillMaxWidth().height(80.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                color = Color(0xFF27AE60).copy(alpha = 0.3f),
                style = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
            )
        }
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Lightbulb, null, tint = Color(0xFFF1C40F))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text, fontSize = 12.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepHeader(currentStep: Int, navController: NavHostController) {
    Column {
        CenterAlignedTopAppBar(
            title = { Text("Buat Open Trip", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
            }
        )
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            StepCircle(1, "Detail Trip", currentStep >= 1)
            Box(modifier = Modifier.weight(1f).height(1.dp).background(LightGrey))
            StepCircle(2, "Itinerary", currentStep >= 2)
            Box(modifier = Modifier.weight(1f).height(1.dp).background(LightGrey))
            StepCircle(3, "Fasilitas", currentStep >= 3)
            Box(modifier = Modifier.weight(1f).height(1.dp).background(LightGrey))
            StepCircle(4, "Review", currentStep >= 4)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun StepCircle(step: Int, label: String, isActive: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(32.dp).background(if (isActive) DarkGreen else Color.Gray, CircleShape), contentAlignment = Alignment.Center) {
            Text(step.toString(), color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 10.sp, fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun CreateTripStep1Screen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        StepHeader(1, navController)
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text("Foto Cover Trip", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier.fillMaxWidth().height(180.dp).background(LightGrey.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.CloudUpload, null, modifier = Modifier.size(40.dp))
                    Text("Upload Foto", fontWeight = FontWeight.Bold)
                    Text("JPG/PNG, maks. 5MB", fontSize = 10.sp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Nama Trip", fontWeight = FontWeight.Bold)
            OutlinedTextField(value = "", onValueChange = {}, placeholder = { Text("Masukkan Nama Trip") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Text("Destinasi", fontWeight = FontWeight.Bold)
            OutlinedTextField(value = "", onValueChange = {}, leadingIcon = { Icon(Icons.Default.LocationOn, null) }, placeholder = { Text("Lokasi utama") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = "", onValueChange = {}, leadingIcon = { Icon(Icons.Default.LocationOn, null) }, placeholder = { Text("Destinasi Tambahan (Opsional)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Text("Durasi Trip", fontWeight = FontWeight.Bold)
            OutlinedTextField(value = "", onValueChange = {}, leadingIcon = { Icon(Icons.Default.CalendarToday, null) }, placeholder = { Text("Tanggal berangkat -> Tanggal pulang") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.navigate(Screen.CreateTripStep2.route) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Lanjutkan", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CreateTripStep2Screen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        StepHeader(2, navController)
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text("Itinerary Perjalanan", fontWeight = FontWeight.Bold)
            Text("Tambahkan rencana perjalanan untuk trip ini", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            
            Column {
                ItineraryStepItem("Day 1 - Berangkat", "Meeting point di Bromo")
                ItineraryStepItem("Day 2 - Bromo Sunrise Tour", "blablabla")
                ItineraryStepItem("Day 3 - Explore Malang", "blablabla")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth().height(48.dp).background(Color.White, RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Tambah Hari", color = Color.Gray)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("Meeting Point", fontWeight = FontWeight.Bold)
            OutlinedTextField(value = "", onValueChange = {}, placeholder = { Text("Masukkan Tempat Meeting Point") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Text("Waktu Meeting Point", fontWeight = FontWeight.Bold)
            OutlinedTextField(value = "", onValueChange = {}, placeholder = { Text("Masukkan waktu meeting point") }, modifier = Modifier.fillMaxWidth())
            
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.navigate(Screen.CreateTripStep3.route) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Lanjutkan", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ItineraryStepItem(title: String, desc: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.Top) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(10.dp).background(DarkGreen, CircleShape))
            Box(modifier = Modifier.width(1.dp).height(50.dp).background(Color.Gray))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Card(modifier = Modifier.weight(1f).border(1.dp, LightGrey, RoundedCornerShape(12.dp)), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.Bold)
                    Text(desc, fontSize = 12.sp, color = Color.Gray)
                }
                Icon(Icons.Default.Delete, null, tint = Color.Gray)
            }
        }
    }
}

@Composable
fun CreateTripStep3Screen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        StepHeader(3, navController)
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text("Fasilitas yang Didapatkan", fontWeight = FontWeight.Bold)
            Text("Pilih fasilitas yang termasuk dalam trip ini", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            
            FacilitySection("Transportasi", listOf("Motor", "Mobil", "Bus / Travel", "Pesawat"))
            FacilitySection("Akomodasi", listOf("Hotel", "Homestay", "Camping", "Tidak Termasuk"))
            FacilitySection("Makan", listOf("Termasuk", "Tidak Termasuk"))
            
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.navigate(Screen.CreateTripStep4.route) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Lanjutkan", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FacilitySection(title: String, items: List<String>) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items.forEach { item ->
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Gray),
                    color = if (item == "Motor" || item == "Hotel" || item == "Termasuk") Color(0xFFD5E8D4) else Color.White
                ) {
                    Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
                        Text(item, fontSize = 10.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Composable
fun CreateTripStep4Screen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        StepHeader(4, navController)
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text("Review Trip", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Periksa kembali detail trip sebelum di publish", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(modifier = Modifier.padding(12.dp)) {
                    Box(modifier = Modifier.size(80.dp).background(Color.LightGray, RoundedCornerShape(8.dp)))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Bromo & Malang Trip", fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                            Text("Malang, Jawa Timur", fontSize = 12.sp, color = Color.Gray)
                        }
                        Text("24 April - 26 April 2026", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("Rincian Trip", fontWeight = FontWeight.Bold)
            DetailRow("Meeting Point", "Stasiun Malang")
            DetailRow("Waktu Meeting", "08:00 WIB")
            DetailRow("Durasi Trip", "3 Hari 2 Malam")
            DetailRow("Kapasitas Peserta", "8/16 Orang")
            DetailRow("Harga per Orang", "Rp3.000.000")
            
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.navigate(Screen.Home.route) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Publish Trip", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
}
