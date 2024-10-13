package fr.eseo.ld.android.ac.colourcloud.model

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import fr.eseo.ld.android.ac.colourcloud.ui.state.GameUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "what_colour")

class WhatColourDataStore(private val context: Context) {
    companion object {
        val LOCAL_LAST_SCORE_KEY = intPreferencesKey("local_last_int_key")
        val LOCAL_HIGH_SCORE_KEY = intPreferencesKey("local_high_int_key")
    }

    val uiStateFlow: Flow<GameUiState> = context.dataStore.data
        .map { preferences ->
            val lastScore = preferences[LOCAL_LAST_SCORE_KEY] ?: 0
            val highScore = preferences[LOCAL_HIGH_SCORE_KEY] ?: 0
            GameUiState(lastScore = lastScore, localHighScore = highScore)
        }

    suspend fun saveScores(lastScore: Int, highScore: Int) {
        context.dataStore.edit { preferences ->
            preferences[LOCAL_LAST_SCORE_KEY] = lastScore
            preferences[LOCAL_HIGH_SCORE_KEY] = highScore
        }
    }
}

