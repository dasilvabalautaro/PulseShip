package com.globalhiddenodds.pulseship.datasource.database

import androidx.room.ColumnInfo

data class SSetCountNewMessages(@ColumnInfo(name = "source")
                                val source: String,
                                @ColumnInfo(name = "name")
                                var name: String,
                                @ColumnInfo(name = "photo")
                                var photo: String,
                                @ColumnInfo(name = "quantity") var quantity: Int) {
}