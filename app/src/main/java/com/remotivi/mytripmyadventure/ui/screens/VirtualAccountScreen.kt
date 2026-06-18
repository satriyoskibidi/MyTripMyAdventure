package com.remotivi.mytripmyadventure.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.database.FirebaseDatabase
import com.remotivi.mytripmyadventure.ui.components.TripData
import com.remotivi.mytripmyadventure.ui.theme.DarkGreen
import com.remotivi.mytripmyadventure.ui.theme.PriceOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtualAccountScreen(tripId: String, method: String, quantity: Int, navController: NavHostController, allTrips: List<TripData>) {
    val trip = allTrips.find { it.title == tripId } ?: allTrips[0]
    val context = LocalContext.current
    val vaNumber = "8077 0812 3456 7890"

    val pricePerPersonLong = remember(trip.price) {
        val clean = trip.price.replace(Regex("[^0-9]"), "")
        clean.toLongOrNull() ?: 0L
    }
    val totalPriceString = remember(pricePerPersonLong, quantity) {
        val total = pricePerPersonLong * quantity
        val formatter = java.text.DecimalFormat("#,###")
        val symbols = formatter.decimalFormatSymbols
        symbols.groupingSeparator = '.'
        formatter.decimalFormatSymbols = symbols
        "Rp" + formatter.format(total)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pembayaran", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    if (trip.id.isNotEmpty()) {
                        val database = FirebaseDatabase.getInstance()
                        val tripRef = database.getReference("trips").child(trip.id)
                        tripRef.child("paid").setValue(true)
                        tripRef.child("joined").setValue(true)
                        if (trip.availableSlots >= quantity) {
                            tripRef.child("availableSlots").setValue(trip.availableSlots - quantity)
                        } else {
                            tripRef.child("availableSlots").setValue(0)
                        }
                    }
                    val encodedMethod = android.net.Uri.encode(method)
                    val encodedTripId = android.net.Uri.encode(tripId)
                    navController.navigate("payment_success/${encodedTripId}?method=${encodedMethod}")
                },
                modifier = Modifier.fillMaxWidth().padding(24.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Cek Status Pembayaran", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Text("Selesaikan Pembayaran", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(totalPriceString, color = PriceOrange, fontWeight = FontWeight.Bold, fontSize = 28.sp)
            
            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Metode Pembayaran", fontSize = 14.sp, color = Color.Gray)
                    Text(method, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text("Nomor Virtual Account", fontSize = 14.sp, color = Color.Gray)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(vaNumber, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = DarkGreen)
                        IconButton(onClick = {
                            Toast.makeText(context, "Nomor disalin!", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = DarkGreen)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Silakan transfer tepat sesuai nominal ke nomor Virtual Account di atas.",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
