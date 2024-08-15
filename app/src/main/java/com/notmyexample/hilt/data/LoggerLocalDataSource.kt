package com.notmyexample.hilt.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "LoggerLocalDataSource"

@Singleton
class LoggerLocalDataSource @Inject constructor(
    private val logDao: LogDao
) : LoggerDataSource {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun getAllLogs(): LiveData<List<Log>> = logDao.getAllLogs()

    override fun addLog(msg: String) {
        scope.launch { logDao.addLog(Log(msg, System.currentTimeMillis())) }
    }

    override fun removeLogs() {
        scope.launch { logDao.removeLogs() }
    }
}