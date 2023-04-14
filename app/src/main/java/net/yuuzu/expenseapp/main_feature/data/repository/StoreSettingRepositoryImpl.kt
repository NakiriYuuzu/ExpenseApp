package net.yuuzu.expenseapp.main_feature.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.yuuzu.expenseapp.main_feature.domain.repository.StoreSettingRepository
import javax.inject.Inject

class StoreSettingRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : StoreSettingRepository {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        const val BUDGET_KEY = "budget"
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
