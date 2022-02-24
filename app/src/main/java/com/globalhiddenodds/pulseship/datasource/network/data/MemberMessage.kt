package com.globalhiddenodds.pulseship.datasource.network.data

import com.google.firebase.database.IgnoreExtraProperties
import kotlin.collections.HashMap

@IgnoreExtraProperties
data class MemberMessage(var name: String? = null,
                         var photo: String? = null,
                         var message: Message? = null){

    fun toMap(): Map<String, Any>{
        val result = HashMap<String, Any>()
        result["name"] = name?: ""
        result["photo"] = photo?: ""
        result["message"] = message?.toMap()?: hashMapOf<String, Any>()
        return result
    }
}