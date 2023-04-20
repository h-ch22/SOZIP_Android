package com.eje.sozip.SOZIP.helper

import androidx.compose.runtime.mutableStateListOf
import com.eje.sozip.SOZIP.models.SOZIPDataModel
import com.eje.sozip.SOZIP.models.SOZIPPackagingTypeModel
import com.eje.sozip.frameworks.helper.AES256Util
import com.eje.sozip.ui.theme.SOZIP_BG_1
import com.eje.sozip.ui.theme.SOZIP_BG_2
import com.eje.sozip.ui.theme.SOZIP_BG_3
import com.eje.sozip.ui.theme.SOZIP_BG_4
import com.eje.sozip.ui.theme.SOZIP_BG_5
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date

class SOZIPHelper {
    private val db = Firebase.firestore
    companion object{
        var SOZIPList = mutableStateListOf<SOZIPDataModel>()
        var categoryList = mutableStateListOf<String>()
    }

    fun getSOZIP(completion : (Boolean) -> Unit){
        SOZIPList.clear()
        categoryList.clear()

        val collectionRef = db.collection("SOZIP")

        collectionRef.addSnapshotListener { documents, error ->
            if(error != null){
                error.printStackTrace()
                completion(false)
            } else{
                documents?.documentChanges?.forEach { diff ->
                    when(diff.type){
                        DocumentChange.Type.ADDED,
                            DocumentChange.Type.MODIFIED -> {
                            val address = diff.document.get("address") as? String ?: ""
                            val date = diff.document.get("dateTime") as com.google.firebase.Timestamp
                            val description = diff.document.get("description") as? String ?: ""
                            val location = diff.document.get("location") as? String ?: ""
                            val name = diff.document.get("name") as? String ?: ""
                            val category = diff.document.get("category") as? String ?: ""
                            val firstCome = diff.document.get("firstCome") as? Int ?: 4
                            val currentPeople = diff.document.get("currentPeople") as? Long ?: 0
                            val docId = diff.document.id
                            val participants = diff.document.get("participants") as? Map<String, String> ?: mapOf()
                            val Manager = diff.document.get("Manager") as? String ?: ""
                            val status = diff.document.get("status") as? String ?: ""

                            val dateFormat = SimpleDateFormat("yyyy. MM. dd. HH:mm:ss")
                            val dateAsDate = date.toDate()
                            val dateAsString = dateFormat.format(dateAsDate)
                            val dateTime = dateFormat.parse(dateAsString)
                            val color = diff.document.get("color") as? String ?: "bg_1"
                            val account = diff.document.get("account") as? String ?: ""
                            val profile = diff.document.get("profiles") as? Map<String, String> ?: mapOf()
                            var colorCode = SOZIP_BG_1
                            val url = diff.document.get("url") as? String ?: ""
                            val type = diff.document.get("type") as? String ?: "DELIVERY"

                            when(color){
                                "bg_1" -> colorCode = SOZIP_BG_1
                                "bg_2" -> colorCode = SOZIP_BG_2
                                "bg_3" -> colorCode = SOZIP_BG_3
                                "bg_4" -> colorCode = SOZIP_BG_4
                                "bg_5" -> colorCode = SOZIP_BG_5
                                else -> colorCode = SOZIP_BG_1
                            }

                            if(status == "" && !categoryList.contains(category)){
                                categoryList.add(category)
                            }

                            val data =
                                SOZIPDataModel(
                                docId,
                                category,
                                firstCome + 1,
                                AES256Util.decrypt(name),
                                currentPeople.toInt() + 1,
                                AES256Util.decrypt(description),
                                dateTime,
                                Manager,
                                participants,
                                AES256Util.decrypt(location),
                                AES256Util.decrypt(address),
                                status,
                                colorCode,
                                account,
                                profile,
                                url,
                                if(type == "DELIVERY") SOZIPPackagingTypeModel.DELIVERY else SOZIPPackagingTypeModel.TAKE_OUT
                            )

                            if(SOZIPList.filter {
                                    it.docID == docId
                                }.size < 1){
                                SOZIPList.add(data)
                            } else {
                                val index = SOZIPList.filter {
                                    it.docID == docId
                                }.lastIndex

                                SOZIPList[index] = data
                            }
                        }

                        DocumentChange.Type.REMOVED -> {
                            val docId = diff.document.id

                            val index = SOZIPList.filter {
                                it.docID == docId
                            }.lastIndex

                            SOZIPList.removeAt(index)
                        }
                    }
                }
            }
        }
    }
}