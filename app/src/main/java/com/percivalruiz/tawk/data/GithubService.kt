package com.percivalruiz.tawk.data

import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {

    @GET("/users")
    suspend fun getUserList(@Query("since") since: Int): List<User>
}