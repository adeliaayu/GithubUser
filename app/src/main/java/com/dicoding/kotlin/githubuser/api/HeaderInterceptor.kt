package com.dicoding.kotlin.githubuser.api

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "token f935652ba69c7d793d09507edd7b70be887c1453")
            .build()
        return chain.proceed(request)
    }
}