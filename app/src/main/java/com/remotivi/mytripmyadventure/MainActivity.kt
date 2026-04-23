package com.remotivi.mytripmyadventure

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.remotivi.mytripmyadventure.ui.screens.*
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

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object Home : Screen("home", Icons.Default.Home, "Home")
    object MyTrips : Screen("my_trips", Icons.Default.ConfirmationNumber, "My Trips")
    object CreateTripIntro : Screen("create_intro", Icons.Default.Add, "Create")
    object Favorite : Screen("favorite", Icons.Default.FavoriteBorder, "Favorite")
    object Profile : Screen("profile", Icons.Default.PersonOutline, "Profile")
    
    // Sub-screens for Creation Flow
    object CreateTripStep1 : Screen("create_step1", Icons.Default.Add, "Step 1")
    object CreateTripStep2 : Screen("create_step2", Icons.Default.Add, "Step 2")
    object CreateTripStep3 : Screen("create_step3", Icons.Default.Add, "Step 3")
    object CreateTripStep4 : Screen("create_step4", Icons.Default.Add, "Step 4")
    
    // Other screens
    object TripDetail : Screen("trip_detail/{tripId}", Icons.Default.Info, "Detail")
    object Payment : Screen("payment/{tripId}", Icons.Default.Payment, "Payment")
    object Review : Screen("review", Icons.Default.RateReview, "Review")
    object Notifications : Screen("notif", Icons.Default.Notifications, "Notifications")
    object Chat : Screen("chat", Icons.AutoMirrored.Filled.Message, "Chat")
    object DetailChat : Screen("detail_chat/{name}", Icons.AutoMirrored.Filled.Message, "Detail Chat")
    object Security : Screen("security", Icons.Default.Shield, "Security")
    object Matching : Screen("matching", Icons.Default.People, "Matching")
    object Planner : Screen("planner", Icons.Default.EventNote, "Planner")
    object Budget : Screen("budget", Icons.Default.AccountBalanceWallet, "Budget")
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            val bottomBarScreens = listOf(Screen.Home.route, Screen.MyTrips.route, Screen.CreateTripIntro.route, Screen.Favorite.route, Screen.Profile.route)
            if (currentRoute in bottomBarScreens) {
                AppBottomNavigation(navController)
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.MyTrips.route) { TripHistoryScreen(navController) }
            composable(Screen.CreateTripIntro.route) { CreateTripIntroScreen(navController) }
            composable(Screen.Favorite.route) { FavoriteScreen() }
            composable(Screen.Profile.route) { ProfileScreen(navController) }
            
            composable(Screen.CreateTripStep1.route) { CreateTripStep1Screen(navController) }
            composable(Screen.CreateTripStep2.route) { CreateTripStep2Screen(navController) }
            composable(Screen.CreateTripStep3.route) { CreateTripStep3Screen(navController) }
            composable(Screen.CreateTripStep4.route) { CreateTripStep4Screen(navController) }
            
            composable(Screen.TripDetail.route) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                TripDetailScreen(tripId, navController)
            }
            composable(Screen.Payment.route) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                PaymentScreen(tripId, navController)
            }
            composable(Screen.Review.route) { ReviewScreen(navController) }
            
            composable(Screen.Notifications.route) { NotificationScreen() }
            composable(Screen.Chat.route) { ChatScreen(navController) }
            composable(Screen.DetailChat.route) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name") ?: "Chat"
                DetailChatScreen(name, navController)
            }
            composable(Screen.Security.route) { SecurityScreen() }
            composable(Screen.Matching.route) { MatchingScreen() }
            composable(Screen.Planner.route) { PlannerScreen() }
            composable(Screen.Budget.route) { BudgetScreen() }
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
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationIcon(Screen.Home, currentRoute, navController)
            NavigationIcon(Screen.MyTrips, currentRoute, navController)
            
            // Central Plus Button
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.White, CircleShape)
                    .clickable { 
                        if (currentRoute != Screen.CreateTripIntro.route) {
                            navController.navigate(Screen.CreateTripIntro.route)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, null, tint = DarkGreen, modifier = Modifier.size(32.dp))
            }
            
            NavigationIcon(Screen.Favorite, currentRoute, navController)
            NavigationIcon(Screen.Profile, currentRoute, navController)
        }
    }
}

@Composable
fun NavigationIcon(screen: Screen, currentRoute: String?, navController: NavHostController) {
    IconButton(onClick = {
        if (currentRoute != screen.route) {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }) {
        Icon(
            imageVector = screen.icon,
            contentDescription = screen.label,
            tint = if (currentRoute == screen.route) Color.White else Color.White.copy(alpha = 0.5f),
            modifier = Modifier.size(28.dp)
        )
    }
}
