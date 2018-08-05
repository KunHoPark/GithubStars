package com.leo.githubstars.di.module;

import android.app.Application
import android.arch.persistence.room.Room
import com.leo.githubstars.data.local.BookmarkRoomDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
* LocalDataModule
 * AppComponent에 연결 된다.
* @author KunHoPark
* @since 2018. 7. 30. PM 2:07
**/
@Module
class LocalDataModule {

    @Provides
    @Singleton
    fun provideBookmarkDatabase(application : Application): BookmarkRoomDatabase
            = Room.databaseBuilder(application, BookmarkRoomDatabase::class.java, "github_bookmark_user.db")
                .allowMainThreadQueries()
                .build()
}
