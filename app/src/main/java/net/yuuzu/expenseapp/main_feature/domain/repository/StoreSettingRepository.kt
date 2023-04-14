package net.yuuzu.expenseapp.main_feature.domain.repository

import kotlinx.coroutines.flow.Flow

interface StoreSettingRepository {

    suspend fun saveOneToStore(key: String, value: String)

    suspend fun saveManyToStore(key: String, value: MutableList<String>)

    fun getOneFromStore(key: String): Flow<String>

    fun getManyFromStore(key: String): Flow<MutableList<String>?>
}