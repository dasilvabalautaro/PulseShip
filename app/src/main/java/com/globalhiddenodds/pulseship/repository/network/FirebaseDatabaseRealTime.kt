package com.globalhiddenodds.pulseship.repository.network

import android.util.Log
import com.globalhiddenodds.pulseship.repository.database.MessageDao
import com.globalhiddenodds.pulseship.datasource.network.data.MemberMessage
import com.globalhiddenodds.pulseship.datasource.network.data.Message
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.jvm.Throws

class FirebaseDatabaseRealTime @Inject constructor(
    private val messageDao: MessageDao
) {

    private val rootMembers = "members"
    private val rootMessage = "message"
    private val database = Firebase.database
    private val childMember = database.reference.child(rootMembers)
    private val tag = "FirebaseDatabaseRealTime"

    fun newMember(uid: String,
                  newMemberMessage: MemberMessage){

        childMember.child(uid).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()){
                    val childUser = childMember.child(uid)

                    val mapMember = hashMapOf<String, Any>()
                    mapMember["name"] = newMemberMessage.name!!
                    mapMember["photo"] = newMemberMessage.photo!!

                    childUser.setValue(mapMember)
                        .addOnSuccessListener {
                            val mapMessage = newMemberMessage.message!!.toMap()
                            childUser.child(rootMessage).setValue(mapMessage)
                                .addOnSuccessListener {
                                    Log.i(tag, "Member created")
                                }
                                .addOnFailureListener {
                                    Log.e(tag, it.toString())
                                }

                        }
                        .addOnFailureListener {
                            Log.e(tag, it.toString())
                        }
                }
                else Log.i(tag, "Member exist")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(tag, error.message)
            }
        })

    }

    @Throws(Exception::class)
    fun sendMessage(uid: String,
                            newMessage: Message){
        val postMessage = newMessage.toMap()

        childMember.child(uid).child(rootMessage).updateChildren(postMessage)
            .addOnSuccessListener {
                Log.i(tag, "Sent OK")
                if (newMessage.source!!.isNotBlank() &&
                    newMessage.target!!.isNotBlank()){

                    saveMessage(newMessage)
                }

            }
            .addOnFailureListener {
                throw it
            }
    }

    fun listenerMessageEvent(uid: String){
        childMember.child(uid).child(rootMessage)
            .addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //val msgReceived: HashMap<*, *>? = (snapshot.value as HashMap<*, *>?)
                val msg = snapshot.getValue(Message::class.java)

                if (msg != null && msg.source!!.isNotBlank() &&
                    msg.target!!.isNotBlank()){

                    clearMessageCloud(uid)
                    saveMessage(msg)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(tag, error.toException().toString())
            }
        })
    }

    private fun saveMessage(msg: Message) = runBlocking(Dispatchers.IO){
        val lastMessage = msg.copy()

        launch {
            val message = com.globalhiddenodds
                .pulseship.datasource
                .database.Message(0, lastMessage.target!!, lastMessage.source!!,
                    lastMessage.name!!, lastMessage.image!!, System.currentTimeMillis(), lastMessage.text!!,
                    lastMessage.photo!!, lastMessage.state)
            messageDao.insert(message)

        }
    }

    private fun clearMessageCloud(uid: String){
        val message = Message(
            "",
            "",
            "",
            "",
            "",
            ""
        )
        try {
            sendMessage(uid, message)
        }catch (e: java.lang.Exception){
            Log.e(tag, e.message!!)
        }

    }


}