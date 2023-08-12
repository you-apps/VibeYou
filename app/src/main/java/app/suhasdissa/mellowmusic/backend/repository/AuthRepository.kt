package app.suhasdissa.mellowmusic.backend.repository

import app.suhasdissa.mellowmusic.backend.api.PipedApi
import app.suhasdissa.mellowmusic.backend.models.Login
import app.suhasdissa.mellowmusic.backend.models.Token

interface AuthRepository {
    suspend fun getAuthToken(login: Login): Token
}

class AuthRepositoryImpl(private val pipedApi: PipedApi) : AuthRepository {
    override suspend fun getAuthToken(login: Login): Token = pipedApi.login(login)
}
