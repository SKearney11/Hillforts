package org.wit.hillfort.models.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore

class HillfortFireStore(val context: Context) : HillfortStore, AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

    suspend override fun findAll(): MutableList<HillfortModel> {
        return hillforts
    }

    suspend override fun findById(id: Long): HillfortModel? {
        val foundHillfort: HillfortModel? = hillforts.find { p -> p.id == id }
        return foundHillfort
    }

    suspend override fun create(hillfort: HillfortModel) {
        val key = db.child("users").child(userId).child("hillforts").push().key
        hillfort.fbId = key!!
        hillforts.add(hillfort)
        db.child("users").child(userId).child("hillforts").child(key).setValue(hillfort)
    }

    suspend override fun update(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.fbId == hillfort.fbId }
        if (foundHillfort != null) {
            foundHillfort.title = hillfort.title
            foundHillfort.description = hillfort.description
            foundHillfort.visitedCheckbox = hillfort.visitedCheckbox
            foundHillfort.additionalNotes = hillfort.additionalNotes
            foundHillfort.images = ArrayList(hillfort.images)
            foundHillfort.day = hillfort.day
            foundHillfort.month = hillfort.month
            foundHillfort.year = hillfort.year
            foundHillfort.location = hillfort.location
            foundHillfort.favorite = hillfort.favorite
            foundHillfort.rating = hillfort.rating
        }

        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(hillfort)
    }

    suspend override fun delete(hillfort: HillfortModel) {
        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).removeValue()
        hillforts.remove(hillfort)
    }

    override fun clear() {
        hillforts.clear()
    }

    fun fetchPlacemarks(placemarksReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(hillforts) { it.getValue<HillfortModel>(HillfortModel::class.java) }
                placemarksReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        hillforts.clear()
        db.child("users").child(userId).child("hillforts").addListenerForSingleValueEvent(valueEventListener)
    }
}