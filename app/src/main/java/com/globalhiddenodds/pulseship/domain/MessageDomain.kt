package com.globalhiddenodds.pulseship.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.globalhiddenodds.pulseship.datasource.database.SSetCountNewMessages
import com.globalhiddenodds.pulseship.repository.database.MessageDao
import com.globalhiddenodds.pulseship.repository.network.FirebaseDatabaseRealTime
import com.globalhiddenodds.pulseship.datasource.network.data.MemberMessage
import com.globalhiddenodds.pulseship.datasource.network.data.Message
import com.globalhiddenodds.pulseship.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class MessageDomain @Inject constructor(
    private val realTime: FirebaseDatabaseRealTime,
    private val messageDao: MessageDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher) {

    suspend fun newMember(uid: String,
                          newMemberMessage: MemberMessage){
        withContext(ioDispatcher){
            realTime.newMember(uid, newMemberMessage)
        }
    }

    suspend fun sendMessage(uid: String,
                            newMessage: Message
    ): String{
        return withContext(ioDispatcher){
            var result = ""
            try {
                realTime.sendMessage(uid, newMessage)
            }catch (e: Exception){
                result = e.message!!
            }
            return@withContext result
        }
    }

    suspend fun listenerMessageEvent(uid: String){
        withContext(ioDispatcher){
            realTime.listenerMessageEvent(uid)
        }

    }

    fun getMessages(): LiveData<List<com.globalhiddenodds
    .pulseship.datasource.database.Message>>{
        return messageDao.getMessages().asLiveData()
    }

    fun getMessageOfContact(uidContact: String): LiveData<List<com.globalhiddenodds
    .pulseship.datasource.database.Message>>{
        return messageDao.getMessageOfContact(uidContact).asLiveData()
    }

    fun getNewMessagesByContact(uidCurrent: String):
            LiveData<List<SSetCountNewMessages>>{
        return messageDao.getCountNewMessages(uidCurrent).asLiveData()
    }
}