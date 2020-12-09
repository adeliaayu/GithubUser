package com.dicoding.kotlin.githubuser.api

import com.dicoding.kotlin.githubuser.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
            .build()
        return chain.proceed(request)
    }
}