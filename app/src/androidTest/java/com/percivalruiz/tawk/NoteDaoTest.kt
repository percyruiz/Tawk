package com.percivalruiz.tawk

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.percivalruiz.tawk.data.Note
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
class NoteDaoTest {

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
    fun insertAndGetNote() = runBlockingTest {
        val note = Note(1, "note")
        db.noteDao().insert(note)
        val noteDb = async {
            db.noteDao().getNote(1).take(1)
        }

        noteDb.await().collect {
            Assert.assertEquals(it?.content, note.content)
        }
    }

    @Test
    fun getAllNotes() = runBlockingTest {
        val notes = listOf(
            Note(1, "note1"),
            Note(2, "note2"),
            Note(3, "note3")
        )
        for(note in notes) {
            db.noteDao().insert(note)
        }
        val noteDb = async {
            db.noteDao().getAllNotes().take(3)
        }

        noteDb.await().let {
            Assert.assertEquals(3, it.size)
            Assert.assertEquals("note1", it.first().content)
        }

    }

}