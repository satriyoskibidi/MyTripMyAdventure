package com.remotivi.mytripmyadventure.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            Text("Favorite", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Icon(Icons.Default.Search, null)
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(true, "All")
            FilterChip(false, "Sumatera")
            FilterChip(false, "Jawa")
            FilterChip(false, "Kalimantan")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(listOf(
                TripData("Bromo & Malang Trip", "Malang, Jawa Timur", "01 Mei - 04 Mei", "Rp3.000.000"),
                TripData("Tanah Lot & Bali", "Tabanan, Bali", "01 Mei - 04 Mei", "Rp3.000.000")
            )) { trip ->
                TripItemCard(trip)
            }
        }
    }
}

@Composable
fun FilterChip(isSelected: Boolean, label: String) {
    Surface(
        color = if (isSelected) Color(0xFFF5F5DC) else LightGrey.copy(alpha = 0.5f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(label, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
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
    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf(
        "Halo, apakah masih ada slot?" to false,
        "Halo! Masih sisa 2 slot kak." to true
    ) }

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
                        value = messageText, 
                        onValueChange = { messageText = it },
                        placeholder = { Text("Ketik pesan...") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { 
                            if (messageText.isNotBlank()) {
                                messages.add(messageText to true)
                                messageText = ""
                            }
                        }, 
                        modifier = Modifier.background(DarkGreen, CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, null, tint = Color.White)
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp)) {
            items(messages) { (text, isMe) ->
                ChatBubble(text, isMe)
            }
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
fun ProfileScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(40.dp))
        // Profile Header
        Card(modifier = Modifier.padding(20.dp).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color.Black)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(80.dp).background(Color.LightGray, CircleShape), contentAlignment = Alignment.BottomEnd) {
                        Icon(Icons.Default.Edit, null, modifier = Modifier.size(24.dp).background(DarkGreen, CircleShape).padding(4.dp), tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Elisa Tisya Nugraha", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("@satisyaa", color = Color.Gray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Life is short, travel more!", fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    ProfileStatItem(Icons.Default.Star, "5.0", "(100 Reviews)")
                    ProfileStatItem(Icons.Default.VerifiedUser, "Trusted Traveler", "Level 3")
                    ProfileStatItem(Icons.Default.Groups, "87,5%", "Match Rate")
                }
            }
        }
        
        // Stats Row
        Row(modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatBox("12", "Trip Joined", Icons.Default.WorkOutline, Modifier.weight(1f))
            StatBox("5", "Trip Created", Icons.Default.Flag, Modifier.weight(1f))
            StatBox("8", "Wishlist", Icons.Default.FavoriteBorder, Modifier.weight(1f))
            StatBox("Medium", "Budget Level", Icons.Default.AccountBalanceWallet, Modifier.weight(1f))
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Travel Preference
        Card(modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = DarkGreen)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("My Travel Preference", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Surface(color = Color(0xFFFDF5E6), shape = RoundedCornerShape(12.dp)) {
                        Text("Edit Preferences >", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    PreferenceItem(Icons.Default.LocationOn, "Destinasi Favorit", "Bali, Bromo, Rinjani", Modifier.weight(1f))
                    PreferenceItem(Icons.Default.AccountBalanceWallet, "Budget", "Low - Mid", Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    PreferenceItem(Icons.Default.Backpack, "Gaya Traveling", "Adventurer", Modifier.weight(1f))
                    PreferenceItem(Icons.Default.CalendarMonth, "Waktu Tersedia", "Weekend", Modifier.weight(1f))
                }
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Safety & Security
        Card(modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, LightGrey)) {
             Column(modifier = Modifier.padding(16.dp)) {
                 Row(verticalAlignment = Alignment.CenterVertically) {
                     Icon(Icons.Default.Shield, null, tint = DarkGreen)
                     Spacer(modifier = Modifier.width(8.dp))
                     Text("Safety & Security", fontWeight = FontWeight.Bold)
                 }
                 Spacer(modifier = Modifier.height(16.dp))
                 Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                     SecurityCircleItem(Icons.Default.Verified, "Verified Account", "Terverifikasi")
                     SecurityCircleItem(Icons.Default.LocationOn, "Alamat", "Lampung")
                     SecurityCircleItem(Icons.Default.Call, "Emergency", "2 Kontak")
                     SecurityCircleItem(Icons.Default.Flag, "Report Center", "Laporkan User")
                 }
             }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Upcoming Trip
        Card(modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, LightGrey)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Row {
                        Icon(Icons.Default.WorkOutline, null, tint = DarkGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Upcoming Trip", fontWeight = FontWeight.Bold)
                    }
                    Text("Lihat Semua >", fontSize = 12.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth().background(LightGrey.copy(alpha = 0.3f), RoundedCornerShape(12.dp)).padding(12.dp)) {
                    Box(modifier = Modifier.size(60.dp).background(Color.Gray, RoundedCornerShape(8.dp)))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Bromo & Malang Trip", fontWeight = FontWeight.Bold)
                        Text("Malang, Jawa Timur", fontSize = 12.sp, color = Color.Gray)
                        Text("01 Mei - 04 Mei", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun ProfileStatItem(icon: ImageVector, title: String, subtitle: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = if (icon == Icons.Default.Star) Color(0xFFF1C40F) else DarkGreen, modifier = Modifier.size(16.dp))
            Text(" $title", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Text(subtitle, fontSize = 10.sp, color = Color.Gray)
    }
}

@Composable
fun StatBox(value: String, label: String, icon: ImageVector, modifier: Modifier) {
    Card(modifier = modifier.height(100.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, LightGrey)) {
        Column(modifier = Modifier.fillMaxSize().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(icon, null, tint = Color.LightGray)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(label, fontSize = 10.sp, color = Color.Gray, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun PreferenceItem(icon: ImageVector, label: String, value: String, modifier: Modifier) {
    Row(modifier = modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
            Text(value, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SecurityCircleItem(icon: ImageVector, label: String, status: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(40.dp).background(LightGrey.copy(alpha = 0.5f), CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = DarkGreen, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text(status, fontSize = 8.sp, color = Color.Gray)
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
