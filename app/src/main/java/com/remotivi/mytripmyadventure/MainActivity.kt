package com.remotivi.mytripmyadventure

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.remotivi.mytripmyadventure.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTripMyAdventureTheme {
                MainApp()
            }
        }
    }
}

// --- Navigation Definition ---
sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object Home : Screen("home", Icons.Default.Home, "Home")
    object Explore : Screen("explore", Icons.Default.Explore, "Explore")
    object Chat : Screen("chat", Icons.AutoMirrored.Filled.Message, "Chat")
    object DetailChat : Screen("detail_chat/{name}", Icons.AutoMirrored.Filled.Message, "Detail Chat")
    object Profile : Screen("profile", Icons.Default.Person, "Profile")
    object Matching : Screen("matching", Icons.Default.People, "Matching")
    object CreateTrip : Screen("create", Icons.Default.AddCircle, "Create")
    object Planner : Screen("planner", Icons.AutoMirrored.Filled.EventNote, "Planner")
    object Budget : Screen("budget", Icons.Default.AccountBalanceWallet, "Budget")
    object Security : Screen("security", Icons.Default.Shield, "Security")
    object Notifications : Screen("notif", Icons.Default.Notifications, "Notifications")
    object TripHistory : Screen("history", Icons.Default.History, "History")
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { AppBottomNavigation(navController) },
        containerColor = Color.White
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Explore.route) { ExploreScreen() }
            composable(Screen.Chat.route) { ChatScreen(navController) }
            composable(Screen.DetailChat.route) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name") ?: "Chat"
                DetailChatScreen(name, navController)
            }
            composable(Screen.Profile.route) { ProfileScreen(navController) }
            composable(Screen.Matching.route) { MatchingScreen() }
            composable(Screen.CreateTrip.route) { CreateTripScreen() }
            composable(Screen.Planner.route) { PlannerScreen() }
            composable(Screen.Budget.route) { BudgetScreen() }
            composable(Screen.Security.route) { SecurityScreen() }
            composable(Screen.Notifications.route) { NotificationScreen() }
            composable(Screen.TripHistory.route) { TripHistoryScreen() }
        }
    }
}

@Composable
fun AppBottomNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        color = DarkGreen,
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val navItems = listOf(Screen.Home, Screen.Explore, Screen.Chat, Screen.Profile)
            navItems.forEach { screen ->
                IconButton(onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }) {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.label,
                        tint = if (currentRoute?.startsWith(screen.route.split("/")[0]) == true) Color.White else Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

// --- HOME SCREEN ---
@Composable
fun HomeScreen(navController: NavHostController) {
    val categories = listOf(
        Category("Mountain", Icons.Default.Terrain, true),
        Category("Beach", Icons.Default.BeachAccess),
        Category("Waterfall", Icons.Default.Waves),
        Category("City", Icons.Default.LocationCity)
    )

    val featuredTrips = listOf(
        TripData("Bromo & Malang", "Malang, East Java", "Rp3.000.000"),
        TripData("Merbabu Hike", "Magelang", "Rp1.800.000"),
        TripData("Rinjani Expedition", "Lombok", "Rp5.000.000")
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
                Text("System Features", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    FeatureIcon(Icons.Default.People, "Match") { navController.navigate(Screen.Matching.route) }
                    FeatureIcon(Icons.Default.AddCircle, "Create") { navController.navigate(Screen.CreateTrip.route) }
                    FeatureIcon(Icons.AutoMirrored.Filled.EventNote, "Planner") { navController.navigate(Screen.Planner.route) }
                    FeatureIcon(Icons.Default.AccountBalanceWallet, "Budget") { navController.navigate(Screen.Budget.route) }
                }

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

        items(featuredTrips) { trip -> TripItemCard(trip) }
    }
}

// --- FITUR 5: Chat (List & Detail) ---
@Composable
fun ChatScreen(navController: NavHostController) {
    val chats = listOf(
        ChatPreview("Bromo Squad", "Andi: OTW guys!", "10:30 AM", true),
        ChatPreview("Siti Aminah", "Ready for Bali trip?", "Yesterday", false),
        ChatPreview("Bali Trip Coordination", "You: Shared a location", "Monday", true),
        ChatPreview("Budi Explorer", "Let's match again!", "12/05/2024", false)
    )
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Messages", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn {
            items(chats) { chat ->
                Row(modifier = Modifier
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
                    Column(horizontalAlignment = Alignment.End) {
                        Text(chat.time, fontSize = 11.sp, color = Color.Gray)
                        if (chat.isGroup) {
                            Icon(Icons.Default.Groups, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                        }
                    }
                }
                HorizontalDivider(color = LightGrey)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailChatScreen(name: String, navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(name, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(tonalElevation = 2.dp, color = Color.White) {
                Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = "", onValueChange = {},
                        placeholder = { Text("Type a message...") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = {}, modifier = Modifier.background(DarkGreen, CircleShape)) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.White)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp)) {
            ChatBubble("Hi, are we still going to Bromo?", true)
            ChatBubble("Yes! Everyone is ready.", false)
            ChatBubble("Great, see you tomorrow!", true)
        }
    }
}

@Composable
fun ChatBubble(message: String, isMe: Boolean) {
    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart) {
        Surface(
            color = if (isMe) DarkGreen else LightGrey,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = if (isMe) 16.dp else 0.dp, bottomEnd = if (isMe) 0.dp else 16.dp)
        ) {
            Text(message, modifier = Modifier.padding(12.dp), color = if (isMe) Color.White else Color.Black)
        }
    }
}

