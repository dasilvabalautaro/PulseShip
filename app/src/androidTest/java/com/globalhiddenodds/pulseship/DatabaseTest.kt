package com.globalhiddenodds.pulseship


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.globalhiddenodds.pulseship.datasource.database.AppRoomDatabase
import com.globalhiddenodds.pulseship.datasource.database.Owner
import com.globalhiddenodds.pulseship.repository.database.OwnerDao
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

import java.util.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class DatabaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var ownerDao: OwnerDao
    private lateinit var db: AppRoomDatabase

    @Before
    fun setup(){
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppRoomDatabase::class.java
        ).allowMainThreadQueries().build()
        ownerDao = db.ownerDao()
    }

    @After
    fun teardown(){
        db.close()
    }


    @Test
    fun insertOwner() = runTest {
        val owner = Owner(0, "123qwe", "Pico Palotes",
            "email@gmail.com", "",
            Date().time.milliseconds.toLong(DurationUnit.MILLISECONDS),
            "123serewttewrgrtgrth")
        ownerDao.insert(owner)
        val ownerInserted = ownerDao.getOwner("123qwe")

        assertThat(ownerInserted.uid).isEqualTo("123qwe")

    }

    @Test
    fun updateToken() = runTest {
        val owner = Owner(0, "123qwe", "Pico Palotes",
            "email@gmail.com", "",
            Date().time.milliseconds.toLong(DurationUnit.MILLISECONDS),
            "123serewttewrgrtgrth")
        ownerDao.insert(owner)

        ownerDao.updateToken("123qwe", "1234567890")

        val ownerUpdate = ownerDao.getOwner("123qwe")
        assertThat(ownerUpdate.token).isEqualTo("1234567890")
    }

    @Test
    fun update() = runTest {
        val owner = Owner(0, "123qwe", "Pico Palotes",
            "email@gmail.com", "",
            Date().time.milliseconds.toLong(DurationUnit.MILLISECONDS),
            "123serewttewrgrtgrth")
        ownerDao.insert(owner)
        val ownerUpdate = ownerDao.getOwner("123qwe")
        ownerUpdate.name = "Arturo Silva"
        ownerDao.update(ownerUpdate)
        val ownerName = ownerDao.getOwner("123qwe")

        assertThat(ownerName.name).isEqualTo("Arturo Silva")
    }

    @Test
    fun getUniqueUser() = runTest {
        val owner = ownerDao.getUniqueUser()
        assertThat(owner).isNull()
    }
}