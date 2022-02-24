package com.globalhiddenodds.pulseship.datasource.database

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.io.ByteArrayOutputStream
import java.util.*


@Entity(tableName = "owner")
data class Owner(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "uid")
    val uid: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "photo")
    var photoB64: String,
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "token")
    val token: String
)

@SuppressLint("SimpleDateFormat")
fun Owner.getFormattedDate(): String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(date))
