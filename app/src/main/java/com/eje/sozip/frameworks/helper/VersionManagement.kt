package com.eje.sozip.frameworks.helper

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class VersionManagement {
    private val db = Firebase.firestore

    companion object{
        var version : String? = null
    }

    fun getVersion(completion: (Boolean) -> Unit){
        val versionRef = db.collection("Version").document("Android")

        versionRef.get().addOnCompleteListener {
            if(it.isSuccessful){
                version = it.result.get("latest") as? String
                completion(true)
            } else{
                completion(false)
            }
        }.addOnFailureListener {
            completion(false)
        }
    }
}