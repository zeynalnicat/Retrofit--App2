package com.matrix.android_104_android.retrofit

import com.matrix.android_104_android.model.product.Products
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ProductApi {

    @GET("auth/products")
    suspend fun getProducts(@Header("Authorization") token: String): Response<Products>

    @GET("auth/products/categories")
    suspend fun getCategories(@Header("Authorization") token: String): Response<List<String>>

    @GET("auth/products/category/{category}")
    suspend fun getSpecific(
        @Path("category") category: String,
        @Header("Authorization") token: String
    ): Response<Products>

}