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
import androidx.compose.ui.draw.clip
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FavoriteScreen(navController: NavHostController, allTrips: MutableList<TripData>) {
    val favoriteTrips = allTrips.filter { it.isFavorite }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
            Text("Favorite", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Icon(Icons.Default.Search, null)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (favoriteTrips.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.FavoriteBorder, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Belum ada trip favorit", color = Color.Gray)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxSize()) {
                items(favoriteTrips) { trip ->
                    TripItemCard(
                        trip = trip,
                        isFavorite = true,
                        onToggleFavorite = {
                            val index = allTrips.indexOfFirst { it.title == trip.title }
                            if (index != -1) {
                                allTrips[index] = allTrips[index].copy(isFavorite = false)
                            }
                        },
                        onClick = { navController.navigate("trip_detail/${trip.title}") }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun FilterChip(isSelected: Boolean, label: String, onClick: () -> Unit = {}) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        color = if (isSelected) Color(0xFFF5F5DC) else LightGrey.copy(alpha = 0.5f),
        shape = RoundedCornerShape(20.dp),
        border = if (isSelected) BorderStroke(1.dp, DarkGreen) else null
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
fun SecurityScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
            Text("Keamanan & SOS", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
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
                        modifier = Modifier.background(if (messageText.isNotBlank()) DarkGreen else Color.Gray, CircleShape),
                        enabled = messageText.isNotBlank()
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
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val uid = auth.currentUser?.uid ?: ""

    var nama by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    LaunchedEffect(uid) {
        if (uid.isNotEmpty()) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    nama = doc.getString("nama") ?: ""
                    username = doc.getString("username") ?: ""
                    bio = doc.getString("bio") ?: ""
                }
        }
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Profil Saya", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = DarkGreen)
            }
        }
        // Profile Header
        Card(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color.Black)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(80.dp).background(Color.LightGray, CircleShape), contentAlignment = Alignment.BottomEnd) {
                        Icon(
                            Icons.Default.Edit,
                            null,
                            modifier = Modifier.size(24.dp).background(DarkGreen, CircleShape).padding(4.dp).clickable { navController.navigate(Screen.EditProfile.route) },
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(if (nama.isEmpty()) "..." else nama, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(if (username.isEmpty()) "@..." else "@$username", color = Color.Gray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(if (bio.isEmpty()) "" else bio, fontSize = 12.sp)
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
            StatBox("12", "Trip Joined", Icons.Default.WorkOutline, Modifier.weight(1f)) { navController.navigate(Screen.TripJoined.route) }
            StatBox("5", "Trip Created", Icons.Default.Flag, Modifier.weight(1f)) { navController.navigate(Screen.TripCreated.route) }
            StatBox("8", "Wishlist", Icons.Default.FavoriteBorder, Modifier.weight(1f)) { navController.navigate(Screen.Wishlist.route) }
            StatBox("Medium", "Budget Level", Icons.Default.AccountBalanceWallet, Modifier.weight(1f)) { navController.navigate(Screen.Budget.route) }
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
                    Surface(
                        color = Color(0xFFFDF5E6),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable { navController.navigate(Screen.EditPreferences.route) }
                    ) {
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

        // Quick Access to Features
        Text("Travel Tools", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 20.dp))
        Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            FeatureIcon(Icons.Default.Groups, "Matching") { navController.navigate(Screen.Matching.route) }
            FeatureIcon(Icons.Default.EventNote, "Planner") { navController.navigate(Screen.Planner.route) }
            FeatureIcon(Icons.Default.AccountBalanceWallet, "Budget") { navController.navigate(Screen.Budget.route) }
            FeatureIcon(Icons.AutoMirrored.Filled.Message, "Chat") { navController.navigate(Screen.Chat.route) }
        }

        // Safety & Security
        Card(modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, LightGrey)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { navController.navigate(Screen.Security.route) }) {
                    Icon(Icons.Default.Shield, null, tint = DarkGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Safety & Security", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SecurityCircleItem(Icons.Default.Verified, "Verified Account", "Terverifikasi") {
                        navController.navigate(Screen.VerifiedAccount.route)
                    }
                    SecurityCircleItem(Icons.Default.LocationOn, "Alamat", "Lampung") {
                        navController.navigate(Screen.Address.route)
                    }
                    SecurityCircleItem(Icons.Default.Call, "Emergency", "2 Kontak") {
                        navController.navigate(Screen.EmergencyContact.route)
                    }
                    SecurityCircleItem(Icons.Default.Flag, "Report Center", "Laporkan User") {
                        navController.navigate(Screen.ReportCenter.route)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Logout Button
        val context = androidx.compose.ui.platform.LocalContext.current
        Button(
            onClick = {
                com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
                (context as? android.app.Activity)?.recreate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.Red),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
        ) {
            Text("Logout", color = androidx.compose.ui.graphics.Color.White)
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
fun StatBox(value: String, label: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit = {}) {
    Card(modifier = modifier.height(100.dp).clickable { onClick() }, colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, LightGrey)) {
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
fun SecurityCircleItem(icon: ImageVector, label: String, status: String, onClick: () -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Box(modifier = Modifier.size(40.dp).background(LightGrey.copy(alpha = 0.5f), CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = DarkGreen, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text(status, fontSize = 8.sp, color = Color.Gray)
    }
}

@Composable
fun FeatureIcon(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Box(modifier = Modifier.size(54.dp).background(Color(0xFFFDF5E6), CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = PriceOrange)
        }
        Text(label, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
fun MatchingScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
            Text("Smart Matching", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Text("Temukan teman perjalanan yang sehobi denganmu!", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(start = 12.dp))
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(listOf(
                MatchUser("Andi Pratama", "Lampung", 98, listOf("Hiking", "Photography")),
                MatchUser("Siti Aminah", "Jakarta", 92, listOf("Beach", "Culinary")),
                MatchUser("Budi Santoso", "Surabaya", 85, listOf("Mountain", "Camping"))
            )) { user ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, LightGrey)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(60.dp).background(Color.LightGray, CircleShape))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(user.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(user.location, fontSize = 12.sp, color = Color.Gray)
                            Row(modifier = Modifier.padding(top = 4.dp)) {
                                user.tags.forEach { tag ->
                                    Surface(color = Color(0xFFD5E8D4), shape = RoundedCornerShape(4.dp), modifier = Modifier.padding(end = 4.dp)) {
                                        Text(tag, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp), fontSize = 8.sp)
                                    }
                                }
                            }
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${user.match}%", color = PriceOrange, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("Match", fontSize = 10.sp, color = PriceOrange)
                            Button(
                                onClick = { navController.navigate("detail_chat/${user.name}") },
                                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                                contentPadding = PaddingValues(horizontal = 12.dp),
                                modifier = Modifier.height(30.dp).padding(top = 4.dp)
                            ) {
                                Text("Chat", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

data class MatchUser(val name: String, val location: String, val match: Int, val tags: List<String>)

@Composable
fun PlannerScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
            Text("Trip Planner", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = DarkGreen)) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Event, null, tint = Color.White, modifier = Modifier.size(40.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Bromo Expedition", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Next: 12 Mei 2024", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Daily Schedule", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(listOf(
                PlanItem("08:00", "Meeting Point", "Stasiun Malang Kota Baru"),
                PlanItem("10:00", "Perjalanan", "Menuju penginapan di Bromo"),
                PlanItem("13:00", "Makan Siang", "Warung lokal Bu Endang"),
                PlanItem("15:00", "Check-in", "Homestay Desa Ngadisari"),
                PlanItem("19:00", "Briefing", "Persiapan sunrise tour")
            )) { item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(item.time, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.width(50.dp))
                    Box(modifier = Modifier.size(8.dp).background(DarkGreen, CircleShape))
                    Spacer(modifier = Modifier.width(16.dp))
                    Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = LightGrey.copy(alpha = 0.2f))) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(item.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(item.desc, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        FloatingActionButton(
            onClick = { /* Add plan */ },
            containerColor = DarkGreen,
            contentColor = Color.White,
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(Icons.Default.Add, null)
        }
    }
}

data class PlanItem(val time: String, val title: String, val desc: String)

@Composable
fun BudgetScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp).verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
            Text("Budget Tracker", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(20.dp))

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF2C3E50))) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Total Spending", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                Text("Rp 4.500.000", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(progress = { 0.65f }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape), color = PriceOrange, trackColor = Color.White.copy(alpha = 0.2f))
                Text("65% of your Rp 7.000.000 limit", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Categories", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        BudgetCategoryItem(Icons.Default.Hotel, "Accommodation", "Rp 2.000.000", Color(0xFF3498DB))
        BudgetCategoryItem(Icons.Default.DirectionsBus, "Transport", "Rp 1.200.000", Color(0xFFE67E22))
        BudgetCategoryItem(Icons.Default.Restaurant, "Food & Drinks", "Rp 800.000", Color(0xFF27AE60))
        BudgetCategoryItem(Icons.Default.ConfirmationNumber, "Activities", "Rp 500.000", Color(0xFF9B59B6))

        Spacer(modifier = Modifier.height(24.dp))
        Text("Recent Transactions", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        TransactionItem("Hotel Bromo Permai", "Yesterday", "- Rp 1.500.000")
        TransactionItem("Nasi Goreng Pak Jo", "Yesterday", "- Rp 35.000")
        TransactionItem("Bensin Mobil", "2 days ago", "- Rp 200.000")

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun BudgetCategoryItem(icon: ImageVector, label: String, amount: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(40.dp).background(color.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, modifier = Modifier.weight(1f), fontSize = 14.sp)
        Text(amount, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun TransactionItem(title: String, date: String, amount: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text(title, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(date, fontSize = 12.sp, color = Color.Gray)
        }
        Text(amount, color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPreferencesScreen(navController: NavHostController) {
    var favDestinations by remember { mutableStateOf("Bali, Bromo, Rinjani") }
    var budget by remember { mutableStateOf("Low - Mid") }
    var style by remember { mutableStateOf("Adventurer") }
    var availableTime by remember { mutableStateOf("Weekend Only") }

    val isFormValid = favDestinations.isNotBlank() && availableTime.isNotBlank()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Preferences", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
            Text("Destinasi Favorit *", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = favDestinations,
                onValueChange = { favDestinations = it },
                modifier = Modifier.fillMaxWidth(),
                isError = favDestinations.isBlank(),
                supportingText = { if (favDestinations.isBlank()) Text("Wajib diisi", color = Color.Red) }
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text("Budget Preference", fontWeight = FontWeight.Bold)
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Backpacker", "Low - Mid", "Luxury").forEach { item ->
                    FilterChip(budget == item, item, onClick = { budget = item })
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Traveling Style", fontWeight = FontWeight.Bold)
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Adventurer", "Relaxing", "Cultural").forEach { item ->
                    FilterChip(style == item, item, onClick = { style = item })
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Waktu Tersedia *", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = availableTime,
                onValueChange = { availableTime = it },
                modifier = Modifier.fillMaxWidth(),
                isError = availableTime.isBlank(),
                supportingText = { if (availableTime.isBlank()) Text("Wajib diisi", color = Color.Red) }
            )

            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { if (isFormValid) navController.popBackStack() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isFormValid) DarkGreen else Color.Gray),
                shape = RoundedCornerShape(28.dp),
                enabled = isFormValid
            ) {
                Text("Simpan Perubahan", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val uid = auth.currentUser?.uid ?: ""

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(uid) {
        if (uid.isNotEmpty()) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    name = doc.getString("nama") ?: ""
                    username = doc.getString("username") ?: ""
                    bio = doc.getString("bio") ?: ""
                }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Profil", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.size(100.dp).background(Color.LightGray, CircleShape), contentAlignment = Alignment.BottomEnd) {
                    Icon(Icons.Default.CameraAlt, null, modifier = Modifier.size(32.dp).background(DarkGreen, CircleShape).padding(6.dp), tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text("Nama Lengkap", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = name, 
                onValueChange = { name = it }, 
                modifier = Modifier.fillMaxWidth(), 
                shape = RoundedCornerShape(12.dp),
                enabled = !isSaving
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Username", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = username, 
                onValueChange = { username = it }, 
                modifier = Modifier.fillMaxWidth(), 
                shape = RoundedCornerShape(12.dp), 
                prefix = { Text("@") },
                enabled = !isSaving
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Bio", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = bio, 
                onValueChange = { bio = it }, 
                modifier = Modifier.fillMaxWidth().height(100.dp), 
                shape = RoundedCornerShape(12.dp),
                enabled = !isSaving
            )

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    if (uid.isNotEmpty()) {
                        isSaving = true
                        db.collection("users").document(uid).update(
                            mapOf(
                                "nama" to name,
                                "username" to username,
                                "bio" to bio
                            )
                        ).addOnSuccessListener {
                            isSaving = false
                            navController.popBackStack()
                        }.addOnFailureListener {
                            isSaving = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(28.dp),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Simpan", fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TripJoinedScreen(navController: NavHostController, allTrips: List<TripData>) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
            Text("Trip Diikuti", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(allTrips.take(2)) { trip ->
                TripItemCard(
                    trip = trip,
                    onClick = { navController.navigate("e_ticket/${trip.title}") }
                )
            }
        }
    }
}

@Composable
fun TripCreatedScreen(navController: NavHostController, allTrips: List<TripData>) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
            Text("Trip Dibuat", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(allTrips.drop(2).take(1)) { trip ->
                TripItemCard(
                    trip = trip,
                    onClick = { navController.navigate("trip_detail/${trip.title}") }
                )
            }
        }
    }
}

@Composable
fun WishlistScreen(navController: NavHostController, allTrips: List<TripData>) {
    val wishlist = allTrips.filter { it.isFavorite }
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
            Text("Wishlist", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (wishlist.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Belum ada wishlist", color = Color.Gray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(wishlist) { trip ->
                    TripItemCard(
                        trip = trip,
                        isFavorite = true,
                        onClick = { navController.navigate("trip_detail/${trip.title}") }
                    )
                }
            }
        }
    }
}

@Composable
fun VerifiedAccountScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
            Text("Verified Account", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(40.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Verified, null, modifier = Modifier.size(100.dp), tint = DarkGreen)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("Akun Anda Terverifikasi!", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text("Identitas Anda telah kami konfirmasi untuk keamanan komunitas.", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color.Gray)

        Spacer(modifier = Modifier.height(40.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFD5E8D4))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Keuntungan Verifikasi:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Icon(Icons.Default.Check, null, tint = DarkGreen, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Badge centang biru di profil.", fontSize = 14.sp)
                }
                Row {
                    Icon(Icons.Default.Check, null, tint = DarkGreen, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Prioritas dalam pencarian matching.", fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun EmergencyContactScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
            Text("Kontak Darurat", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("Siapa yang harus kami hubungi dalam keadaan darurat?", color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))

        EmergencyItem("Ayah", "0812-3456-7890")
        EmergencyItem("Ibu", "0812-9876-5432")

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { /* Add new */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Tambah Kontak Baru", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EmergencyItem(rel: String, phone: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), border = BorderStroke(1.dp, LightGrey)) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(rel, fontWeight = FontWeight.Bold)
                Text(phone, color = Color.Gray)
            }
            Icon(Icons.Default.Call, null, tint = DarkGreen)
        }
    }
}

@Composable
fun ReportCenterScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
            Text("Report Center", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("Laporkan masalah atau pengguna lain demi keamanan bersama.", color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Ceritakan apa yang terjadi...") },
            modifier = Modifier.fillMaxWidth().height(150.dp),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Kirim Laporan", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AddressScreen(navController: NavHostController) {
    var address by remember { mutableStateOf("Jl. Merdeka No. 123, Bandar Lampung") }
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
            Text("Alamat Saya", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("Alamat Utama", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = DarkGreen) }
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Simpan Alamat", fontWeight = FontWeight.Bold)
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
            Text("Messages", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
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