package net.yuuzu.expenseapp.main_feature.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.yuuzu.expenseapp.main_feature.data.util.CategoryColor
import net.yuuzu.expenseapp.main_feature.domain.repository.StoreSettingRepository
import javax.inject.Inject

class StoreSettingRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : StoreSettingRepository {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

        private val DEFAULT_CATEGORY_COLOR_LIST = listOf(
            CategoryColor("Others", "#E1BEE7"),
            CategoryColor("Food", "#FFCDD2"),
            CategoryColor("Health", "#FF9574"),
            CategoryColor("Transportation", "#F0F4C3"),
            CategoryColor("Entertainment", "#B2DFDB"),
            CategoryColor("Shopping", "#BBDEFB"),
            CategoryColor("Subscription", "#7986CB"),
        )

        const val BUDGET_KEY = "budget"
        const val NAME_KEY = "name"
        const val CATEGORY_COLOR_KEY = "category_color_"
    }

    override suspend fun saveOneToStore(key: String, value: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun saveManyToStore(key: String, value: MutableList<String>) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = encode(value)
        }
    }

    override suspend fun saveCategoryColor(value: CategoryColor) {
        val key = stringPreferencesKey(CATEGORY_COLOR_KEY + value.category)
        context.dataStore.edit { preferences ->
            preferences[key] = value.color
        }
    }

    override fun getOneFromStore(key: String): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                return@map preferences[stringPreferencesKey(key)] ?: ""
            }
    }

    override fun getManyFromStore(key: String): Flow<MutableList<String>?> {
        return context.dataStore.data
            .map { preferences ->
                return@map decode(preferences[stringPreferencesKey(key)] ?: "")
            }
    }

    override fun getCategoryColor(): Flow<List<CategoryColor>?> {
        return context.dataStore.data
            .map { preferences ->
                val filterPreferences = preferences.asMap()
                    .filter { (key, _) -> key.name.startsWith(CATEGORY_COLOR_KEY) }

                if (filterPreferences.isEmpty()) {
                    DEFAULT_CATEGORY_COLOR_LIST.forEach { categoryColor ->  
                        saveCategoryColor(categoryColor)
                    }
                }

                preferences.asMap()
                    .filter { (key, _) -> key.name.startsWith(CATEGORY_COLOR_KEY) }
                    .mapNotNull { (key, value) ->
                        val category = key.name.removePrefix(CATEGORY_COLOR_KEY)
                        val color = value as? String ?: return@mapNotNull null
                        CategoryColor(category, color)
                    }
            }
    }

    private fun decode(storeValue: String): MutableList<String> {
        return if (storeValue.isEmpty()) {
            mutableListOf()
        } else {
            storeValue.split(",").toMutableList()
        }
    }

    private fun encode(value: MutableList<String>): String {
        return value.joinToString(separator = ",")
    }
}
