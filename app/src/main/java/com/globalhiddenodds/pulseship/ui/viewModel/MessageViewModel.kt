package com.globalhiddenodds.pulseship.ui.viewModel

import androidx.lifecycle.*
import com.globalhiddenodds.pulseship.datasource.database.SSetCountNewMessages
import com.globalhiddenodds.pulseship.datasource.network.data.MemberMessage
import com.globalhiddenodds.pulseship.datasource.network.data.Message
import com.globalhiddenodds.pulseship.domain.MessageDomain
import com.globalhiddenodds.pulseship.ui.util.TaskResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val messageRepository: MessageDomain
) : ViewModel() {

    private val uidKeyCurrent: String = "UID_CURRENT"
    private val uidKeyContact: String = "UID_CONTACT"

    private val taskResultMutableLive = MutableLiveData<TaskResult>()
    val taskResult: LiveData<TaskResult> = taskResultMutableLive
    val allMessages: LiveData<List<com.globalhiddenodds
    .pulseship.datasource.database.Message>> = messageRepository.getMessages()
    private val uidCurrent: LiveData<String> by lazy { handle.getLiveData(uidKeyCurrent) }
    private val uidContact: LiveData<String> by lazy { handle.getLiveData(uidKeyContact) }
    val newMessagesByContact:
            LiveData<List<SSetCountNewMessages>> =
        messageRepository.getNewMessagesByContact(uidCurrent.value!!)
    val messagesWithContact: LiveData<List<com.globalhiddenodds
    .pulseship.datasource.database.Message>> =
        messageRepository.getMessageOfContact(uidContact.value!!)

    init {
        handle[uidKeyContact] = ""
        handle[uidKeyContact] = ""
    }

    fun setUIDCurrent(uid: String){
        handle[uidKeyContact] = uid
    }

    fun setUIDContact(uid: String){
        handle[uidKeyContact] = uid
    }

    fun newMember(
        uid: String,
        newMemberMessage: MemberMessage
    ) {
        viewModelScope.launch {
            messageRepository.newMember(uid, newMemberMessage)

        }
    }

    fun sendMessage(
        uid: String,
        newMessage: Message
    ) {
        viewModelScope.launch {
            val result = messageRepository.sendMessage(uid, newMessage)

            if (result.isNotEmpty()) {
                taskResultMutableLive.value = TaskResult(error = result)

            }
        }
    }

    fun listenerMessageEvent(uid: String) {
        viewModelScope.launch {
            messageRepository.listenerMessageEvent(uid)
        }

    }


    fun clearMessage(uid: String) {
        val message = Message(
            "",
            "",
            "",
            "",
            "",
            ""
        )
        sendMessage(uid, message)
    }
}