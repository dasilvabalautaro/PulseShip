package com.globalhiddenodds.pulseship.datasource.database

import android.annotation.SuppressLint
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "message")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "target")
    val target: String,
    @ColumnInfo(name = "source")
    val source: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "image")
    var image: String,
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "message")
    val message: String,
    @ColumnInfo(name = "photo")
    var photoB64: String,
    @ColumnInfo(name = "state")
    var state: Int
)

@SuppressLint("SimpleDateFormat")
fun Message.getFormattedDate(): String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(date))
