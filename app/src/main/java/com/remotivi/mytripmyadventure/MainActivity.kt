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
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.automirrored.filled.Help
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.remotivi.mytripmyadventure.ui.components.ReviewData
import com.remotivi.mytripmyadventure.ui.components.TripData
import com.remotivi.mytripmyadventure.ui.screens.*
import com.remotivi.mytripmyadventure.ui.theme.*
import com.remotivi.mytripmyadventure.viewmodel.CreateTripViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

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
    object Login : Screen("login", Icons.Default.Lock, "Login")
    object Register : Screen("register", Icons.Default.Person, "Register")
    
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
    object Review : Screen("review/{tripId}", Icons.Default.RateReview, "Review")
    object Notifications : Screen("notif", Icons.Default.Notifications, "Notifications")
    object Chat : Screen("chat", Icons.AutoMirrored.Filled.Message, "Chat")
    object DetailChat : Screen("detail_chat/{name}", Icons.AutoMirrored.Filled.Message, "Detail Chat")
    object Security : Screen("security", Icons.Default.Shield, "Security")
    object Matching : Screen("matching", Icons.Default.People, "Matching")
    object Planner : Screen("planner", Icons.AutoMirrored.Filled.EventNote, "Planner")
    object Budget : Screen("budget", Icons.Default.AccountBalanceWallet, "Budget")
    object EditPreferences : Screen("edit_preferences", Icons.Default.Settings, "Edit Preferences")
    object EditProfile : Screen("edit_profile", Icons.Default.Edit, "Edit Profile")
    object TripJoined : Screen("trip_joined", Icons.Default.WorkOutline, "Trip Joined")
    object TripCreated : Screen("trip_created", Icons.Default.Flag, "Trip Created")
    object Wishlist : Screen("wishlist", Icons.Default.FavoriteBorder, "Wishlist")
    object ETicket : Screen("e_ticket/{tripId}", Icons.Default.ConfirmationNumber, "E-Ticket")
    object ReviewSuccess : Screen("review_success", Icons.Default.CheckCircle, "Review Success")
    object MyReviews : Screen("my_reviews", Icons.Default.Star, "My Reviews")
    object ReviewDetail : Screen("review_detail/{reviewId}", Icons.Default.RateReview, "Review Detail")
    
    // Safety & Security Sub-screens
    object VerifiedAccount : Screen("verified_account", Icons.Default.Verified, "Verified Account")
    object EmergencyContact : Screen("emergency_contact", Icons.Default.Call, "Emergency Contact")
    object ReportCenter : Screen("report_center", Icons.Default.Flag, "Report Center")
    object Address : Screen("address", Icons.Default.LocationOn, "Address")
    object LiveLocation : Screen("live_location", Icons.Default.MyLocation, "Live Location")

    // Additional Features
    object Settings : Screen("settings", Icons.Default.Settings, "Settings")
    object HelpCenter : Screen("help_center", Icons.AutoMirrored.Filled.Help, "Help Center")
    object Voucher : Screen("voucher", Icons.Default.ConfirmationNumber, "Voucher")
    object PaymentSuccess : Screen("payment_success/{tripId}", Icons.Default.CheckCircle, "Payment Success")
    object VirtualAccount : Screen("virtual_account/{tripId}", Icons.Default.Payment, "Virtual Account")
    object ParticipantList : Screen("participants/{tripId}", Icons.Default.Groups, "Participants")
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Shared state for trips with drawable resources
    val allTrips = remember { mutableStateListOf<TripData>() }

    LaunchedEffect(Unit) {
        val database = FirebaseDatabase.getInstance()
        val tripsRef = database.getReference("trips")

        tripsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                android.util.Log.d("FIREBASE_TRIPS", "Snapshot exists: ${snapshot.exists()}, children count: ${snapshot.childrenCount}, value: ${snapshot.value}")
                if (snapshot.exists()) {
                    allTrips.clear()
                    for (child in snapshot.children) {
                        val trip = child.getValue(TripData::class.java)
                        if (trip != null) {
                            trip.id = child.key ?: ""
                            if (trip.availableSlots == 2) {
                                tripsRef.child(trip.id).child("availableSlots").setValue(7)
                                trip.availableSlots = 7
                            }
                            if (trip.title.contains("Rinjani") && !trip.isCompleted) {
                                // Kept commented out so testing Rinjani doesn't instantly complete it
                                // tripsRef.child(trip.id).child("completed").setValue(true)
                                // trip.isCompleted = true
                            }
                            if ((trip.title.contains("Bromo") || trip.title.contains("Merbabu")) && !trip.isJoined) {
                                tripsRef.child(trip.id).child("joined").setValue(true)
                                trip.isJoined = true
                            }
                            allTrips.add(trip)
                        }
                    }
                } else {
                    // Seed initial data if empty
                    val defaultTrips = listOf(
                        TripData(title = "Bromo & Malang", location = "Malang, East Java", date = "01 Mei - 04 Mei", price = "Rp3.000.000", category = "Mountain", imageName = "bromo"),
                        TripData(title = "Merbabu Hike", location = "Magelang", date = "08 Mei - 10 Mei", price = "Rp1.800.000", category = "Mountain", imageName = "merbabu"),
                        TripData(title = "Rinjani & NTB", location = "Lombok", date = "01 Mei - 04 Mei", price = "Rp5.000.000", category = "Mountain", imageName = "rinjani"),
                        TripData(title = "Raja Ampat & Papua", location = "Sorong", date = "15 Jun - 20 Jun", price = "Rp5.000.000", category = "Beach", imageName = "rajaampat"),
                        TripData(title = "Banda Neira", location = "Maluku Tengah", date = "05 Jul - 07 Jul", price = "Rp5.000.000", category = "City", imageName = "bandaneira"),
                        TripData(title = "Tanah Lot & Bali", location = "Tabanan", date = "12 Jul - 14 Jul", price = "Rp5.000.000", category = "Beach", imageName = "tanahlot")
                    )
                    defaultTrips.forEach { trip ->
                        val newRef = tripsRef.push()
                        trip.id = newRef.key ?: ""
                        newRef.setValue(trip)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) { }
        })
    }

    // Shared state for reviews
    val allReviews = remember { mutableStateListOf<ReviewData>() }
    
    LaunchedEffect(Unit) {
        val database = FirebaseDatabase.getInstance()
        val reviewsRef = database.getReference("reviews")
        reviewsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    allReviews.clear()
                    for (child in snapshot.children) {
                        val review = child.getValue(ReviewData::class.java)
                        if (review != null) {
                            review.id = child.key ?: ""
                            allReviews.add(review)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) { }
        })
    }
    
    val createTripViewModel: CreateTripViewModel = viewModel()

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
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) { LoginScreen(navController) }
            composable(Screen.Register.route) { RegisterScreen(navController) }

            composable(Screen.Home.route) { 
                HomeScreen(navController, allTrips) 
            }
            composable(Screen.MyTrips.route) { TripHistoryScreen(navController, allTrips) }
            composable(Screen.CreateTripIntro.route) { CreateTripIntroScreen(navController) }
            composable(Screen.Favorite.route) { 
                FavoriteScreen(navController, allTrips) 
            }
            composable(Screen.Profile.route) { ProfileScreen(navController) }
            
            composable(Screen.CreateTripStep1.route) { CreateTripStep1Screen(navController, createTripViewModel) }
            composable(Screen.CreateTripStep2.route) { CreateTripStep2Screen(navController, createTripViewModel) }
            composable(Screen.CreateTripStep3.route) { CreateTripStep3Screen(navController, createTripViewModel) }
            composable(Screen.CreateTripStep4.route) { CreateTripStep4Screen(navController, createTripViewModel) }
            
            composable(Screen.TripDetail.route) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                TripDetailScreen(tripId, navController, allTrips, allReviews)
            }
            composable(Screen.Payment.route) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                PaymentScreen(tripId, navController, allTrips)
            }
            composable(Screen.Review.route) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                ReviewScreen(tripId = tripId, navController = navController, onSaveReview = { review: ReviewData ->
                    val database = FirebaseDatabase.getInstance()
                    val newRef = database.getReference("reviews").push()
                    review.id = newRef.key ?: ""
                    newRef.setValue(review)
                    navController.navigate(Screen.ReviewSuccess.route)
                })
            }
            composable(Screen.ReviewSuccess.route) { ReviewSuccessScreen(navController) }
            composable(Screen.MyReviews.route) { MyReviewsScreen(navController, allReviews) }
            composable(Screen.ReviewDetail.route) { backStackEntry ->
                val reviewId = backStackEntry.arguments?.getString("reviewId") ?: ""
                com.remotivi.mytripmyadventure.ui.screens.ReviewDetailScreen(reviewId, navController, allReviews)
            }
            
            composable(Screen.Notifications.route) { NotificationScreen() }
            composable(Screen.Chat.route) { ChatScreen(navController) }
            composable(Screen.DetailChat.route) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name") ?: "Chat"
                DetailChatScreen(name, navController)
            }
            composable(Screen.Security.route) { SecurityScreen(navController) }
            composable(Screen.Matching.route) { MatchingScreen(navController) }
            composable(Screen.Planner.route) { PlannerScreen(navController) }
            composable(Screen.Budget.route) { BudgetScreen(navController) }
            composable(Screen.EditPreferences.route) { EditPreferencesScreen(navController) }
            composable(Screen.EditProfile.route) { EditProfileScreen(navController) }
            composable(Screen.TripJoined.route) { TripJoinedScreen(navController, allTrips) }
            composable(Screen.TripCreated.route) { TripCreatedScreen(navController, allTrips) }
            composable(Screen.Wishlist.route) { WishlistScreen(navController, allTrips) }
            composable(
                route = Screen.ETicket.route + "?method={method}",
                arguments = listOf(androidx.navigation.navArgument("method") { 
                    type = androidx.navigation.NavType.StringType
                    defaultValue = "Bank BCA" 
                })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                val method = backStackEntry.arguments?.getString("method") ?: "Bank BCA"
                ETicketScreen(tripId, method, navController, allTrips)
            }
            
            // Safety & Security Sub-screens
            composable(Screen.VerifiedAccount.route) { VerifiedAccountScreen(navController) }
            composable(Screen.EmergencyContact.route) { EmergencyContactScreen(navController) }
            composable(Screen.ReportCenter.route) { ReportCenterScreen(navController) }
            composable(Screen.Address.route) { AddressScreen(navController) }
            composable(Screen.LiveLocation.route) { LiveLocationScreen(navController) }

            // Additional Screens
            composable(Screen.Settings.route) { SettingsScreen(navController) }
            composable(Screen.HelpCenter.route) { HelpCenterScreen(navController) }
            composable(Screen.Voucher.route) { VoucherScreen(navController) }
            composable(
                route = Screen.PaymentSuccess.route + "?method={method}",
                arguments = listOf(androidx.navigation.navArgument("method") { 
                    type = androidx.navigation.NavType.StringType
                    defaultValue = "Bank BCA" 
                })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                val method = backStackEntry.arguments?.getString("method") ?: "Bank BCA"
                PaymentSuccessScreen(navController, tripId, method)
            }
            composable(
                route = Screen.VirtualAccount.route + "?method={method}",
                arguments = listOf(androidx.navigation.navArgument("method") { 
                    type = androidx.navigation.NavType.StringType
                    defaultValue = "Bank BCA" 
                })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                val method = backStackEntry.arguments?.getString("method") ?: "Bank BCA"
                com.remotivi.mytripmyadventure.ui.screens.VirtualAccountScreen(tripId, method, navController, allTrips)
            }
            composable(Screen.ParticipantList.route) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                ParticipantListScreen(navController, tripId)
            }
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