// --- FITUR: Riwayat & Trip Berjalan ---
@Composable
fun TripHistoryScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("On Going", "History")

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("My Trips", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        TabRow(selectedTabIndex = selectedTab, containerColor = Color.White, contentColor = DarkGreen, divider = {}) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTab == index, onClick = { selectedTab = index }) {
                    Text(title, modifier = Modifier.padding(16.dp), fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == 0) {
            TripItemCard(TripData("Bromo Adventure", "East Java", "Rp 3.000.000"), status = "Active")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(listOf(
                    TripData("Bali Beach Party", "Bali", "Rp 2.500.000"),
                    TripData("Lombok Trekking", "NTB", "Rp 4.000.000")
                )) { trip ->
                    TripItemCard(trip, status = "Completed")
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(100.dp).background(Color.LightGray, CircleShape))
        Spacer(modifier = Modifier.height(16.dp))
        Text("John Doe", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Verified, contentDescription = null, tint = Color.Blue, modifier = Modifier.size(16.dp))
            Text(" Elite Traveler (Trust Level: 98)", color = Color.Gray, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            ProfileRowItem(Icons.Default.History, "My Trip History & Status") { navController.navigate(Screen.TripHistory.route) }
            ProfileRowItem(Icons.Default.Settings, "Travel Style & Preferences")
            ProfileRowItem(Icons.Default.Shield, "Security & SOS") { navController.navigate(Screen.Security.route) }
            ProfileRowItem(Icons.Default.Star, "My Ratings & Reviews")
            ProfileRowItem(Icons.Default.Badge, "Achievement Badges")
            ProfileRowItem(Icons.AutoMirrored.Filled.Logout, "Logout", color = Color.Red)
        }
    }
}

// --- Rest of the screens ---
@Composable fun MatchingScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Smart Matching Buddy", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(1) { Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = LightGrey)) {
                Row(modifier = Modifier.padding(16.dp)) { Text("Ahmad Pratama - 98% Match") }
            } }
        }
    }
}

@Composable fun ExploreScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Explore Open Trip", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(3) { TripItemCard(TripData("Trip #$it", "Location", "Rp 2M")) }
        }
    }
}

@Composable fun CreateTripScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Create Open Trip", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Publish Trip") }
    }
}

@Composable fun PlannerScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Trip Planner", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        Text("04:00 - Sunrise View")
    }
}

@Composable fun BudgetScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Budget Tracker", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = DarkGreen)) {
            Text("Rp 4.500.000", color = Color.White, modifier = Modifier.padding(24.dp), fontSize = 32.sp)
        }
    }
}

@Composable fun SecurityScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Security Center", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        SecurityOptionCard("SOS", "Immediate alert", Icons.Default.Call)
    }
}

@Composable fun NotificationScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Notifications", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

// --- Common UI Components ---
@Composable fun SearchBar() {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        TextField(
            value = "", onValueChange = {}, placeholder = { Text("Search location...") },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
            modifier = Modifier.weight(1f).height(56.dp).clip(RoundedCornerShape(28.dp)),
            colors = TextFieldDefaults.colors(focusedContainerColor = LightGrey, unfocusedContainerColor = LightGrey, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent)
        )
    }
}

@Composable fun FeatureIcon(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Box(modifier = Modifier.size(54.dp).background(LightCream, CircleShape), contentAlignment = Alignment.Center) { Icon(icon, null, tint = PriceOrange) }
        Text(label, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable fun CategoryChip(category: Category) {
    Row(modifier = Modifier.fillMaxWidth().height(50.dp).background(if (category.isSelected) LightCream else LightGrey, RoundedCornerShape(25.dp)).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(category.icon, null, modifier = Modifier.size(20.dp), tint = if (category.isSelected) Color.Black else Color.Gray)
        Spacer(modifier = Modifier.width(8.dp))
        Text(category.name, fontSize = 14.sp, color = if (category.isSelected) PriceOrange else Color.Gray)
    }
}

@Composable fun TripItemCard(trip: TripData, status: String = "") {
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), modifier = Modifier.fillMaxWidth()) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFFBDC3C7))) {
                if (status.isNotEmpty()) {
                    Surface(modifier = Modifier.padding(8.dp).align(Alignment.TopStart), color = if (status == "Active") Color.Green else Color.Gray, shape = RoundedCornerShape(8.dp)) {
                        Text(status, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color.White, fontSize = 10.sp)
                    }
                }
            }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(trip.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(trip.price, color = PriceOrange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable fun ProfileRowItem(icon: ImageVector, label: String, color: Color = Color.Black, onClick: () -> Unit = {}) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = if (color == Color.Red) color else DarkGreen)
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, fontSize = 16.sp, color = color)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
    }
}

@Composable fun SecurityOptionCard(title: String, desc: String, icon: ImageVector) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = DarkGreen, modifier = Modifier.size(28.dp))
            Icon(icon, null, tint = DarkGreen, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column { Text(title, fontWeight = FontWeight.Bold); Text(desc, fontSize = 12.sp, color = Color.Gray) }
        }
    }
}

data class TripData(val title: String, val location: String, val price: String)
data class Category(val name: String, val icon: ImageVector, val isSelected: Boolean = false)
data class ChatPreview(val name: String, val lastMessage: String, val time: String, val isGroup: Boolean)
data class MatchingBuddy(val name: String, val style: String, val budget: String, val match: Int)

@Preview(showBackground = true)
@Composable
fun FullAppPreview() { MyTripMyAdventureTheme { MainApp() } }
