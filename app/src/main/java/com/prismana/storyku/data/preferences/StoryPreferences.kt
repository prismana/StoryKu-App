package com.prismana.storyku.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.prismana.storyku.data.remote.response.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.concurrent.Volatile

val Context.datastore: DataStore<Preferences> by preferencesDataStore("session")

class StoryPreferences(private val dataStore: DataStore<Preferences>) {

    // login save credentials
    suspend fun saveSession(user: LoginResponse.LoginResult) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name.toString()
            preferences[USERID_KEY] = user.userId.toString()
            preferences[TOKEN_KEY] = user.token.toString()
            preferences[IS_LOGIN] = true
        }
    }

    fun getSession(): Flow<LoginResponse.LoginResult> {
        return dataStore.data.map { preferences ->
            LoginResponse.LoginResult(
                preferences[NAME_KEY] ?: "",
                preferences[USERID_KEY] ?: "",
                preferences[TOKEN_KEY]?: "",
                preferences[IS_LOGIN] ?: false
            )
        }
    }

    // for logout purpose
    suspend fun removeSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val NAME_KEY = stringPreferencesKey("name")
        private val USERID_KEY = stringPreferencesKey("userId")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN = booleanPreferencesKey("isLogin")

        @Volatile
        private var INSTANCE: StoryPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): StoryPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = StoryPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}