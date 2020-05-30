package com.leo.githubstars.di.module


import android.content.Context
import com.leo.githubstars.util.LeoSharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * RepositoryModule
 * AppComponent에 연결 된다.
 * @author parkkh
 * @since 2019. 9. 3
 **/
@Module
class UtilModule {

    @Provides
    @Singleton
    fun provideLeoSharedPreferences( context: Context): LeoSharedPreferences =
        LeoSharedPreferences(context)


}
