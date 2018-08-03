package com.leo.githubstars.application

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.facebook.stetho.Stetho
import com.leo.githubstars.di.component.AppComponent
import com.leo.githubstars.di.component.DaggerAppComponent
import dagger.android.support.DaggerApplication
import timber.log.Timber

/**
 * DaggerApplication를 상속받고, AppComponent에서 정의한 Builder를 활용하여 Component와 연결 한다.
 */
class MyGithubApp : DaggerApplication() {
    companion object {
        val appComponent: AppComponent by lazy { DaggerAppComponent.builder()
                .application(MyGithubApp.instance!!)
                .build() }

        private var instance: MyGithubApp? = null
        @JvmStatic fun applicationContext() : Context = instance!!.applicationContext
        @JvmStatic fun application() : Application = instance!!

        fun resources() : Resources = instance!!.applicationContext.resources
    }

    override fun applicationInjector() = appComponent

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        Timber.uprootAll()
        Timber.plant(Timber.DebugTree())

        Stetho.initializeWithDefaults(this)
    }

}
