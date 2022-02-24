package com.globalhiddenodds.pulseship.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globalhiddenodds.pulseship.datasource.database.Message
import com.globalhiddenodds.pulseship.datasource.database.SSetCountNewMessages
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: Message)

    @Query("SELECT * FROM message WHERE source LIKE :source OR target LIKE :source ORDER BY date ASC")
    fun getMessageOfContact(source: String): Flow<List<Message>>

    @Query("SELECT * FROM message ORDER BY date ASC")
    fun getMessages(): Flow<List<Message>>

    @Query("UPDATE message SET state = 1 WHERE source LIKE :source AND state == 0")
    fun updateStateMessages(source: String)

    @Query("DELETE FROM message WHERE source LIKE :source OR target LIKE :source")
    fun deleteMessagesOfContact(source: String)

    @Query("SELECT source, name, photo, SUM(state) AS quantity FROM message WHERE source NOT LIKE :currentSource GROUP BY source ORDER BY date ASC")
    fun getCountNewMessages(currentSource: String): Flow<List<SSetCountNewMessages>>

}