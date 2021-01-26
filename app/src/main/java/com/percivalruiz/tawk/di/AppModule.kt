package com.percivalruiz.tawk.di

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.savedstate.SavedStateRegistryOwner
import com.percivalruiz.tawk.data.GithubService
import com.percivalruiz.tawk.data.User
import com.percivalruiz.tawk.repository.GithubRepository
import com.percivalruiz.tawk.repository.RemoteRepository
import com.percivalruiz.tawk.ui.user_list.UserListViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {

    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single<GithubService> {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
        retrofit.create(GithubService::class.java)
    }

    single<GithubRepository> {
        RemoteRepository(
            service = get()
        )
    }

    single {
        UserListViewModel(
            handle = get(),
            repository = get()
        )
    }
}