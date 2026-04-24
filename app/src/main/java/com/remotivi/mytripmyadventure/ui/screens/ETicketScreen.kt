package com.remotivi.mytripmyadventure.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
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
fun ETicketScreen(tripId: String, navController: NavHostController, allTrips: List<TripData>) {
    val trip = allTrips.find { it.title == tripId } ?: allTrips[0]

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("E-Ticket", fontWeight = FontWeight.Bold) },
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
                .background(Color(0xFFF8F9F9))
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ticket Container
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Header Trip Info
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(trip.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                        Text("Terkonfirmasi", color = Color(0xFF27AE60), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            TicketInfoItem("Nama Pemesan", "John Doe")
                            TicketInfoItem("Jumlah Peserta", "1 Orang", Alignment.End)
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            TicketInfoItem("Tanggal Berangkat", trip.date.ifEmpty { "01 Mei 2024" })
                            TicketInfoItem("Meeting Point", "Stasiun Malang", Alignment.End)
                        }
                    }

                    // Dashed Line
                    TicketDividerEffect()

                    // QR Code and Payment Info
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Scan QR untuk Check-in", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .size(180.dp)
                                .background(LightGrey.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.QrCode2, null, modifier = Modifier.size(140.dp), tint = Color.Black)
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        HorizontalDivider(color = LightGrey)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("Rincian Pembayaran", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        TicketPaymentRow("ID Transaksi", "TRX-9928371")
                        TicketPaymentRow("Metode Pembayaran", "Bank BCA")
                        TicketPaymentRow("Status Pembayaran", "Lunas")
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DarkGreen.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Bayar", fontWeight = FontWeight.Bold, color = DarkGreen)
                            Text(trip.price, fontWeight = FontWeight.Bold, color = PriceOrange)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { /* Download PDF */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Download E-Ticket (PDF)", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun TicketInfoItem(label: String, value: String, alignment: Alignment.Horizontal = Alignment.Start) {
    Column(horizontalAlignment = alignment) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun TicketDividerEffect() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
            drawLine(
                color = Color.LightGray,
                start = Offset(0f, 0.5f),
                end = Offset(size.width, 0.5f),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(modifier = Modifier.offset(x = (-15).dp).size(30.dp).background(Color(0xFFF8F9F9), CircleShape))
            Box(modifier = Modifier.offset(x = (15).dp).size(30.dp).background(Color(0xFFF8F9F9), CircleShape))
        }
    }
}

@Composable
private fun TicketPaymentRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 13.sp, color = Color.Gray)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}
