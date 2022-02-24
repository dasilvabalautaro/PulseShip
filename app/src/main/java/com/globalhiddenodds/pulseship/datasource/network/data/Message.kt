package com.globalhiddenodds.pulseship.datasource.network.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    var text: String? = null,
    var image: String? = null,
    var target: String? = null,
    var source: String? = null,
    var photo: String? = null,
    var name: String? = null,
    var state: Int = 1) {

    fun toMap(): Map<String, Any>{
        val result = HashMap<String, Any>()
        result["text"] = text?: ""
        result["image"] = image?: ""
        result["target"] = target?: ""
        result["source"] = source?: ""
        result["photo"] = photo?: ""
        result["name"] = name?: ""
        result["state"] = state

        return result
    }

}