package com.percivalruiz.tawk.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.percivalruiz.tawk.data.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Query("SELECT * FROM note where id = :id")
    fun getNote(id: Long): Flow<Note?>
}