package ru.netology.nework.auth

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.netology.nework.model.Token
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val keyId = "id"
    private val keyToken = "token"

    private val _authStateFlow: MutableStateFlow<Token>

    init {
        val id = prefs.getLong(keyId, 0)
        val token = prefs.getString(keyToken, null)

        if (id == 0L || token == null) {
            _authStateFlow = MutableStateFlow(Token())
            with(prefs.edit()) {
                clear()
                apply()
            }
        } else {
            _authStateFlow = MutableStateFlow(Token(id, token))
        }
    }

    val authStateFlow: StateFlow<Token> = _authStateFlow.asStateFlow()

    @Synchronized
    fun setAuth(id: Long, token: String) {
        _authStateFlow.value = Token(id, token)
        with(prefs.edit()) {
            putLong(keyId, id)
            putString(keyToken, token)
            commit()
        }
    }

    @Synchronized
    fun removeAuth() {
        _authStateFlow.value = Token()
        with(prefs.edit()) {
            clear()
            commit()
        }
    }
}
