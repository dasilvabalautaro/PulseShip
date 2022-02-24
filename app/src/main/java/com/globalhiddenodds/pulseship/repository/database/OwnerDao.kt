package com.globalhiddenodds.pulseship.repository.database

import androidx.room.*
import com.globalhiddenodds.pulseship.datasource.database.Owner

@Dao
interface OwnerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(owner: Owner)

    @Update
    suspend fun update(owner: Owner)

    @Query("SELECT * FROM owner WHERE uid = :uid")
    suspend fun getOwner(uid: String): Owner

    @Query("UPDATE owner SET token = :token WHERE uid = :uid")
    suspend fun updateToken(uid: String, token: String)

    @Query("SELECT * FROM owner ORDER BY id DESC LIMIT 1")
    suspend fun getUniqueUser(): Owner
}