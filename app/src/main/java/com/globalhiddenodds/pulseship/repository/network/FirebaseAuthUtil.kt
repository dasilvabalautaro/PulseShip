package com.globalhiddenodds.pulseship.repository.network

//import androidx.appcompat.app.AppCompatActivity
import com.globalhiddenodds.pulseship.domain.data.OwnerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import kotlin.Exception
import com.globalhiddenodds.pulseship.util.Result
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class FirebaseAuthUtil @Inject constructor() {
    private var auth: FirebaseAuth = Firebase.auth
    private var ownerView: OwnerView? = null
    private var exception: Exception? = null

    fun ownerExist(): String = runBlocking {
        var result = ""
        val job = launch {
            val currentUser = auth.currentUser
            if (currentUser != null){
                result = currentUser.uid
            }
        }// coroutineScope
        job.join()

        return@runBlocking result
    }

    fun getOwnerProfile(): Result<OwnerView> {
        ownerView = null
        exception = null
        val currentUser = auth.currentUser
        if (currentUser != null) {
            currentUser.let {
                val name = if (!currentUser.displayName.isNullOrEmpty()) currentUser.displayName else "Empty"
                ownerView = OwnerView(currentUser.uid,
                    currentUser.email!!, "empty",
                    name!!, currentUser.isEmailVerified)

            }
        }else{
            exception = Exception("User not found")
        }

        return when {
            ownerView != null -> Result.Success(ownerView!!)
            exception != null -> Result.Error(exception!!)
            else -> Result.Error(Exception("Unknown Error"))
        }
    }

    //(activity) ,
    //                    activity: AppCompatActivity

    suspend fun createOwner(email: String,
                    password: String): Result<OwnerView> {
        ownerView = null
        exception = null
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val currentUser = auth.currentUser
                    if (currentUser != null){
                        ownerView =  OwnerView(currentUser.uid, email, "", "New User")
                    }
                    else{
                        exception = Exception("User not found")
                    }
                }else{
                    exception = task.exception!!
                }
            }.addOnFailureListener{e -> exception = e}.await()

        return when {
            ownerView != null -> Result.Success(ownerView!!)
            exception != null -> Result.Error(exception!!)
            else -> Result.Error(Exception("Unknown Error"))
        }
    }

    suspend fun updateProfile(name: String): Result<Boolean>{
        var result = false
        exception = null
        val user = auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    result = true
                }
                else{
                    exception = task.exception!!
                }
            }.addOnFailureListener{e -> exception = e}.await()

        return when {
            result -> {
                Result.Success(result)
            }
            exception != null -> {
                Result.Error(exception!!)
            }
            else -> {
                Result.Error(Exception("Unknown Error"))
            }
        }
    }

    suspend fun signIn(email: String, password: String): Result<OwnerView> {
        ownerView = null
        exception = null
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val name = if (!auth.currentUser!!.displayName.isNullOrEmpty()) auth.currentUser!!.displayName else "New User"
                    ownerView =  OwnerView("", auth.currentUser!!.email!!, "", name!!)
                }
                else{
                    exception = task.exception!!
                }
            }.addOnFailureListener{e -> exception = e}.await()

        return when {
            ownerView != null -> Result.Success(ownerView!!)
            exception != null -> Result.Error(exception!!)
            else -> Result.Error(Exception("Unknown Error"))
        }
    }

    suspend fun getAuthToken(): Result<OwnerView> {
        ownerView = null
        exception = null
        val currentUser = auth.currentUser
        currentUser?.getIdToken(false)?.addOnCompleteListener { task ->
            if (task.isSuccessful){
                val token = task.result.token
                val name = if (!currentUser.displayName.isNullOrEmpty()) currentUser.displayName else "New User"
                ownerView =  OwnerView(currentUser.uid, currentUser.email!!, token!!, name!!)
            } else{
                exception = task.exception!!
            }
        }?.addOnFailureListener{ e -> exception = e}?.await()

        return when {
            ownerView != null -> Result.Success(ownerView!!)
            exception != null -> Result.Error(exception!!)
            else -> Result.Error(Exception("Unknown Error"))
        }
    }
}

