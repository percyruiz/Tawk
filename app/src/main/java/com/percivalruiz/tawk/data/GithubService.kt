package com.percivalruiz.tawk.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    @GET("/users")
    suspend fun getUserList(@Query("since") since: Int?): List<User>

    @GET("/users/{login}")
    suspend fun getUserProfile(@Path("login") login: String): UserProfile
}