package app.suhasdissa.mellowmusic.backend.repository

import app.suhasdissa.mellowmusic.backend.models.Login
import app.suhasdissa.mellowmusic.backend.models.Token
import app.suhasdissa.mellowmusic.utils.RetrofitHelper

interface AuthRepository {
    suspend fun getAuthToken(login: Login): Token
}

class AuthRepositoryImpl : AuthRepository {
    private val pipedApi = RetrofitHelper.createPipedApi()
    override suspend fun getAuthToken(login: Login): Token = pipedApi.login(login)
}
