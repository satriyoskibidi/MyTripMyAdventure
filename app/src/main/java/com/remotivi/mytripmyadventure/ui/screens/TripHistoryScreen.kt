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
import com.google.firebase.database.FirebaseDatabase

@Composable
fun TripHistoryScreen(navController: NavHostController, allTrips: SnapshotStateList<TripData>) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Trip Mendatang", "Trip Lampau")
    
    val upcomingTrips = allTrips.filter { it.isJoined && !it.isCompleted }
    val pastTrips = allTrips.filter { it.isCompleted }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text("Riwayat Perjalanan", fontSize = 20.sp, fontWeight = FontWeight.Bold)
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
                if (upcomingTrips.isEmpty()) {
                    item { Text("Belum ada trip mendatang", color = Color.Gray, modifier = Modifier.padding(16.dp)) }
                } else {
                    items(upcomingTrips) { trip ->
                        TripItemCard(
                            trip = trip,
                            status = if (trip.paid) "Active" else "Menunggu Pembayaran",
                            showFavoriteIcon = false,
                            onClick = { 
                                val encodedTitle = android.net.Uri.encode(trip.title)
                                navController.navigate("trip_detail/$encodedTitle") 
                            }
                        )
                    }
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                if (pastTrips.isEmpty()) {
                    item { Text("Belum ada trip lampau", color = Color.Gray, modifier = Modifier.padding(16.dp)) }
                } else {
                    items(pastTrips) { trip ->
                        TripItemCard(
                            trip = trip,
                            status = "Completed",
                            showFavoriteIcon = false,
                            onClick = { 
                                val encodedTitle = android.net.Uri.encode(trip.title)
                                navController.navigate("trip_detail/$encodedTitle") 
                            }
                        )
                    }
                }
            }
        }
    }
}
