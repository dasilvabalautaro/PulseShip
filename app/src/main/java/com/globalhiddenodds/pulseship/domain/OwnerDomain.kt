package com.globalhiddenodds.pulseship.domain

import android.util.Log
//import androidx.appcompat.app.AppCompatActivity
import com.globalhiddenodds.pulseship.util.Result
import com.globalhiddenodds.pulseship.datasource.database.Owner
import com.globalhiddenodds.pulseship.repository.database.OwnerDao
import com.globalhiddenodds.pulseship.repository.network.FirebaseAuthUtil
import com.globalhiddenodds.pulseship.di.IoDispatcher
import com.globalhiddenodds.pulseship.domain.data.OwnerView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OwnerDomain @Inject constructor(
    private val firebaseAuthUtil: FirebaseAuthUtil,
    private val ownerDao: OwnerDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher)
{

    suspend fun ownerExist(): String{
        return withContext(ioDispatcher){
            firebaseAuthUtil.ownerExist()
        }
    }

    suspend fun getOwnerProfile(): Result<OwnerView>{
        return withContext(ioDispatcher){
            firebaseAuthUtil.getOwnerProfile()
        }
    }
    suspend fun getToken(): Result<OwnerView> {
        return withContext(ioDispatcher){
            val result = firebaseAuthUtil.getAuthToken()

            if (result is Result.Success){
                setTokenInOwner(result.data)
            }
            return@withContext result
        }
    }
    //,
    //                            activity: AppCompatActivity
    suspend fun createOwner(email: String,
                            password: String): Result<OwnerView> {
        return withContext(ioDispatcher){
            val result = firebaseAuthUtil.createOwner(
                email, password) //, activity

            if (result is Result.Success){
                addNewOwner(result.data)
            }

            return@withContext result
        }
    }

    suspend fun signIn(email: String,
                       password: String): Result<OwnerView> {
        return withContext(ioDispatcher){
            firebaseAuthUtil.signIn(email, password)
        }
    }

    private suspend fun setTokenInOwner(ownerTemp: OwnerView) {
        Log.d("OwnerDomain", ownerTemp.uid)
        ownerDao.updateToken(ownerTemp.uid, ownerTemp.token)
    }

    private fun getNewOwner(uid: String, name: String,
                            email: String): Owner{

        return Owner(
            uid = uid,
            name = name,
            email = email,
            photoB64 = "",
            date = System.currentTimeMillis(),
            token = ""
        )
    }

    private suspend fun addNewOwner(ownerView: OwnerView){
        val newOwner = getNewOwner(ownerView.uid, ownerView.name, ownerView.email)
        ownerDao.insert(newOwner)
    }

    suspend fun retrieveOwner(uid: String): Owner {
        return withContext(ioDispatcher){
            ownerDao.getOwner(uid)
        }
    }

    /*suspend fun getUniqueUser(): Owner {
        return withContext(ioDispatcher){
            ownerDao.getUniqueUser()
        }
    }
*/
    suspend fun updateOwner(owner: Owner): Result<Boolean>{
        return withContext(ioDispatcher){
            ownerDao.update(owner)
            firebaseAuthUtil.updateProfile(owner.name)
        }
    }
}