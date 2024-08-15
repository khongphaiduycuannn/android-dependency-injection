package com.notmyexample.hilt.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface LogDao {

    @Query("SELECT * FROM logs")
    fun getAllLogs(): LiveData<List<Log>>

    @Insert
    fun addLog(log: Log)

    @Query("DELETE FROM logs")
    fun removeLogs()
}