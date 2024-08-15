package com.notmyexample.hilt.data

import androidx.lifecycle.LiveData

interface LoggerDataSource {

    fun getAllLogs(): LiveData<List<Log>>

    fun addLog(msg: String)

    fun removeLogs()
}