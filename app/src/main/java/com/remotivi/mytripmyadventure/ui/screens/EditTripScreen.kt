package com.remotivi.mytripmyadventure.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.database.FirebaseDatabase
import com.remotivi.mytripmyadventure.ui.components.TripData
import com.remotivi.mytripmyadventure.ui.theme.DarkGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTripScreen(tripId: String, navController: NavHostController, allTrips: List<TripData>) {
    val trip = allTrips.find { it.title == tripId }
    val context = LocalContext.current

    if (trip == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Trip tidak ditemukan")
        }
        return
    }

    var tripName by remember { mutableStateOf(trip.title) }
    var location by remember { mutableStateOf(trip.location) }
    var duration by remember { mutableStateOf(trip.date) }
    var maxCapacity by remember { mutableStateOf(trip.maxSlots.toString()) }
    var pricePerPerson by remember { mutableStateOf(trip.price) }

    val isFormValid = tripName.isNotBlank() && location.isNotBlank() && duration.isNotBlank() && maxCapacity.isNotBlank() && pricePerPerson.isNotBlank()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Trip", fontWeight = FontWeight.Bold) },
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text("Nama Trip *", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = tripName,
                onValueChange = { tripName = it },
                placeholder = { Text("Masukkan Nama Trip") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                isError = tripName.isEmpty()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Destinasi Utama *", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                placeholder = { Text("Lokasi utama") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                isError = location.isEmpty()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Durasi Trip *", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                placeholder = { Text("Contoh: 3 Hari 2 Malam") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                isError = duration.isEmpty()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Kapasitas Peserta *", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = maxCapacity,
                onValueChange = { input ->
                    maxCapacity = input.replace(Regex("[^\\d]"), "")
                },
                leadingIcon = { Icon(Icons.Default.People, null) },
                placeholder = { Text("Contoh: 16") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = maxCapacity.isEmpty()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Harga per Orang *", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = pricePerPerson,
                onValueChange = { input ->
                    pricePerPerson = formatRupiahLocal(input)
                },
                leadingIcon = { Icon(Icons.Default.Payment, null) },
                placeholder = { Text("Contoh: 3000000") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = pricePerPerson.isEmpty()
            )

            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = {
                    if (isFormValid) {
                        val database = FirebaseDatabase.getInstance()
                        val tripRef = database.getReference("trips").child(trip.id)
                        
                        val newMax = maxCapacity.toIntOrNull() ?: 10
                        val diff = newMax - trip.maxSlots
                        val newAvailable = (trip.availableSlots + diff).coerceAtLeast(0)

                        val updates = hashMapOf<String, Any>(
                            "title" to tripName,
                            "location" to location,
                            "date" to duration,
                            "maxSlots" to newMax,
                            "availableSlots" to newAvailable,
                            "price" to pricePerPerson
                        )

                        tripRef.updateChildren(updates).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Trip berhasil diupdate", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, "Gagal mengupdate trip", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Simpan Perubahan", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private fun formatRupiahLocal(value: String): String {
    val cleanString = value.replace(Regex("[^\\d]"), "")
    if (cleanString.isEmpty()) return ""
    val parsed = cleanString.toLongOrNull() ?: return ""
    val formatter = java.text.DecimalFormat("#,###")
    val symbols = formatter.decimalFormatSymbols
    symbols.groupingSeparator = '.'
    formatter.decimalFormatSymbols = symbols
    return "Rp" + formatter.format(parsed)
}

