package app.suhasdissa.vibeyou.backend.repository

import app.suhasdissa.vibeyou.backend.models.Login
import app.suhasdissa.vibeyou.backend.models.Token
import app.suhasdissa.vibeyou.utils.RetrofitHelper

interface AuthRepository {
    suspend fun getAuthToken(login: Login): Token
}

class AuthRepositoryImpl : AuthRepository {
    private val pipedApi = RetrofitHelper.createPipedApi()
    override suspend fun getAuthToken(login: Login): Token = pipedApi.login(login)
}
