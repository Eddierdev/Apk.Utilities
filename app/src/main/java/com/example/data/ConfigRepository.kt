package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConfigRepository(private val configDao: ConfigDao) {
    fun getConfigFlow(key: String): Flow<String?> {
        return configDao.getConfigFlow(key).map { it?.value }
    }

    suspend fun getConfig(key: String): String? {
        return configDao.getConfig(key)?.value
    }

    suspend fun saveConfig(key: String, value: String) {
        configDao.insertConfig(ConfigEntity(key, value))
    }
}
