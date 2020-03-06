package com.leo.githubstars.di.module;

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * AppModule
 * AppComponent에 연결 된다.
 * @author LeoPark
 **/
@Module
class AppModule {

    // Context 타입의 의존성 객체를 생성한다.
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

}

