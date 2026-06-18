package mx.utng.utngrunner.data.datasource

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PreferencesDataSource(private val context: Context) {
 
    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("utng_runner_prefs") }
    )
 
    private object Keys {
        val HIGH_SCORE = intPreferencesKey("high_score")
    }
 
    suspend fun getHighScore(): Int =
        dataStore.data.map { it[Keys.HIGH_SCORE] ?: 0 }.first()
 
    suspend fun saveHighScore(score: Int) {
        dataStore.edit { prefs -> prefs[Keys.HIGH_SCORE] = score }
    }
}
