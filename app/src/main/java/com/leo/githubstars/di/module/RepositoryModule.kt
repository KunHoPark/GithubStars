package com.leo.githubstars.di.module;


import com.leo.githubstars.data.local.BookmarkRoomDatabase
import com.leo.githubstars.data.remote.api.RemoteApi
import com.leo.githubstars.data.repository.AuthRepository
import com.leo.githubstars.data.repository.RemoteRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

/**
 * RepositoryModule
 * AppComponent에 연결 된다.
 * @author LeoPark
 **/
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRemoteRepository( @Named("authorized") authRestAdapter: Retrofit, bookmarkRoomDatabase: BookmarkRoomDatabase): RemoteRepository =
            RemoteRepository(authRestAdapter.create(RemoteApi::class.java), bookmarkRoomDatabase.userDao())

    @Provides
    @Singleton
    fun provideAuthRepository(@Named("unauthorized") unauthRestAdapter: Retrofit): AuthRepository =
            AuthRepository(unauthRestAdapter.create(RemoteApi::class.java) )


}
