import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.vibechat.constants.CONSTANTS
import com.example.vibechat.datastore.AppDataStore
import kotlinx.coroutines.flow.first

class AppDataStoreImpl(
    private val dataStore: DataStore<Preferences>
) : AppDataStore {

    override suspend fun getToken(): String {
        return dataStore.data.first()[CONSTANTS.TOKEN_KEY].orEmpty()
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[CONSTANTS.TOKEN_KEY] = token
        }
    }

    override suspend fun getUserId(): String {
        return dataStore.data.first()[CONSTANTS.USER_ID].orEmpty()
    }

    override suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[CONSTANTS.USER_ID] = userId
        }
    }

    override suspend fun clearUserId() {
        dataStore.edit { preferences ->
            preferences.remove(CONSTANTS.USER_ID)
        }
    }

    override suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(CONSTANTS.TOKEN_KEY)
        }
    }

    override suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
