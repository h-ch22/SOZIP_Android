package com.eje.sozip.SOZIP.helper

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import com.eje.sozip.SOZIP.models.ParticipateSOZIPResultModel
import com.eje.sozip.SOZIP.models.SOZIPDataModel
import com.eje.sozip.SOZIP.models.SOZIPPackagingTypeModel
import com.eje.sozip.frameworks.helper.AES256Util
import com.eje.sozip.ui.theme.SOZIP_BG_1
import com.eje.sozip.ui.theme.SOZIP_BG_2
import com.eje.sozip.ui.theme.SOZIP_BG_3
import com.eje.sozip.ui.theme.SOZIP_BG_4
import com.eje.sozip.ui.theme.SOZIP_BG_5
import com.eje.sozip.userManagement.helper.UserManagement
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date

class SOZIPHelper {
    private val db = Firebase.firestore
    companion object{
        val SOZIPList = mutableStateListOf<SOZIPDataModel>()
        var categoryList = mutableStateListOf<String>()
    }


    fun getSOZIP(completion : (Boolean) -> Unit){
        categoryList.clear()
        SOZIPList.clear()

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
                            val firstCome = diff.document.get("firstCome") as? Long ?: 4
                            val currentPeople = diff.document.get("currentPeople") as? Long ?: 1
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
                                (firstCome + 1).toInt(),
                                AES256Util.decrypt(name),
                                currentPeople.toInt(),
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
                                }.isEmpty()){
                                SOZIPList.add(data)
                            } else {
                                val index = SOZIPList.indexOfFirst {
                                    it.docID == docId
                                }

                                Log.d("SOZIPHelper", "Modifying ID ${data.docID} Index ${index}")


                                SOZIPList[index] = SOZIPList[index].copy(
                                    category = data.category,
                                    firstCome = data.firstCome,
                                    SOZIPName = data.SOZIPName,
                                    currentPeople = data.currentPeople,
                                    location_description = data.location_description,
                                    time = data.time,
                                    Manager = data.Manager,
                                    participants = data.participants,
                                    location = data.location,
                                    address = data.address,
                                    status = data.status,
                                    color = data.color,
                                    account = data.account,
                                    profile = data.profile,
                                    url = data.url,
                                    type = data.type
                                )
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

    fun addSOZIP(SOZIPName : String,
                 location : String,
                 address : String,
                 locationDescription : String,
                 date : String,
                 time : String,
                 color : Color,
                 account : String,
                 url : String,
                 category : String,
                 firstCome : Int,
                 type : SOZIPPackagingTypeModel,
                 completion: (Boolean) -> Unit){
        val docRef = db.collection("SOZIP").document()
        val formatter = SimpleDateFormat("yy/MM/dd HH:mm:ss.SSSS")
        val date = formatter.parse("${date} ${time}:00.0000")
        var color_String = ""

        when(color){
            SOZIP_BG_1 -> color_String = "bg_1"
            SOZIP_BG_2 -> color_String = "bg_2"
            SOZIP_BG_3 -> color_String = "bg_3"
            SOZIP_BG_4 -> color_String = "bg_4"
            SOZIP_BG_5 -> color_String = "bg_5"
            else -> color_String = "bg_1"
        }

        val current = formatter.format(Date())

        docRef.set(
            mapOf("name" to AES256Util.encrypt(SOZIPName),
                "location" to AES256Util.encrypt(location),
                "address" to AES256Util.encrypt(address),
                "description" to AES256Util.encrypt(locationDescription),
                "dateTime" to date,
                "category" to category,
                "firstCome" to firstCome,
                "Manager" to UserManagement.userInfo?.uid,
                "participants" to mapOf(UserManagement.userInfo?.uid to UserManagement.userInfo?.nickName),
                "payMethod" to mapOf(UserManagement.userInfo?.uid to ""),
                "transactionMethod" to mapOf(UserManagement.userInfo?.uid to ""),
                "last_msg" to AES256Util.encrypt("소집이 시작되었어요!"),
                "profiles" to mapOf(UserManagement.userInfo?.uid to "${UserManagement.userInfo?.profile}, ${UserManagement.userInfo?.profile_bg}"),
                "last_msg_type" to "participate",
                "color" to color_String,
                "last_msg_time" to current,
                "account" to account,
                "url" to url,
                "type" to if(type == SOZIPPackagingTypeModel.DELIVERY) "DELIVERY" else "TAKE_OUT")
        ).addOnCompleteListener {
            if(it.isSuccessful){
                val chatRef = docRef.collection("Chat").document()
                chatRef.set(mapOf(
                    "msg" to AES256Util.encrypt("소집이 시작되었어요!"),
                    "msg_type" to "participate",
                    "sender" to UserManagement.userInfo?.uid,
                    "date" to current,
                    "unread" to listOf<String>()
                )).addOnCompleteListener {
                    if(it.isSuccessful){
                        completion(true)
                    } else{
                        docRef.delete()
                        completion(false)
                    }
                }
            } else{
                completion(false)
            }
        }.addOnFailureListener {
            completion(false)
        }
    }

    fun participate_SOZIP(id : String, position : String, completion: (ParticipateSOZIPResultModel) -> Unit){
        val SOZIPRef = db.collection("SOZIP").document(id)

        SOZIPRef.get().addOnCompleteListener {
            if(it.isSuccessful){
                val document = it.result

                if(document.exists()){
                    val current = document.get("currentPeople") as? Long ?: 0
                    val limit = document.get("firstCome") as? Long ?: 0

                    if(current >= limit + 1){
                        completion(ParticipateSOZIPResultModel.LIMIT_PEOPLE)
                        return@addOnCompleteListener
                    }

                    val participates = document.get("participants") as? Map<String, String>
                    if (participates != null) {
                        if(participates.keys.contains(UserManagement.userInfo?.uid)){
                            completion(ParticipateSOZIPResultModel.ALREADY_PARTICIPATED)
                            return@addOnCompleteListener
                        }
                    }

                    SOZIPRef.update(mapOf(
                        "participants.${UserManagement.userInfo?.uid}" to UserManagement.userInfo?.nickName,
                        "profiles.${UserManagement.userInfo?.uid}" to "${UserManagement.userInfo?.profile},${UserManagement.userInfo?.profile_bg}"
                    )).addOnCompleteListener {
                        if(it.isSuccessful){
                            val formatter = SimpleDateFormat("yy/MM/dd HH:mm:ss.SSSS")
                            val chatRef = SOZIPRef.collection("Chat").document()
                            chatRef.set(mapOf(
                                "msg" to AES256Util.encrypt("${AES256Util.decrypt(UserManagement.userInfo?.nickName)}님이 참여했어요!"),
                                "msg_type" to "participate",
                                "sender" to UserManagement.userInfo?.uid,
                                "date" to formatter.format(Date()),
                                "unread" to listOf<String>()
                            )).addOnCompleteListener {
                                if(it.isSuccessful){
                                    completion(ParticipateSOZIPResultModel.SUCCESS)
                                } else{
                                    completion(ParticipateSOZIPResultModel.SUCCESS)
                                }
                            }
                        } else{
                            completion(ParticipateSOZIPResultModel.ERROR)
                        }
                    }.addOnFailureListener {
                        it.printStackTrace()
                        completion(ParticipateSOZIPResultModel.ERROR)
                    }

                } else{
                    completion(ParticipateSOZIPResultModel.ERROR)
                }
            } else{
                completion(ParticipateSOZIPResultModel.ERROR)
            }
        }.addOnFailureListener {
            it.printStackTrace()
            completion(ParticipateSOZIPResultModel.ERROR)
        }
    }
}