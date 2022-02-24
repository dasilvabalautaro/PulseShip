package com.globalhiddenodds.pulseship.ui.viewModel

import android.util.Log
//import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.globalhiddenodds.pulseship.R
import com.globalhiddenodds.pulseship.util.Result
import com.globalhiddenodds.pulseship.datasource.database.Owner
import com.globalhiddenodds.pulseship.di.IoDispatcher
import com.globalhiddenodds.pulseship.ui.util.InputLoginState
import com.globalhiddenodds.pulseship.domain.OwnerDomain
import com.globalhiddenodds.pulseship.ui.extensions.toBase64String
import com.globalhiddenodds.pulseship.ui.fragments.login.LoginResult
import com.globalhiddenodds.pulseship.ui.util.EmailValidator
import com.globalhiddenodds.pulseship.ui.util.ManagerFile
import com.globalhiddenodds.pulseship.ui.util.TaskResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OwnerViewModel @Inject constructor (
    private val handle: SavedStateHandle,
    private val ownerRepository: OwnerDomain,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher): ViewModel(){

    @Inject
    lateinit var managerFile: ManagerFile

    private val loginSuccessful: String = "LOGIN_SUCCESSFUL"
    private val inputLoginMutableLiveData = MutableLiveData<InputLoginState>()
    val inputLoginLiveData: LiveData<InputLoginState> = inputLoginMutableLiveData

    private val loginResultMutableLive = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = loginResultMutableLive

    private val taskResultMutableLive = MutableLiveData<TaskResult>()
    val taskResult: LiveData<TaskResult> = taskResultMutableLive

    val stateLogin: LiveData<Boolean> = handle.getLiveData(loginSuccessful)

    private var ownerMutable = MutableLiveData<Owner>()
    val ownerCurrent: LiveData<Owner> = ownerMutable

    var uidCurrent = ""

    init {
        handle[loginSuccessful] = false
    }

    fun setStateLogin(state: Boolean){
        handle[loginSuccessful] = state
    }

    fun ownerExist(){
        viewModelScope.launch {
            val result = ownerRepository.ownerExist()

            if (result.isNotEmpty()){
                uidCurrent = result
                loginResultMutableLive.value =
                    LoginResult(success = "User found")

            }else{
                loginResultMutableLive.value = LoginResult(error = R.string.user_not_found)  //
            }
        }
    }

    fun getOwnerProfile(){
        viewModelScope.launch {
            val result = ownerRepository.getOwnerProfile()
            if (result is Result.Success){
                uidCurrent = result.data.uid
            }
            else{
                loginResultMutableLive.value = LoginResult(error = R.string.profile_not_found)
            }

        }


    }

    fun createOwner(email: String, password: String){
        viewModelScope.launch{
            val result = ownerRepository.createOwner(
                email, password) //, activity

            if (result is Result.Success){
                uidCurrent = result.data.uid
                loginResultMutableLive.value =
                    LoginResult(success = result.data.name)
            }
            else{
                Log.e("ViewModal", result.toString())
                loginResultMutableLive.value = LoginResult(error = R.string.create_failed)  //
            }
        }
    }

    fun signIn(email: String, password: String){
        viewModelScope.launch {
            val result = ownerRepository.signIn(email, password)
            if (result is Result.Success){
                Log.d("ViewModel", result.toString())
                loginResultMutableLive.value =
                    LoginResult(success = result.data.name)
            }
            else{
                Log.e("ViewModal", result.toString())
                loginResultMutableLive.value = LoginResult(error = R.string.login_failed)  //
            }

        }
    }

    fun updatePhotoOwner(pathPhoto: String){
        viewModelScope.launch {
            val base64 = changePhoto(pathPhoto)
            val newOwner = ownerMutable.value!!.copy(photoB64 = base64)
            ownerMutable.value = newOwner
        }
    }

    fun updateNameOwner(name: String){
        ownerMutable.value!!.name = name
    }

    private suspend fun changePhoto(pathPhoto: String): String{
        return withContext(ioDispatcher) {
            val inputBuffer = managerFile.loadInputBuffer(pathPhoto)
            val bitmap = managerFile.decodeBitmap(inputBuffer, inputBuffer.size)
            return@withContext bitmap.toBase64String()
        }
    }

    fun updateOwner(){
        viewModelScope.launch {
            val owner = ownerMutable.value!!.copy()
            val result = ownerRepository.updateOwner(owner)

            if (result is Result.Success){
                taskResultMutableLive.value =
                    TaskResult(success =  "Updated Ok.")
            }
            else{
                taskResultMutableLive.value = TaskResult(error = result.toString())  //
            }
        }
    }

    fun retrieveOwner(uid: String) {
        viewModelScope.launch {
            ownerMutable.value = ownerRepository.retrieveOwner(uid)
        }

    }

    fun getTokenOwner(){
        viewModelScope.launch {
            val result = ownerRepository.getToken()

            if (result is Result.Success){
                loginResultMutableLive.value =
                    LoginResult(success = result.data.token)
            }
            else{
                Log.e("ViewModal", result.toString())
                loginResultMutableLive.value = LoginResult(error = R.string.user_not_found)
            }
        }
    }

    fun verifyDataEntry(email: String, password: String) {
        if (!EmailValidator.isValidEmail(email)) {
            inputLoginMutableLiveData.value = InputLoginState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            inputLoginMutableLiveData.value = InputLoginState(passwordError = R.string.invalid_password)
        } else {
            inputLoginMutableLiveData.value = InputLoginState(isDataValid = true)
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}