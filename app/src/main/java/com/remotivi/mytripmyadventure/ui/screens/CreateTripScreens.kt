package com.remotivi.mytripmyadventure.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.remotivi.mytripmyadventure.Screen
import com.remotivi.mytripmyadventure.ui.theme.DarkGreen
import com.remotivi.mytripmyadventure.ui.theme.LightGrey

@Composable
fun CreateTripIntroScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
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
        Spacer(modifier = Modifier.height(40.dp))
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
fun InfoItem(icon: ImageVector, text: String) {
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
                color = Color.Gray.copy(alpha = 0.5f),
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
    var tripName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        StepHeader(1, navController)
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text("Foto Cover Trip", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier.fillMaxWidth().height(180.dp).background(LightGrey.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
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
            OutlinedTextField(value = tripName, onValueChange = { tripName = it }, placeholder = { Text("Masukkan Nama Trip") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Destinasi", fontWeight = FontWeight.Bold)
            OutlinedTextField(value = location, onValueChange = { location = it }, leadingIcon = { Icon(Icons.Default.LocationOn, null) }, placeholder = { Text("Lokasi utama") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = destination, onValueChange = { destination = it }, leadingIcon = { Icon(Icons.Default.LocationOn, null) }, placeholder = { Text("Destinasi Tambahan (Opsional)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Durasi Trip", fontWeight = FontWeight.Bold)
            OutlinedTextField(value = duration, onValueChange = { duration = it }, leadingIcon = { Icon(Icons.Default.CalendarToday, null) }, placeholder = { Text("Tanggal berangkat -> Tanggal pulang") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { navController.navigate(Screen.CreateTripStep2.route) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Lanjutkan", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CreateTripStep2Screen(navController: NavHostController) {
    var meetingPoint by remember { mutableStateOf("") }
    var meetingTime by remember { mutableStateOf("") }
    
    val itineraryList = remember { mutableStateListOf(
        "Day 1 - Berangkat" to "Meeting point di Bromo",
        "Day 2 - Bromo Sunrise Tour" to "blablabla",
        "Day 3 - Explore Malang" to "blablabla"
    ) }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        StepHeader(2, navController)
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text("Itinerary Perjalanan", fontWeight = FontWeight.Bold)
            Text("Tambahkan rencana perjalanan untuk trip ini", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            
            itineraryList.forEachIndexed { index, (title, desc) ->
                ItineraryStepItem(title, desc, onRemove = { itineraryList.removeAt(index) })
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth().height(56.dp)
                    .clickable { itineraryList.add("Day ${itineraryList.size + 1}" to "Deskripsi aktivitas") }
                    .background(LightGrey.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Tambah Hari", color = Color.Gray)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("Meeting Point", fontWeight = FontWeight.Bold)
            OutlinedTextField(value = meetingPoint, onValueChange = { meetingPoint = it }, placeholder = { Text("Masukkan Tempat Meeting Point") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Waktu Meeting Point", fontWeight = FontWeight.Bold)
            OutlinedTextField(value = meetingTime, onValueChange = { meetingTime = it }, placeholder = { Text("Masukkan waktu meeting point") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { navController.navigate(Screen.CreateTripStep3.route) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Lanjutkan", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ItineraryStepItem(title: String, desc: String, onRemove: () -> Unit) {
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
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, null, tint = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun CreateTripStep3Screen(navController: NavHostController) {
    val selectedTransport = remember { mutableStateListOf("Motor") }
    val selectedAcomodation = remember { mutableStateOf("Hotel") }
    val selectedMakan = remember { mutableStateOf("Termasuk") }
    val selectedTiket = remember { mutableStateOf("Termasuk") }
    val selectedLainnya = remember { mutableStateListOf("Air Mineral", "Asuransi", "Dokumentasi", "Snack") }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        StepHeader(3, navController)
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text("Fasilitas yang Didapatkan", fontWeight = FontWeight.Bold)
            Text("Pilih fasilitas yang termasuk dalam trip ini", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            
            FacilitySectionMulti("Transportasi", listOf("Motor", "Mobil", "Bus / Travel", "Pesawat"), selectedTransport)
            FacilitySectionSingle("Akomodasi", listOf("Hotel", "Homestay", "Camping", "Tidak Termasuk"), selectedAcomodation)
            FacilitySectionSingle("Makan", listOf("Termasuk", "Tidak Termasuk"), selectedMakan)
            FacilitySectionSingle("Tiket Masuk & Wisata", listOf("Termasuk", "Tidak Termasuk"), selectedTiket)
            FacilitySectionMulti("Fasilitas Lainnya", listOf("Tour Leader", "Air Mineral", "Asuransi", "P3K", "Dokumentasi", "Snack"), selectedLainnya)
            
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { navController.navigate(Screen.CreateTripStep4.route) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Lanjutkan", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FacilitySectionMulti(title: String, items: List<String>, selectedItems: MutableList<String>) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        androidx.compose.foundation.layout.FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), maxItemsInEachRow = 4) {
            items.forEach { item ->
                val isSelected = selectedItems.contains(item)
                Surface(
                    modifier = Modifier.padding(vertical = 4.dp).clickable { 
                        if (isSelected) selectedItems.remove(item) else selectedItems.add(item)
                    },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, if (isSelected) DarkGreen else Color.Gray),
                    color = if (isSelected) Color(0xFFD5E8D4) else Color.White
                ) {
                    Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), contentAlignment = Alignment.Center) {
                        Text(item, fontSize = 10.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Composable
fun FacilitySectionSingle(title: String, items: List<String>, selectedItem: MutableState<String>) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items.forEach { item ->
                val isSelected = selectedItem.value == item
                Surface(
                    modifier = Modifier.weight(1f).clickable { selectedItem.value = item },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, if (isSelected) DarkGreen else Color.Gray),
                    color = if (isSelected) Color(0xFFD5E8D4) else Color.White
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
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Flag, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                            Text(" Trip Mendatang", fontSize = 12.sp, color = Color.Gray)
                        }
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
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("Itinerary", fontWeight = FontWeight.Bold)
            Column(modifier = Modifier.padding(start = 8.dp)) {
                ItinerarySummaryItem("Day 1 - Berangkat")
                ItinerarySummaryItem("Day 2 - Bromo Sunrise Tour")
                ItinerarySummaryItem("Day 3 - Explore Malang")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Fasilitas", fontWeight = FontWeight.Bold)
            Text("Motor, Hotel, Makan Termasuk, Tiket Masuk Termasuk, Air Mineral, Asuransi, Dokumentasi, Snack", fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { navController.navigate(Screen.Home.route) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Publish Trip", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ItinerarySummaryItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Box(modifier = Modifier.size(8.dp).background(DarkGreen, CircleShape))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, fontSize = 12.sp)
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
}
