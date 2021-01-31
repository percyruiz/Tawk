package com.percivalruiz.tawk.di

import android.util.Log
import androidx.room.Room
import com.bumptech.glide.Glide
import com.percivalruiz.tawk.data.GithubService
import com.percivalruiz.tawk.db.AppDatabase
import com.percivalruiz.tawk.repository.Repository
import com.percivalruiz.tawk.repository.RepositoryImpl
import com.percivalruiz.tawk.ui.profile.ProfileViewModel
import com.percivalruiz.tawk.ui.user_list.UserListViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Handles dependency injection using Koin
 */
val appModule = module {

    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single {
        val logger = HttpLoggingInterceptor { Log.d("API", it) }
        logger.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }

    single<GithubService> {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
        retrofit.create(GithubService::class.java)
    }

    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java, "github"
        ).build()
    }

    single {
        val db = get<AppDatabase>()
        db.userDao()
    }

    single<Repository> {
        RepositoryImpl(
            service = get(),
            db = get()
        )
    }

    viewModel {
        UserListViewModel(
            handle = get(),
            repository = get()
        )
    }

    viewModel {
        ProfileViewModel(
            repository = get()
        )
    }


    single {
        Glide.with(androidContext())
    }
}