package com.leo.githubstars.di.module;


import com.leo.githubstars.data.local.WalletRoomDatabase
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
 * @author KunHoPark
 * @since 2018. 7. 30. PM 2:07
 **/
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRemoteRepository( @Named("authorized") authRestAdapter: Retrofit, walletRoomDatabase: WalletRoomDatabase): RemoteRepository =
            RemoteRepository(authRestAdapter.create(RemoteApi::class.java))

    @Provides
    @Singleton
    fun provideAuthRepository(@Named("unauthorized") unauthRestAdapter: Retrofit): AuthRepository =
            AuthRepository(unauthRestAdapter.create(RemoteApi::class.java) )


}
