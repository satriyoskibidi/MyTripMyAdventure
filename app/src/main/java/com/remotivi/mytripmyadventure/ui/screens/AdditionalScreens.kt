package com.remotivi.mytripmyadventure.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
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
import com.remotivi.mytripmyadventure.ui.theme.DarkGreen
import com.remotivi.mytripmyadventure.ui.theme.LightGrey
import com.remotivi.mytripmyadventure.ui.theme.PriceOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pengaturan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(20.dp)) {
            SettingsItem("Akun & Keamanan", Icons.Default.Lock)
            SettingsItem("Notifikasi", Icons.Default.Notifications) { navController.navigate(Screen.Notifications.route) }
            SettingsItem("Bahasa", Icons.Default.Language)
            SettingsItem("Pusat Bantuan", Icons.AutoMirrored.Filled.Help) { navController.navigate(Screen.HelpCenter.route) }
            SettingsItem("Tentang Aplikasi", Icons.Default.Info)
            
            Spacer(modifier = Modifier.weight(1f))
            
            TextButton(
                onClick = { /* Logout */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Keluar Akun", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SettingsItem(title: String, icon: ImageVector, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = DarkGreen)
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
        Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
    }
    HorizontalDivider(color = LightGrey.copy(alpha = 0.5f))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpCenterScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pusat Bantuan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(20.dp)) {
            Text("Ada yang bisa kami bantu?", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Cari kendala kamu...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = RoundedCornerShape(12.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("FAQ Populer", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            
            val faqs = listOf(
                "Bagaimana cara membatalkan trip?",
                "Apakah bisa ganti jadwal?",
                "Metode pembayaran apa saja yang tersedia?",
                "Cara verifikasi akun?"
            )
            
            LazyColumn {
                items(faqs) { faq ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, LightGrey)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(faq, fontSize = 14.sp)
                            Icon(Icons.Default.Add, null, tint = DarkGreen)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Text("Butuh bantuan lebih lanjut?", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ContactCard("Chat Admin", Icons.AutoMirrored.Filled.Chat, Modifier.weight(1f))
                ContactCard("Email Kami", Icons.Default.Email, Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun ContactCard(title: String, icon: ImageVector, modifier: Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Color(0xFFD5E8D4))) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = DarkGreen)
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoucherScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Voucher Saya", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(20.dp)) {
            val vouchers = listOf(
                VoucherData("DISKON AWAL TAHUN", "Diskon 20% s/d Rp 200rb", "Berlaku hingga 31 Jan 2025"),
                VoucherData("PENGGUNA BARU", "Potongan Langsung Rp 50rb", "Berlaku hingga 28 Feb 2025"),
                VoucherData("CASHBACK GUNUNG", "Cashback 10% koin", "Khusus kategori Mountain")
            )
            
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(vouchers) { voucher ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, PriceOrange)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(50.dp).background(PriceOrange, CircleShape), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.ConfirmationNumber, null, tint = Color.White)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(voucher.code, fontWeight = FontWeight.Bold, color = PriceOrange)
                                Text(voucher.desc, fontSize = 14.sp)
                                Text(voucher.expiry, fontSize = 10.sp, color = Color.Gray)
                            }
                            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = PriceOrange), contentPadding = PaddingValues(horizontal = 8.dp), modifier = Modifier.height(30.dp)) {
                                Text("Pakai", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

data class VoucherData(val code: String, val desc: String, val expiry: String)

@Composable
fun PaymentSuccessScreen(navController: NavHostController, tripTitle: String, method: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(100.dp), tint = DarkGreen)
        Spacer(modifier = Modifier.height(24.dp))
        Text("Pembayaran Berhasil!", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Tiket untuk trip $tripTitle telah terbit.", textAlign = TextAlign.Center, color = Color.Gray)
        
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = { 
                val encodedMethod = android.net.Uri.encode(method)
                val encodedTripTitle = android.net.Uri.encode(tripTitle)
                navController.navigate("e_ticket/$encodedTripTitle?method=${encodedMethod}") { popUpTo("home") } 
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Lihat E-Ticket", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = { navController.navigate(Screen.MyTrips.route) { 
                popUpTo(navController.graph.startDestinationId) { inclusive = false } 
            } },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Ke Riwayat Perjalanan", color = DarkGreen)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveLocationScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Bagi Lokasi Langsung", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Box(modifier = Modifier.size(200.dp).background(LightGrey, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.MyLocation, null, modifier = Modifier.size(80.dp), tint = DarkGreen)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text("Fitur Berbagi Lokasi", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Teman perjalananmu dapat melihat posisimu secara real-time.", modifier = Modifier.padding(24.dp), textAlign = TextAlign.Center, color = Color.Gray)
            
            Button(
                onClick = { /* Start sharing */ },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Mulai Berbagi Lokasi", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParticipantListScreen(navController: NavHostController, tripTitle: String) {
    val participants = listOf("Elisa Tisya", "Andi Pratama", "Siti Aminah", "Budi Santoso", "Dewi Lestari")
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Peserta Trip", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(20.dp)) {
            Text(tripTitle, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
            Text("${participants.size} Peserta Bergabung", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(participants) { name ->
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, LightGrey)) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(40.dp).background(Color.LightGray, CircleShape))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(name, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                            IconButton(onClick = { navController.navigate("detail_chat/$name") }) {
                                Icon(Icons.AutoMirrored.Filled.Chat, null, tint = DarkGreen)
                            }
                        }
                    }
                }
            }
        }
    }
}
