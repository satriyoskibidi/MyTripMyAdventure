package com.remotivi.mytripmyadventure.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.remotivi.mytripmyadventure.Screen
import com.remotivi.mytripmyadventure.ui.components.TripData
import com.remotivi.mytripmyadventure.ui.components.TripItemCard
import com.remotivi.mytripmyadventure.ui.theme.DarkGreen
import com.remotivi.mytripmyadventure.ui.theme.LightGrey

@Composable
fun TripHistoryScreen(navController: NavHostController, allTrips: SnapshotStateList<TripData>) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Trip Mendatang", "Trip Lampau")

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Notifications, null)
            Text("Riwayat Perjalanan", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Icon(Icons.Default.Notifications, null, tint = Color.Transparent)
        }
        Spacer(modifier = Modifier.height(24.dp))
        
        Surface(color = Color(0xFFEFEFEF), shape = RoundedCornerShape(12.dp)) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = DarkGreen,
                divider = {},
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = DarkGreen
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { 
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(if (index == 0) Icons.Default.CalendarToday else Icons.Default.Flag, null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(title, fontSize = 12.sp, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        if (selectedTab == 0) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    val bromoTrip = allTrips.find { it.title.contains("Bromo") } ?: allTrips[0]
                    TripItemCard(
                        trip = bromoTrip,
                        status = "Active",
                        showFavoriteIcon = false
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { navController.navigate("e_ticket/${bromoTrip.title}") },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE67E22)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ConfirmationNumber, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("E-Ticket", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                item {
                    val merbabuTrip = allTrips.find { it.title.contains("Merbabu") } ?: allTrips[1]
                    TripItemCard(
                        trip = merbabuTrip,
                        status = "Menunggu Pembayaran",
                        showFavoriteIcon = false
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { navController.navigate("payment/${merbabuTrip.title}") },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE67E22)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Bayar Sekarang", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    val rinjaniTrip = allTrips.find { it.title.contains("Rinjani") } ?: allTrips[2]
                    TripItemCard(
                        trip = rinjaniTrip,
                        status = "Completed",
                        showFavoriteIcon = false
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { navController.navigate("review/${rinjaniTrip.title}") },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Text("Beri Ulasan", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
