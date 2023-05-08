package com.eje.sozip.chat.helper

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import com.eje.sozip.chat.models.ChatContentsDataModel
import com.eje.sozip.chat.models.ChatListDataModel
import com.eje.sozip.ui.theme.SOZIP_BG_1
import com.eje.sozip.ui.theme.SOZIP_BG_2
import com.eje.sozip.ui.theme.SOZIP_BG_3
import com.eje.sozip.ui.theme.SOZIP_BG_4
import com.eje.sozip.ui.theme.SOZIP_BG_5
import com.eje.sozip.userManagement.helper.UserManagement
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatHelper {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    companion object{
        val chatList : MutableList<ChatListDataModel> = mutableStateListOf()
        val chatContents : MutableList<ChatContentsDataModel> = mutableStateListOf()
    }

    fun getChatList(completion : (Boolean) -> Unit){
        chatList.clear()

        val SOZIPRef = db.collection("SOZIP").orderBy("last_msg_time", Query.Direction.DESCENDING)

        SOZIPRef.addSnapshotListener{ value, error ->
            if(error != null){
                error.printStackTrace()
                completion(false)
                return@addSnapshotListener
            } else{
                value?.documentChanges?.forEach{
                    when(it.type){
                        DocumentChange.Type.ADDED,
                        DocumentChange.Type.MODIFIED ->{
                            val participants = it.document.get("participants") as? Map<String, String> ?: mapOf()

                            if(participants.keys.contains(auth.currentUser?.uid ?: "")){
                                val docId = it.document.id
                                val SOZIPName = it.document.get("name") as? String ?: ""
                                val currentPeople = it.document.get("currentPeople") as? Long ?: 0

                                val last_msg = it.document.get("last_msg") as? String ?: ""
                                val status = it.document.get("status") as? String ?: ""
                                val color = it.document.get("color") as? String ?: "bg_1"
                                val last_msg_time = it.document.get("last_msg_time") as? String ?: ""
                                val profiles = it.document.get("profiles") as? Map<String, String>
                                val manager = it.document.get("Manager") as? String ?: ""
                                var colorCode : Color = SOZIP_BG_1

                                when(color){
                                    "bg_1" -> colorCode = SOZIP_BG_1
                                    "bg_2" -> colorCode = SOZIP_BG_2
                                    "bg_3" -> colorCode = SOZIP_BG_3
                                    "bg_4" -> colorCode = SOZIP_BG_4
                                    "bg_5" -> colorCode = SOZIP_BG_5
                                    else -> colorCode = SOZIP_BG_1
                                }

                                Log.d("ChatHelper","DOC TYPE : ${it.type}")

                                for(chat in chatList)
                                {
                                    Log.d("ChatHelper", chat.toString())
                                }
                                if(chatList.filter {
                                        it.docId == docId
                                    }.isEmpty()){
                                    Log.d("ChatHelper", "Document not found with id ${docId}\n${chatList.toString()}")
                                    chatList.add(
                                        ChatListDataModel(
                                            docId = docId, SOZIPName = SOZIPName, currentPeople = currentPeople.toInt(),
                                            last_msg = last_msg, status = status, color = colorCode, last_msg_time = last_msg_time,
                                            profiles = profiles, manager = manager, participants = participants
                                        )
                                    )
                                }
                                else{
                                    val index = chatList.indexOfFirst {
                                        it.docId == docId
                                    }

                                    Log.d("ChatHelper", "Document found with id ${docId}, index : ${index} \n${chatList.toString()}")

                                    chatList[index] = chatList[index].copy(
                                        SOZIPName = SOZIPName,
                                        currentPeople = currentPeople.toInt(),
                                        last_msg = last_msg,
                                        status = status,
                                        color = colorCode,
                                        last_msg_time = last_msg_time,
                                        profiles = profiles,
                                        manager = manager,
                                        participants = participants
                                    )
                                }
                            }
                        }

                        DocumentChange.Type.REMOVED -> {
                            val docId = it.document.id
                            val index = chatList.filter{
                                it.docId == docId
                            }.lastIndex

                            chatList.removeAt(index)

                        }

                    }
                }

            }
        }
    }

    fun getChatContents(data : ChatListDataModel, completion: (Boolean) -> Unit){
        chatContents.clear()

        val chatRef = db.collection("SOZIP").document(data.docId).collection("Chat")

        chatRef.orderBy("date", Query.Direction.ASCENDING).addSnapshotListener { value, error ->
            if(error != null){
                println(error)
                completion(false)
                return@addSnapshotListener
            }

            else{
                value?.documentChanges?.forEach { diff ->
                    when(diff.type){
                        DocumentChange.Type.ADDED,
                        DocumentChange.Type.MODIFIED ->{
                            val date = diff.document.get("date") as? String ?: ""
                            val msg = diff.document.get("msg") as? String ?: ""
                            val msg_type = diff.document.get("msg_type") as? String ?: ""
                            val sender = diff.document.get("sender") as? String ?: ""
                            val unread = diff.document.get("unread") as List<String>
                            val rootDocId = data.docId
                            val docId = diff.document.id
                            val imgIndex = diff.document.get("imageIndex") as? Long
                            val profile_all = data.profiles?.get(sender) as? String ?: "chick,bg_3"
                            val profile_split = profile_all.split(",")
                            var profile = ""
                            var profile_BG = SOZIP_BG_1
                            val url = diff.document.get("url") as? List<String>
                            val account = diff.document.get("url") as? String

                            profile = UserManagement.convertProfileToEmoji(profile_split[0])
                            profile_BG = UserManagement.convertProfileBGToColor(profile_split[1])

                            if(chatContents.filter {
                                    it.docId == docId
                                }.isEmpty()){
                                chatContents.add(
                                    ChatContentsDataModel(rootDocId, docId, msg, sender, unread.size, date, msg_type, imgIndex?.toInt(), profile, profile_BG, data.participants.get(sender) ?: "", url, account)
                                )
                            }
                            else{
                                val index = chatContents.indexOfFirst {
                                    it.docId == docId
                                }

                                chatContents[index] = chatContents[index].copy(
                                    rootDocId = rootDocId,
                                    msg = msg,
                                    sender = sender,
                                    unread = unread.size,
                                    time = date,
                                    type = msg_type,
                                    imgIndex = imgIndex?.toInt(),
                                    profile = profile,
                                    profile_BG = profile_BG,
                                    nickName = data.participants.get(sender) ?: "",
                                    url = url,
                                    account = account
                                )
                            }
                        }

                        DocumentChange.Type.REMOVED -> {

                        }
                    }
                }
            }
        }
    }
}