package com.remotivi.mytripmyadventure.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.remotivi.mytripmyadventure.Screen
import com.remotivi.mytripmyadventure.ui.components.*
import com.remotivi.mytripmyadventure.ui.theme.*

@Composable
fun HomeScreen(navController: NavHostController) {
    val categories = listOf(
        Category("Mountain", Icons.Default.Terrain, true),
        Category("Beach", Icons.Default.BeachAccess),
        Category("Waterfall", Icons.Default.Waves),
        Category("City", Icons.Default.LocationCity)
    )

    val featuredTrips = listOf(
        TripData("Bromo & Malang", "Malang, East Java", "01 Mei - 04 Mei", "Rp3.000.000"),
        TripData("Merbabu & Central...", "Magelang", "08 Mei - 10 Mei", "Rp1.800.000"),
        TripData("Rinjani & NTB", "Lombok", "01 Mei - 04 Mei", "Rp5.000.000"),
        TripData("Raja Ampat & Papua", "Sorong", "01 Mei - 04 Mei", "Rp5.000.000"),
        TripData("Banda Neira & Maluku", "Maluku Tengah", "01 Mei - 04 Mei", "Rp5.000.000"),
        TripData("Tanah Lot & Bali", "Tabanan", "01 Mei - 04 Mei", "Rp5.000.000")
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Icon(Icons.Default.Menu, contentDescription = null, modifier = Modifier.size(32.dp))
                    IconButton(onClick = { navController.navigate(Screen.Notifications.route) }) {
                        Icon(Icons.Outlined.Notifications, contentDescription = null, modifier = Modifier.size(32.dp))
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text("Let’s Choose\nYour Trip", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(20.dp))
                SearchBar()

                Spacer(modifier = Modifier.height(24.dp))
                Text("Categories", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        items(categories) { category -> CategoryChip(category) }

        item(span = { GridItemSpan(2) }) {
            Column {
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Available Now", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("See All", color = Color(0xFF3498DB), fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        items(featuredTrips) { trip -> 
            TripItemCard(trip, onClick = { navController.navigate("trip_detail/${trip.title}") }) 
        }
    }
}
