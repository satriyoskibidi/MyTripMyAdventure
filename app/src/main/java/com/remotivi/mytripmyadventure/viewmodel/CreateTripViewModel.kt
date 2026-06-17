package com.remotivi.mytripmyadventure.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CreateTripViewModel : ViewModel() {
    // Step 1
    val tripName = mutableStateOf("")
    val location = mutableStateOf("")
    val destination = mutableStateOf("")
    val duration = mutableStateOf("")
    val imageUri = mutableStateOf<Uri?>(null)
    val maxCapacity = mutableStateOf("")
    val pricePerPerson = mutableStateOf("")

    // Step 2
    val meetingPoint = mutableStateOf("")
    val meetingTime = mutableStateOf("")
    val itineraryList = mutableStateListOf<Pair<String, String>>()

    // Step 3
    val selectedTransport = mutableStateListOf<String>()
    val selectedAcomodation = mutableStateOf("")
    val selectedMakan = mutableStateOf("")
    val selectedTiket = mutableStateOf("")
    val selectedLainnya = mutableStateListOf<String>()

    fun reset() {
        tripName.value = ""
        location.value = ""
        destination.value = ""
        duration.value = ""
        imageUri.value = null
        maxCapacity.value = ""
        pricePerPerson.value = ""
        
        meetingPoint.value = ""
        meetingTime.value = ""
        itineraryList.clear()
        
        selectedTransport.clear()
        selectedAcomodation.value = ""
        selectedMakan.value = ""
        selectedTiket.value = ""
        selectedLainnya.clear()
    }
}
