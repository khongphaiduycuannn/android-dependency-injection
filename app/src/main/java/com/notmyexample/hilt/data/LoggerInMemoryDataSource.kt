package com.notmyexample.hilt.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggerInMemoryDataSource @Inject constructor() : LoggerDataSource {

    private val logs = mutableListOf<Log>()

    private val _logsLiveData = MutableLiveData(listOf<Log>())

    override fun getAllLogs(): LiveData<List<Log>> = _logsLiveData

    override fun addLog(msg: String) {
        logs.add(Log(msg, System.currentTimeMillis()))
        _logsLiveData.value = logs
    }

    override fun removeLogs() {
        logs.clear()
        _logsLiveData.value = logs
    }
}