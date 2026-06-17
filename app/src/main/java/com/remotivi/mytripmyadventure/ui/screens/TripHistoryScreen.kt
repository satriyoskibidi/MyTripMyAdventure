package com.remotivi.mytripmyadventure.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.remotivi.mytripmyadventure.ui.components.TripData
import com.remotivi.mytripmyadventure.ui.components.TripItemCard
import com.remotivi.mytripmyadventure.ui.theme.DarkGreen

@Composable
fun TripHistoryScreen(navController: NavHostController, allTrips: SnapshotStateList<TripData>) {
    val joinedTrips = allTrips.filter { it.isJoined || it.isCompleted }

    val upcomingTrips = joinedTrips.filter { !it.isCompleted }
    val pastTrips = joinedTrips.filter { it.isCompleted }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Trip Mendatang", "Trip Lampau")

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Riwayat Perjalanan", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = DarkGreen,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = DarkGreen
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) DarkGreen else Color.Gray
                        )
                    }
                )
            }
        }

        val currentList = if (selectedTab == 0) upcomingTrips else pastTrips
        val emptyMessage = if (selectedTab == 0) "Belum ada trip mendatang" else "Belum ada trip yang selesai"

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
        ) {
            if (currentList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(emptyMessage, color = Color.Gray)
                    }
                }
            } else {
                items(currentList) { trip ->
                    val statusText = if (trip.isCompleted) "Completed"
                    else if (trip.paid) "Active"
                    else "Menunggu Pembayaran"
                    TripItemCard(
                        trip = trip,
                        status = statusText,
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