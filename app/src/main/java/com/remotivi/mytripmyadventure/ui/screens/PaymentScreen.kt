package com.remotivi.mytripmyadventure.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.remotivi.mytripmyadventure.R
import com.remotivi.mytripmyadventure.ui.components.TripData
import com.remotivi.mytripmyadventure.ui.theme.DarkGreen
import com.remotivi.mytripmyadventure.ui.theme.LightGrey
import com.remotivi.mytripmyadventure.ui.theme.PriceOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(tripId: String, navController: NavHostController, allTrips: List<TripData>) {
    var selectedMethod by remember { mutableStateOf("Bank BCA") }
    val trip = allTrips.find { it.title == tripId } ?: allTrips[0]

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
                onClick = { /* Implement payment logic */ },
                modifier = Modifier.fillMaxWidth().padding(24.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Bayar Sekarang", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize().padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Surface(color = Color(0xFFFFF9C4), shape = RoundedCornerShape(12.dp)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccessTime, null, tint = Color(0xFFF39C12), modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Selesaikan pembayaran dalam", fontSize = 12.sp)
                            Text("59:48", color = PriceOrange, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Text("Jika melewati batas waktu, pembayaran akan dianggap gagal.", fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }
            }

            item {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        if (trip.imageRes != 0) {
                            Image(
                                painter = painterResource(id = trip.imageRes),
                                contentDescription = null,
                                modifier = Modifier.size(100.dp).clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(modifier = Modifier.size(100.dp).background(Color.LightGray, RoundedCornerShape(12.dp)))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(trip.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(trip.location, fontSize = 12.sp, color = Color.Gray)
                            Text(trip.date, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }

            item {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, LightGrey)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Rincian Pembayaran", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        PaymentDetailRow("Harga Perorang", trip.price)
                        PaymentDetailRow("Jumlah Peserta", "1 Orang")
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Pembayaran", fontWeight = FontWeight.Bold)
                            Text(trip.price, color = PriceOrange, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Text("Pilih Metode Pembayaran", fontWeight = FontWeight.Bold)
            }

            items(listOf(
                PaymentMethodData("Bank BCA", R.drawable.bca),
                PaymentMethodData("Bank BNI", R.drawable.bni),
                PaymentMethodData("Bank BRI", R.drawable.bri),
                PaymentMethodData("QRIS", R.drawable.qris),
                PaymentMethodData("Kartu Kredit/Debit", 0)
            )) { method ->
                PaymentMethodItem(
                    method = method,
                    isSelected = selectedMethod == method.name,
                    onSelect = { selectedMethod = method.name }
                )
            }
            
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

data class PaymentMethodData(val name: String, val iconRes: Int)

@Composable
fun PaymentDetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray, fontSize = 13.sp)
        Text(value, fontSize = 13.sp)
    }
}

@Composable
fun PaymentMethodItem(method: PaymentMethodData, isSelected: Boolean, onSelect: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onSelect() },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (isSelected) DarkGreen else LightGrey),
        color = Color.White
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = isSelected, onClick = onSelect, colors = RadioButtonDefaults.colors(selectedColor = DarkGreen))
            Spacer(modifier = Modifier.width(12.dp))
            if (method.iconRes != 0) {
                Image(
                    painter = painterResource(id = method.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            } else {
                Box(modifier = Modifier.size(40.dp).background(LightGrey, CircleShape))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(method.name, fontWeight = FontWeight.Medium)
        }
    }
}
