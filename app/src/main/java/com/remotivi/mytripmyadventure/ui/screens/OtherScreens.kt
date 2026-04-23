package com.remotivi.mytripmyadventure.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.remotivi.mytripmyadventure.Screen
import com.remotivi.mytripmyadventure.ui.components.SearchBar
import com.remotivi.mytripmyadventure.ui.components.TripData
import com.remotivi.mytripmyadventure.ui.components.TripItemCard
import com.remotivi.mytripmyadventure.ui.theme.DarkGreen
import com.remotivi.mytripmyadventure.ui.theme.LightGrey
import com.remotivi.mytripmyadventure.ui.theme.PriceOrange

@Composable
fun FavoriteScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Favorite Trips", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        // Mock favorites
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(listOf(
                TripData("Bromo & Malang", "Malang, East Java", "01 Mei - 04 Mei", "Rp3.000.000"),
                TripData("Rinjani Expedition", "Lombok", "01 Mei - 04 Mei", "Rp5.000.000")
            )) { trip ->
                TripItemCard(trip)
            }
        }
    }
}

@Composable
fun NotificationScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Notifications", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(3) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = LightGrey.copy(alpha = 0.3f))) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, null, tint = PriceOrange)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Update Perjalanan", fontWeight = FontWeight.Bold)
                            Text("Trip Bromo kamu akan dimulai besok!", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SecurityScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Keamanan & SOS", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        SecurityOption("Bagi Lokasi Langsung", "Update posisi kamu ke teman perjalanan", Icons.Default.MyLocation)
        SecurityOption("Kontak Darurat", "Panggil bantuan dalam satu klik", Icons.Default.Call)
        SecurityOption("Lapor Pengguna", "Jaga komunitas tetap aman", Icons.Default.Report)
    }
}

@Composable
fun SecurityOption(title: String, desc: String, icon: ImageVector) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = DarkGreen, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold)
                Text(desc, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailChatScreen(name: String, navController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(name, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 4.dp, color = Color.White) {
                Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = "", onValueChange = {},
                        placeholder = { Text("Ketik pesan...") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = {}, modifier = Modifier.background(DarkGreen, CircleShape)) {
                        Icon(Icons.AutoMirrored.Filled.Send, null, tint = Color.White)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp)) {
            ChatBubble("Halo, apakah masih ada slot?", false)
            ChatBubble("Halo! Masih sisa 2 slot kak.", true)
        }
    }
}

@Composable
fun ChatBubble(text: String, isMe: Boolean) {
    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart) {
        Surface(
            color = if (isMe) DarkGreen else LightGrey,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = if (isMe) 16.dp else 0.dp, bottomEnd = if (isMe) 0.dp else 16.dp)
        ) {
            Text(text, modifier = Modifier.padding(12.dp), color = if (isMe) Color.White else Color.Black)
        }
    }
}

@Composable
fun MatchingScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Smart Matching", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        // Mock matching list
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(listOf("Andi Pratama", "Siti Aminah")) { name ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(50.dp).background(Color.LightGray, CircleShape))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(name, fontWeight = FontWeight.Bold)
                            Text("98% Match", color = PriceOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlannerScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Trip Planner", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Rencanakan aktivitas harianmu di sini.", color = Color.Gray)
    }
}

@Composable
fun BudgetScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Budget Tracker", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = DarkGreen)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Total Spending", color = Color.White.copy(alpha = 0.7f))
                Text("Rp 4.500.000", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ChatScreen(navController: NavHostController) {
    val chats = listOf(
        ChatPreview("Bromo Squad", "Andi: OTW guys!", "10:30 AM", true),
        ChatPreview("Siti Aminah", "Ready for Bali trip?", "Yesterday", false)
    )
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Messages", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn {
            items(chats) { chat ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("detail_chat/${chat.name}") }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(54.dp).background(Color.LightGray, CircleShape))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(chat.name, fontWeight = FontWeight.Bold)
                        Text(chat.lastMessage, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                    }
                    Text(chat.time, fontSize = 11.sp, color = Color.Gray)
                }
                HorizontalDivider(color = LightGrey)
            }
        }
    }
}

data class ChatPreview(val name: String, val lastMessage: String, val time: String, val isGroup: Boolean)

@Composable
fun ProfileScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(100.dp).background(Color.LightGray, CircleShape))
        Spacer(modifier = Modifier.height(16.dp))
        Text("John Doe", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Verified, null, tint = Color.Blue, modifier = Modifier.size(16.dp))
            Text(" Elite Traveler (98)", color = Color.Gray, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(32.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            ProfileRowItem(Icons.Default.History, "My Trip History") { navController.navigate(Screen.MyTrips.route) }
            ProfileRowItem(Icons.Default.Settings, "Preferences")
            ProfileRowItem(Icons.Default.Shield, "Security & SOS") { navController.navigate(Screen.Security.route) }
            ProfileRowItem(Icons.AutoMirrored.Filled.Logout, "Logout", color = Color.Red)
        }
    }
}

@Composable
fun ProfileRowItem(icon: ImageVector, label: String, color: Color = Color.Black, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = if (color == Color.Red) color else DarkGreen)
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, fontSize = 16.sp, color = color)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
    }
}
