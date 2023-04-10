package com.eje.sozip.userManagement.helper

import com.eje.sozip.frameworks.helper.AES256Util
import com.eje.sozip.userManagement.models.AuthResultModel
import com.eje.sozip.userManagement.models.UserInfoModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class UserManagement {
    companion object{
        var userInfo : UserInfoModel? = null
    }

    private val auth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()

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
                            completion(AuthResultModel.SUCCESS)
                            return@addOnCompleteListener
                        } else{
                            completion(AuthResultModel.UNKNOWN)
                            return@addOnCompleteListener
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
}