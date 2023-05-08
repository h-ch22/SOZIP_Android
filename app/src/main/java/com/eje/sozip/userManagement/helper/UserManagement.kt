package com.eje.sozip.userManagement.helper

import androidx.compose.ui.graphics.Color
import com.eje.sozip.frameworks.helper.AES256Util
import com.eje.sozip.ui.theme.SOZIP_BG_1
import com.eje.sozip.ui.theme.SOZIP_BG_2
import com.eje.sozip.ui.theme.SOZIP_BG_3
import com.eje.sozip.ui.theme.SOZIP_BG_4
import com.eje.sozip.ui.theme.SOZIP_BG_5
import com.eje.sozip.userManagement.models.AuthResultModel
import com.eje.sozip.userManagement.models.ChangePasswordResultModel
import com.eje.sozip.userManagement.models.UserInfoModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class UserManagement {
    companion object{
        var userInfo : UserInfoModel? = null
        var accountInfo : MutableList<String>? = null

        fun convertProfileToEmoji(profile : String) : String{
            when(profile){
                "chick" -> return "\uD83D\uDC25"
                "pig" -> return "\uD83D\uDC37"
                "rabbit" -> return "\uD83D\uDC30"
                "tiger" -> return "\uD83D\uDC2F"
                "monkey" -> return "\uD83D\uDC35"
                else -> return "\uD83D\uDC25"
            }
        }

        fun convertEmojiToProfile(emoji : String) : String{
            when(emoji){
                "\uD83D\uDC25" -> return "chick"
                "\uD83D\uDC37" -> return "pig"
                "\uD83D\uDC30" -> return "rabbit"
                "\uD83D\uDC2F" -> return "tiger"
                "\uD83D\uDC35" -> return "monkey"
                else -> return "chick"
            }
        }

        fun convertColorToProfileBG(color : Color) : String{
            when(color){
                SOZIP_BG_1 -> return "bg_1"
                SOZIP_BG_2 -> return "bg_2"
                SOZIP_BG_3 -> return "bg_3"
                SOZIP_BG_4 -> return "bg_4"
                SOZIP_BG_5 -> return "bg_5"
                else -> return "bg_3"
            }
        }

        fun convertProfileBGToColor(bg : String) : Color{
            when(bg){
                "bg_1" -> return SOZIP_BG_1
                "bg_2" -> return SOZIP_BG_2
                "bg_3" -> return SOZIP_BG_3
                "bg_4" -> return SOZIP_BG_4
                "bg_5" -> return SOZIP_BG_5
                else -> return SOZIP_BG_3
            }
        }
    }

    private val auth = Firebase.auth
    private val db = Firebase.firestore

    fun signUp(
        userInfo : UserInfoModel,
        password : String,
        isMarketingAccept : Boolean,
        completion : ((AuthResultModel) -> Unit)
    ){
        auth.createUserWithEmailAndPassword(userInfo.email, password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    db.collection("Users").document(auth.currentUser?.uid ?: "").set(
                        hashMapOf("mail" to AES256Util.encrypt(userInfo.email),
                        "name" to AES256Util.encrypt(userInfo.name),
                        "nickName" to AES256Util.encrypt(userInfo.nickName),
                        "phone" to AES256Util.encrypt(userInfo.phone),
                        "studentNo" to AES256Util.encrypt(userInfo.studentNo),
                        "token" to "",
                        "marketingAccept" to isMarketingAccept,
                        "profile" to "chick",
                        "profile_bg" to "bg_3")
                    ).addOnCompleteListener {
                        if(it.isSuccessful){
                            getUserData {
                                if(it){
                                    completion(AuthResultModel.SUCCESS)
                                } else{
                                    auth.signOut()
                                    completion(AuthResultModel.UNKNOWN)
                                }
                            }

                        } else{
                            try {
                                auth.currentUser?.delete()
                            } catch(e : Exception){
                                e.printStackTrace()
                            } finally{
                                completion(AuthResultModel.UNKNOWN)
                                return@addOnCompleteListener
                            }

                        }
                    }.addOnFailureListener {
                        it.printStackTrace()
                        completion(AuthResultModel.UNKNOWN)
                        return@addOnFailureListener
                    }
                } else{
                    try{
                        throw it.exception!!
                    } catch(e : FirebaseAuthWeakPasswordException){
                        completion(AuthResultModel.WEAK_PASSWORD)
                    } catch(e : FirebaseAuthUserCollisionException){
                        completion(AuthResultModel.EMAIL_EXCEPTION)
                    } catch(e : FirebaseTooManyRequestsException){
                        completion(AuthResultModel.TOO_MANY_REQUESTS)
                    } catch(e : FirebaseNetworkException){
                        completion(AuthResultModel.NETWORK_EXCEPTION)
                    }
                }
            }
    }

    fun signIn(
        email : String,
        password : String,
        completion : ((Boolean) -> Unit)
    ){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                getUserData {
                    if(it){
                        getAccountInfo {
                            if(it){
                                completion(true)
                                return@getAccountInfo
                            } else{
                                completion(true)
                                return@getAccountInfo
                            }
                        }
                    } else{
                        auth.signOut()
                        completion(false)
                        return@getUserData
                    }
                }
            }
        }.addOnFailureListener {
            it.printStackTrace()
            completion(false)

            return@addOnFailureListener
        }
    }

    fun getUserData(completion: (Boolean) -> Unit){
        db.collection("Users").document(auth.currentUser?.uid ?: "").get().addOnCompleteListener {
            if(it.isSuccessful){
                val document = it.result

                if(document.exists()){
                    userInfo = UserInfoModel(name = document.get("name") as? String ?: "",
                                            phone = document.get("phone") as? String ?: "",
                                            email = document.get("email") as? String ?: "",
                                            studentNo = document.get("studentNo") as? String ?: "",
                                            school = document.get("school") as? String ?: "",
                                            nickName = document.get("nickName") as? String ?: "",
                                            uid = auth.currentUser?.uid ?: "",
                                            profile = document.get("profile") as? String ?: "",
                                            profile_bg = document.get("profile_bg") as? String ?: "")
                    
                    completion(true)
                    return@addOnCompleteListener
                }
            }
        }.addOnFailureListener {
            it.printStackTrace()
            completion(false)
            return@addOnFailureListener
        }
    }

    fun getAccountInfo(completion: (Boolean) -> Unit){
        db.collection("Users").document(auth.currentUser?.uid ?: "").get().addOnCompleteListener {
            if(it.isSuccessful){
                val document = it.result

                if(document.exists()){
                    val accounts = document.get("bankAccount") as? List<*>

                    if(accounts != null){
                        accountInfo = accounts as MutableList<String>
                    } else{
                        accountInfo = mutableListOf()
                    }

                    completion(true)
                    return@addOnCompleteListener
                }
            } else{
                completion(false)
                return@addOnCompleteListener
            }
        }.addOnFailureListener {
            it.printStackTrace()
            completion(false)
            return@addOnFailureListener
        }
    }

    fun getMarketingInfo(completion: (Boolean) -> Unit){
        db.collection("Users").document(auth.currentUser?.uid ?: "").get().addOnCompleteListener {
            if(it.isSuccessful){
                val document = it.result

                if(document.exists()){
                    completion(document.get("marketingAccept") as? Boolean ?: false)
                    return@addOnCompleteListener
                }
            }
        }.addOnFailureListener {
            it.printStackTrace()
            completion(false)
            return@addOnFailureListener
        }
    }

    fun updateMarketingInfo(isAccept : Boolean, completion: (Boolean) -> Unit){
        db.collection("Users").document(auth.currentUser?.uid ?: "").update(mapOf("marketingAccept" to isAccept)).addOnCompleteListener {
            if(it.isSuccessful){
                completion(true)
                return@addOnCompleteListener
            }
        }.addOnFailureListener {
            it.printStackTrace()
            completion(false)
            return@addOnFailureListener
        }
    }

    fun updateProfile(nickName : String, profile : String, profile_bg : String, completion: (Boolean) -> Unit){
        db.collection("Users").document(auth.currentUser?.uid ?: "").update(mapOf(
            "nickName" to AES256Util.encrypt(nickName),
            "profile" to profile,
            "profile_bg" to profile_bg
        )).addOnCompleteListener {
            if(it.isSuccessful){
                getUserData {
                    completion(true)
                    return@getUserData
                }
            } else{
                completion(false)
                return@addOnCompleteListener
            }
        }.addOnFailureListener {
            it.printStackTrace()
            completion(false)
            return@addOnFailureListener
        }
    }

    fun updatePassword(password : String, currentPassword : String, completion: (ChangePasswordResultModel) -> Unit){

        auth.signInWithEmailAndPassword(auth.currentUser?.email ?: "", currentPassword).addOnFailureListener {
            it.printStackTrace()
            completion(ChangePasswordResultModel.PASSWORD_DOES_NOT_MATCH)
            return@addOnFailureListener
        }.addOnCompleteListener {
            if(it.isSuccessful){
                auth.currentUser?.updatePassword(password)?.addOnCompleteListener {
                    if(it.isSuccessful){
                        try{
                            auth.signOut()
                            completion(ChangePasswordResultModel.SUCCESS)
                            return@addOnCompleteListener

                        } catch(e : Exception){
                            e.printStackTrace()
                            completion(ChangePasswordResultModel.ERROR)
                        }
                    }else{
                        completion(ChangePasswordResultModel.ERROR)
                        return@addOnCompleteListener
                    }
                }?.addOnFailureListener {
                    it.printStackTrace()
                    completion(ChangePasswordResultModel.ERROR)
                    return@addOnFailureListener
                }
            } else{
                completion(ChangePasswordResultModel.PASSWORD_DOES_NOT_MATCH)
                return@addOnCompleteListener
            }
        }
    }

    fun updatePhone(phone : String, completion: (Boolean) -> Unit){
        db.collection("Users").document(auth.currentUser?.uid ?: "").update(mapOf(
            "phone" to phone
        )).addOnCompleteListener {
            if(it.isSuccessful){
                getUserData {
                    if(it){
                        completion(true)
                        return@getUserData
                    } else{
                        completion(true)
                        return@getUserData
                    }
                }
            } else{
                completion(false)
                return@addOnCompleteListener
            }
        }.addOnFailureListener {
            completion(false)
            return@addOnFailureListener
        }
    }

    fun signOut(completion: (Boolean) -> Unit){
        try{
            auth.signOut()
            completion(true)
        } catch(e : Exception){
            e.printStackTrace()
            completion(false)
            return
        }
    }

    fun secession(completion: (Boolean) -> Unit){
        val uid = auth.currentUser?.uid ?: ""

        auth.currentUser?.delete()?.addOnCompleteListener {
            if(it.isSuccessful){
                db.collection("Users").document(uid).delete().addOnCompleteListener {
                    if(it.isSuccessful){
                        completion(true)
                        return@addOnCompleteListener
                    } else{
                        completion(false)
                        return@addOnCompleteListener
                    }
                }.addOnFailureListener {
                    it.printStackTrace()
                    completion(false)
                    return@addOnFailureListener
                }

            } else{
                completion(false)
                return@addOnCompleteListener
            }
        }?.addOnFailureListener {
            it.printStackTrace()
            completion(false)
            return@addOnFailureListener
        }
    }
}