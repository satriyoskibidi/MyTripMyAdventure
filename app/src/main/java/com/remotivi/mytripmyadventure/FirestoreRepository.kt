package com.remotivi.mytripmyadventure

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

data class TripFirestore(
    val id: String = "",
    val title: String = "",
    val destination: String = "",
    val date: String = "",
    val price: Int = 0,
    val imageUrl: String = "",
    val description: String = "",
    val createdBy: String = ""
)

class FirestoreRepository {
    private val db = Firebase.firestore
    private val tripsCollection = db.collection("trips")

    suspend fun getAllTrips(): List<TripFirestore> {
        return try {
            val result = tripsCollection.get().await()
            result.documents.mapNotNull { doc ->
                doc.toObject(TripFirestore::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addTrip(trip: TripFirestore) {
        tripsCollection.add(trip).await()
    }

    suspend fun getTripById(id: String): TripFirestore? {
        return try {
            val doc = tripsCollection.document(id).get().await()
            doc.toObject(TripFirestore::class.java)?.copy(id = doc.id)
        } catch (e: Exception) {
            null
        }
    }
}