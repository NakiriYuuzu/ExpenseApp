package net.yuuzu.expenseapp.main_feature.domain.repository

import kotlinx.coroutines.flow.Flow
import net.yuuzu.expenseapp.main_feature.data.util.CategoryColor

interface StoreSettingRepository {
    suspend fun saveOneToStore(key: String, value: String)

    suspend fun saveManyToStore(key: String, value: MutableList<String>)

    suspend fun saveCategoryColor(value: CategoryColor)

    fun getOneFromStore(key: String): Flow<String>

    fun getManyFromStore(key: String): Flow<MutableList<String>?>

    fun getCategoryColor(): Flow<List<CategoryColor>?>
}