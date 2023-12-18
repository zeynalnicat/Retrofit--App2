package com.matrix.android_104_android.retrofit

import com.matrix.android_104_android.model.users.LoginRequest
import com.matrix.android_104_android.model.users.UserDetail
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("auth/login")
    suspend fun getToken(@Body loginRequest: LoginRequest): Response<UserDetail>
}