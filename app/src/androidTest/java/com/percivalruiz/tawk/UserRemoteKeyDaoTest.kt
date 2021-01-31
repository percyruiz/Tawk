package com.percivalruiz.tawk

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.percivalruiz.tawk.data.Note
import com.percivalruiz.tawk.data.UserRemoteKey
import com.percivalruiz.tawk.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class UserRemoteKeyDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        db.close()
    }

    @Test
    fun insertAndPeekKey() = runBlockingTest {
        val key = UserRemoteKey(nextPageKey = 1)
        db.keyDao().insert(key)
        val keyDb = async {
            db.keyDao().peek()
        }

        keyDb.await().let {
            Assert.assertEquals(key.nextPageKey, it?.nextPageKey)
            Assert.assertEquals(key.uid, it?.uid)
        }
    }

    @Test
    fun deleteKey() = runBlockingTest {
        val key = UserRemoteKey(nextPageKey = 1)
        db.keyDao().insert(key)
        var keyDb = async {
            db.keyDao().peek()
        }

        keyDb.await().let {
            Assert.assertEquals(key.nextPageKey, it?.nextPageKey)
            Assert.assertEquals(key.uid, it?.uid)
        }

        db.keyDao().nukeKey()
        keyDb = async {
            db.keyDao().peek()
        }

        Assert.assertEquals(null, keyDb.await())
    }
}
