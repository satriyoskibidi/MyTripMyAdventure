package com.remotivi.mytripmyadventure.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
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
import com.remotivi.mytripmyadventure.ui.components.*

@Composable
fun HomeScreen(navController: NavHostController, allTrips: SnapshotStateList<TripData>) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var showCategories by remember { mutableStateOf(false) }
    
    val categories = listOf(
        Category("Mountain", Icons.Default.Terrain),
        Category("Beach", Icons.Default.BeachAccess),
        Category("Waterfall", Icons.Default.Waves),
        Category("City", Icons.Default.LocationCity)
    )

    // Logika Filter: Tampilkan SEMUA trip jika panel kategori sedang ditutup
    val filteredTrips = allTrips.filter { trip ->
        val matchesCategory = if (showCategories && selectedCategory != null) {
            trip.category == selectedCategory
        } else {
            true // Tampilkan semua trip
        }
        val matchesSearch = trip.title.contains(searchQuery, ignoreCase = true) || 
                          trip.location.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

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
                
                // SearchBar dengan aksi klik Filter
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onFilterClick = { 
                        showCategories = !showCategories
                        // Jika dimatikan, reset kategori agar menampilkan semua trip kembali
                        if (!showCategories) selectedCategory = null 
                    }
                )

                if (showCategories) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Categories", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Tampilkan Categories hanya jika tombol filter diklik
        if (showCategories) {
            items(categories) { category -> 
                CategoryChip(
                    category = category,
                    isSelected = selectedCategory == category.name,
                    onClick = { 
                        // Toggle kategori: jika klik yang sama, maka unselect
                        selectedCategory = if (selectedCategory == category.name) null else category.name 
                    }
                ) 
            }
        }

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

        if (filteredTrips.isEmpty()) {
            item(span = { GridItemSpan(2) }) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    Text("No trips found", color = Color.Gray)
                }
            }
        } else {
            items(filteredTrips) { trip -> 
                TripItemCard(
                    trip = trip,
                    isFavorite = trip.isFavorite,
                    onToggleFavorite = {
                        val index = allTrips.indexOf(trip)
                        if (index != -1) {
                            allTrips[index] = allTrips[index].copy(isFavorite = !allTrips[index].isFavorite)
                        }
                    },
                    onClick = { navController.navigate("trip_detail/${trip.title}") }
                ) 
            }
        }
        
        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
