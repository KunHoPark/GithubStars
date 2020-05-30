package com.leo.githubstars.di.module;

import com.leo.githubstars.application.MyGithubStarsApp
import com.leo.githubstars.data.remote.api.AuthInterceptor
import com.leo.githubstars.util.Constants
import com.leo.githubstars.util.LeoSharedPreferences
import com.leo.githubstars.util.RetrofitLogger
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


/**
 * NetworkDataModule
 * AppComponent에 연결 된다.
 * @author LeoPark
 **/
@Module
class NetworkDataModule {

    @Provides
    @Named("unauthorized")
    @Singleton
    fun provideUnauthorizedOkHttpClient(@Named("unAuthOkHttpClient") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().client(okHttpClient).baseUrl("https://github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Named("authorized")
    @Singleton
    fun provideAuthRestAdapter(@Named("authOkHttpClient") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().client(okHttpClient).baseUrl(Constants.GITHUB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

    }

    @Provides
    @Named("unAuthOkHttpClient")
    @Singleton
    fun providesUnAuthOkHttpClient(): OkHttpClient {

        val builder = OkHttpClient.Builder().apply {
            readTimeout(Constants.READ_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS)
            writeTimeout(Constants.WRITE_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS)
            connectTimeout(Constants.CONNECT_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS)
            addInterceptor(HttpLoggingInterceptor(RetrofitLogger(Constants.HTTP_LOGGING_PRETTY_PRINTING_ENABLE)).apply {
                level = HttpLoggingInterceptor.Level.BODY
                HttpLoggingInterceptor.Level.HEADERS

            })
        }

        return builder.build()
    }

    @Provides
    @Named("authOkHttpClient")
    @Singleton
    fun providesOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {

        val builder = OkHttpClient.Builder().apply {
            readTimeout(Constants.READ_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS)
            writeTimeout(Constants.WRITE_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS)
            connectTimeout(Constants.CONNECT_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS)
            addInterceptor(authInterceptor)
            addInterceptor(HttpLoggingInterceptor(RetrofitLogger(Constants.HTTP_LOGGING_PRETTY_PRINTING_ENABLE)).apply {
                level = HttpLoggingInterceptor.Level.BODY
                HttpLoggingInterceptor.Level.HEADERS

            })
        }

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthInterceptor {
        val provider = LeoSharedPreferences(MyGithubStarsApp.context).getString(Constants.PREF_ACTION_KEY_AUTH_TOKEN)
        val token =  provider ?: ""
        return AuthInterceptor(token)
    }

}
