package com.remotivi.mytripmyadventure.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.remotivi.mytripmyadventure.ui.theme.DarkGreen
import com.remotivi.mytripmyadventure.ui.theme.LightCream
import com.remotivi.mytripmyadventure.ui.theme.LightGrey
import com.remotivi.mytripmyadventure.ui.theme.PriceOrange

data class TripData(
    var id: String = "",
    var title: String = "",
    var location: String = "",
    var date: String = "",
    var price: String = "",
    var category: String = "Mountain",
    var imageRes: Int = 0,
    var imageName: String = "",
    var availableSlots: Int = 10,
    var maxSlots: Int = 10,
    var isJoined: Boolean = false,
    var paid: Boolean = false,
    var isCompleted: Boolean = false,
    var isFavorite: Boolean = false
)

data class ReviewData(
    var id: String = "",
    val tripTitle: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val date: String = "",
    val tags: List<String> = emptyList(),
    val imageUri: String? = null
)

data class Category(val name: String, val icon: ImageVector)

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search your location", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
            modifier = Modifier.weight(1f).height(56.dp).clip(RoundedCornerShape(28.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = LightGrey,
                unfocusedContainerColor = LightGrey,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(LightGrey)
                .clickable { onFilterClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Tune, null)
        }
    }
}

@Composable
fun TripItemCard(
    trip: TripData,
    status: String = "",
    isFavorite: Boolean = false,
    showFavoriteIcon: Boolean = true,
    onToggleFavorite: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                val context = androidx.compose.ui.platform.LocalContext.current
                if (trip.imageName.startsWith("data:image")) {
                    val decodedBitmap: android.graphics.Bitmap? = remember(trip.imageName) {
                        try {
                            val base64String = trip.imageName.substringAfter("base64,")
                            val decodedBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
                            android.graphics.BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                        } catch (e: Exception) { null }
                    }
                    if (decodedBitmap != null) {
                        Image(
                            bitmap = decodedBitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize().background(Color(0xFFBDC3C7)))
                    }
                } else {
                    val resolvedImageRes = if (trip.imageRes != 0) trip.imageRes 
                        else if (trip.imageName.isNotEmpty()) {
                            context.resources.getIdentifier(trip.imageName, "drawable", context.packageName)
                        } else 0

                    if (resolvedImageRes != 0) {
                        Image(
                            painter = painterResource(id = resolvedImageRes),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize().background(Color(0xFFBDC3C7)))
                    }
                }
                
                if (status.isNotEmpty()) {
                    Surface(
                        modifier = Modifier.padding(8.dp).align(Alignment.TopStart),
                        color = when(status) {
                            "Active", "Terkonfirmasi" -> Color(0xFF27AE60).copy(alpha = 0.8f)
                            "Menunggu Pembayaran" -> Color(0xFFF39C12).copy(alpha = 0.8f)
                            else -> Color.Gray.copy(alpha = 0.8f)
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            status,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                if (showFavoriteIcon) {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
                    ) {
                        Icon(
                            if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.White
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(trip.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                    Text(trip.location, fontSize = 10.sp, color = Color.Gray)
                }
                if (trip.date.isNotEmpty()) {
                    Text(trip.date, fontSize = 10.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(trip.price, color = PriceOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ConfirmationNumber, null, modifier = Modifier.size(12.dp), tint = DarkGreen)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sisa ${trip.availableSlots} slot", fontSize = 10.sp, color = DarkGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryChip(category: Category, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(if (isSelected) LightCream else LightGrey)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            category.icon,
            null,
            modifier = Modifier.size(20.dp),
            tint = if (isSelected) PriceOrange else Color.Gray
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            category.name,
            fontSize = 14.sp,
            color = if (isSelected) PriceOrange else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun FeatureIcon(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Box(modifier = Modifier.size(54.dp).background(LightCream, CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = PriceOrange)
        }
        Text(label, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
fun ReviewImageDisplay(imageUri: String, modifier: Modifier = Modifier) {
    if (imageUri.startsWith("data:image")) {
        val decodedBitmap: android.graphics.Bitmap? = androidx.compose.runtime.remember(imageUri) {
            try {
                val base64String = imageUri.substringAfter("base64,")
                val decodedBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
                android.graphics.BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } catch (e: Exception) { null }
        }
        if (decodedBitmap != null) {
            androidx.compose.foundation.Image(
                bitmap = decodedBitmap.asImageBitmap(),
                contentDescription = "Review Photo",
                modifier = modifier,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
        } else {
            Box(modifier = modifier.background(Color.LightGray, RoundedCornerShape(8.dp)))
        }
    } else if (imageUri.startsWith("http")) {
        coil.compose.AsyncImage(
            model = android.net.Uri.parse(imageUri),
            contentDescription = "Review Photo",
            modifier = modifier,
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
    } else {
        Box(modifier = modifier.background(Color.LightGray, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
            Text("Gambar tidak tersedia", color = Color.Gray, fontSize = 12.sp)
        }
    }
}
