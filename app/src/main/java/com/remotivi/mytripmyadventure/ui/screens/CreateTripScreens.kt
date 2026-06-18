package com.remotivi.mytripmyadventure.ui.screens

import com.google.firebase.database.FirebaseDatabase
import com.remotivi.mytripmyadventure.ui.components.TripData
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.runtime.saveable.rememberSaveable
import com.remotivi.mytripmyadventure.viewmodel.CreateTripViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun CreateTripIntroScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
        Text("Buat Open Trip", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        Text("Mulai Buat Trip!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Rencanakan perjalananmu dan ajak penjelajah lain bergabung.", color = Color.Gray)
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
            StepCircle(1, "Detail", currentStep >= 1)
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
fun CreateTripStep1Screen(navController: NavHostController, viewModel: CreateTripViewModel) {
    var tripName = viewModel.tripName.value
    var location = viewModel.location.value
    var destination = viewModel.destination.value
    var duration = viewModel.duration.value
    val imageUri = viewModel.imageUri.value
    var maxCapacity = viewModel.maxCapacity.value
    var pricePerPerson = viewModel.pricePerPerson.value

    val isFormValid = tripName.isNotBlank() && location.isNotBlank() && duration.isNotBlank() && imageUri != null && maxCapacity.isNotBlank() && pricePerPerson.isNotBlank()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        viewModel.imageUri.value = uri
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        StepHeader(1, navController)
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text("Foto Cover Trip", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier.fillMaxWidth().height(180.dp).background(LightGrey.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.CloudUpload, null, modifier = Modifier.size(40.dp))
                        Text("Upload Foto", fontWeight = FontWeight.Bold)
                        Text("JPG/PNG, maks. 5MB", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Nama Trip *", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = tripName, 
                onValueChange = { viewModel.tripName.value = it }, 
                placeholder = { Text("Masukkan Nama Trip") }, 
                modifier = Modifier.fillMaxWidth(), 
                shape = RoundedCornerShape(12.dp),
                isError = tripName.isEmpty(),
                supportingText = { if (tripName.isEmpty()) Text("Nama trip tidak boleh kosong", color = Color.Red, fontSize = 10.sp) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("Destinasi Utama *", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = location, 
                onValueChange = { viewModel.location.value = it }, 
                leadingIcon = { Icon(Icons.Default.LocationOn, null) }, 
                placeholder = { Text("Lokasi utama") }, 
                modifier = Modifier.fillMaxWidth(), 
                shape = RoundedCornerShape(12.dp),
                isError = location.isEmpty(),
                supportingText = { if (location.isEmpty()) Text("Lokasi tidak boleh kosong", color = Color.Red, fontSize = 10.sp) }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            Text("Destinasi Tambahan (Opsional)", fontWeight = FontWeight.Bold)
            OutlinedTextField(value = destination, onValueChange = { viewModel.destination.value = it }, leadingIcon = { Icon(Icons.Default.LocationOn, null) }, placeholder = { Text("Contoh: Kawah Ijen, Baluran") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("Durasi Trip *", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = duration, 
                onValueChange = { viewModel.duration.value = it }, 
                leadingIcon = { Icon(Icons.Default.CalendarToday, null) }, 
                placeholder = { Text("Contoh: 3 Hari 2 Malam") }, 
                modifier = Modifier.fillMaxWidth(), 
                shape = RoundedCornerShape(12.dp),
                isError = duration.isEmpty(),
                supportingText = { if (duration.isEmpty()) Text("Durasi tidak boleh kosong", color = Color.Red, fontSize = 10.sp) }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Kapasitas Peserta *", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = maxCapacity, 
                onValueChange = { input ->
                    viewModel.maxCapacity.value = input.replace(Regex("[^\\d]"), "")
                }, 
                leadingIcon = { Icon(Icons.Default.People, null) }, 
                placeholder = { Text("Contoh: 16") }, 
                modifier = Modifier.fillMaxWidth(), 
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = maxCapacity.isEmpty(),
                supportingText = { if (maxCapacity.isEmpty()) Text("Kapasitas tidak boleh kosong", color = Color.Red, fontSize = 10.sp) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("Harga per Orang *", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = pricePerPerson, 
                onValueChange = { input ->
                    viewModel.pricePerPerson.value = formatRupiah(input)
                }, 
                leadingIcon = { Icon(Icons.Default.Payment, null) }, 
                placeholder = { Text("Contoh: 3000000") }, 
                modifier = Modifier.fillMaxWidth(), 
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = pricePerPerson.isEmpty(),
                supportingText = { if (pricePerPerson.isEmpty()) Text("Harga tidak boleh kosong", color = Color.Red, fontSize = 10.sp) }
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { if (isFormValid) navController.navigate(Screen.CreateTripStep2.route) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isFormValid) DarkGreen else Color.Gray),
                shape = RoundedCornerShape(28.dp),
                enabled = isFormValid
            ) {
                Text("Lanjutkan", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CreateTripStep2Screen(navController: NavHostController, viewModel: CreateTripViewModel) {
    var meetingPoint = viewModel.meetingPoint.value
    var meetingTime = viewModel.meetingTime.value
    
    val itineraryList = viewModel.itineraryList

    val isFormValid = meetingPoint.isNotBlank() && meetingTime.isNotBlank() && itineraryList.isNotEmpty()

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        StepHeader(2, navController)
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text("Itinerary Perjalanan *", fontWeight = FontWeight.Bold)
            Text("Tambahkan rencana perjalanan untuk trip ini", fontSize = 12.sp, color = Color.Gray)
            if (itineraryList.isEmpty()) {
                Text("Minimal harus ada 1 rencana perjalanan", color = Color.Red, fontSize = 10.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            itineraryList.forEachIndexed { index, (title, desc) ->
                ItineraryStepItem(
                    title = title,
                    desc = desc,
                    onDescChange = { newDesc -> itineraryList[index] = title to newDesc },
                    onRemove = { itineraryList.removeAt(index) }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth().height(56.dp)
                    .clickable { itineraryList.add("Day ${itineraryList.size + 1}" to "") }
                    .background(LightGrey.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Tambah Hari", color = Color.Gray)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("Meeting Point *", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = meetingPoint, 
                onValueChange = { viewModel.meetingPoint.value = it }, 
                placeholder = { Text("Masukkan Tempat Meeting Point") }, 
                modifier = Modifier.fillMaxWidth(), 
                shape = RoundedCornerShape(12.dp),
                isError = meetingPoint.isEmpty(),
                supportingText = { if (meetingPoint.isEmpty()) Text("Meeting point wajib diisi", color = Color.Red, fontSize = 10.sp) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("Waktu Meeting Point *", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = meetingTime, 
                onValueChange = { viewModel.meetingTime.value = it }, 
                placeholder = { Text("Contoh: 08:00 WIB") }, 
                modifier = Modifier.fillMaxWidth(), 
                shape = RoundedCornerShape(12.dp),
                isError = meetingTime.isEmpty(),
                supportingText = { if (meetingTime.isEmpty()) Text("Waktu wajib diisi", color = Color.Red, fontSize = 10.sp) }
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { if (isFormValid) navController.navigate(Screen.CreateTripStep3.route) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isFormValid) DarkGreen else Color.Gray),
                shape = RoundedCornerShape(28.dp),
                enabled = isFormValid
            ) {
                Text("Lanjutkan", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ItineraryStepItem(title: String, desc: String, onDescChange: (String) -> Unit, onRemove: () -> Unit) {
    Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.Top) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(10.dp).background(DarkGreen, CircleShape))
            Box(modifier = Modifier.width(1.dp).height(80.dp).background(Color.Gray))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Card(modifier = Modifier.weight(1f).border(1.dp, LightGrey, RoundedCornerShape(12.dp)), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(title, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onRemove) {
                        Icon(Icons.Default.Delete, null, tint = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = desc,
                    onValueChange = onDescChange,
                    placeholder = { Text("Deskripsi aktivitas", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                )
            }
        }
    }
}

@Composable
fun CreateTripStep3Screen(navController: NavHostController, viewModel: CreateTripViewModel) {
    val selectedTransport = viewModel.selectedTransport
    var selectedAcomodation = viewModel.selectedAcomodation.value
    var selectedMakan = viewModel.selectedMakan.value
    var selectedTiket = viewModel.selectedTiket.value
    val selectedLainnya = viewModel.selectedLainnya

    val isFormValid = selectedTransport.isNotEmpty() && selectedAcomodation.isNotEmpty()

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        StepHeader(3, navController)
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text("Fasilitas yang Didapatkan *", fontWeight = FontWeight.Bold)
            Text("Pilih fasilitas yang termasuk dalam trip ini", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            
            FacilitySectionMulti("Transportasi *", listOf("Motor", "Mobil", "Bus / Travel", "Pesawat"), selectedTransport)
            FacilitySectionSingle("Akomodasi *", listOf("Hotel", "Homestay", "Camping", "Tidak Termasuk"), selectedAcomodation) { viewModel.selectedAcomodation.value = it }
            FacilitySectionSingle("Makan", listOf("Termasuk", "Tidak Termasuk"), selectedMakan) { viewModel.selectedMakan.value = it }
            FacilitySectionSingle("Tiket Masuk & Wisata", listOf("Termasuk", "Tidak Termasuk"), selectedTiket) { viewModel.selectedTiket.value = it }
            FacilitySectionMulti("Fasilitas Lainnya", listOf("Tour Leader", "Air Mineral", "Asuransi", "P3K", "Dokumentasi", "Snack"), selectedLainnya)
            
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { if (isFormValid) navController.navigate(Screen.CreateTripStep4.route) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isFormValid) DarkGreen else Color.Gray),
                shape = RoundedCornerShape(28.dp),
                enabled = isFormValid
            ) {
                Text("Lanjutkan", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun FacilitySectionMulti(title: String, items: List<String>, selectedItems: MutableList<String>) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                val isSelected = selectedItems.contains(item)
                Surface(
                    onClick = { 
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
fun FacilitySectionSingle(title: String, items: List<String>, selectedItem: String, onItemSelected: (String) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                val isSelected = selectedItem == item
                Surface(
                    onClick = { onItemSelected(item) },
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
fun CreateTripStep4Screen(navController: NavHostController, viewModel: CreateTripViewModel) {
    val context = LocalContext.current
    val tripName = viewModel.tripName.value
    val location = viewModel.location.value
    val duration = viewModel.duration.value
    val meetingPoint = viewModel.meetingPoint.value
    val meetingTime = viewModel.meetingTime.value
    val imageUri = viewModel.imageUri.value
    
    val allFacilities = mutableListOf<String>()
    allFacilities.addAll(viewModel.selectedTransport)
    if (viewModel.selectedAcomodation.value.isNotEmpty()) allFacilities.add(viewModel.selectedAcomodation.value)
    if (viewModel.selectedMakan.value.isNotEmpty()) allFacilities.add(viewModel.selectedMakan.value)
    if (viewModel.selectedTiket.value.isNotEmpty()) allFacilities.add(viewModel.selectedTiket.value)
    allFacilities.addAll(viewModel.selectedLainnya)

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        StepHeader(4, navController)
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text("Review Trip", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Periksa kembali detail trip sebelum di publish", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(modifier = Modifier.padding(12.dp)) {
                    Box(modifier = Modifier.size(80.dp).background(Color.LightGray, RoundedCornerShape(8.dp)).clip(RoundedCornerShape(8.dp))) {
                        if (imageUri != null) {
                            AsyncImage(
                                model = imageUri,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(tripName, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                            Text(location, fontSize = 12.sp, color = Color.Gray)
                        }
                        Text(duration, fontSize = 12.sp, color = Color.Gray)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Flag, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                            Text(" Trip Mendatang", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("Rincian Trip", fontWeight = FontWeight.Bold)
            DetailRow("Meeting Point", meetingPoint)
            DetailRow("Waktu Meeting", meetingTime)
            DetailRow("Durasi Trip", duration)
            DetailRow("Kapasitas Peserta", "${viewModel.maxCapacity.value} Orang")
            DetailRow("Harga per Orang", viewModel.pricePerPerson.value)
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("Itinerary", fontWeight = FontWeight.Bold)
            Column(modifier = Modifier.padding(start = 8.dp)) {
                viewModel.itineraryList.forEach { (title, _) ->
                    ItinerarySummaryItem(title)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Fasilitas", fontWeight = FontWeight.Bold)
            Text(allFacilities.joinToString(", "), fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { 
                    var base64Image = ""
                    if (imageUri != null) {
                        try {
                            val inputStream = context.contentResolver.openInputStream(imageUri)
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            val outputStream = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream) // Compress to 20%
                            val byteArray = outputStream.toByteArray()
                            base64Image = "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    val newTrip = TripData(
                        id = "",
                        title = tripName,
                        location = location,
                        date = duration,
                        price = viewModel.pricePerPerson.value,
                        category = "Open Trip",
                        imageName = base64Image,
                        maxSlots = viewModel.maxCapacity.value.toIntOrNull() ?: 10,
                        availableSlots = viewModel.maxCapacity.value.toIntOrNull() ?: 10,
                        creatorId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    )
                    
                    val database = FirebaseDatabase.getInstance()
                    val tripsRef = database.getReference("trips")
                    val newRef = tripsRef.push()
                    newTrip.id = newRef.key ?: ""
                    newRef.setValue(newTrip)

                    viewModel.reset()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0)
                    }
                },
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

fun formatRupiah(value: String): String {
    val cleanString = value.replace(Regex("[^\\d]"), "")
    if (cleanString.isEmpty()) return ""
    val parsed = cleanString.toLongOrNull() ?: return ""
    val formatter = java.text.DecimalFormat("#,###")
    val symbols = formatter.decimalFormatSymbols
    symbols.groupingSeparator = '.'
    formatter.decimalFormatSymbols = symbols
    return "Rp" + formatter.format(parsed)
}
