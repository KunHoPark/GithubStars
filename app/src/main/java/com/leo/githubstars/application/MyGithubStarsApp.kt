package com.leo.githubstars.application

import android.content.Context
import android.content.res.Resources
import com.facebook.stetho.Stetho
import com.leo.githubstars.di.component.AppComponent
import com.leo.githubstars.di.component.DaggerAppComponent
import timber.log.Timber

/**
 * DaggerApplication를 상속받고, AppComponent에서 정의한 Builder를 활용하여 Component와 연결 한다.
 * @author LeoPark
 */
class MyGithubStarsApp : MultidexDaggerApplication() {
    companion object {
        private var instance: MyGithubStarsApp? = null
        val appComponent: AppComponent by lazy { DaggerAppComponent.builder()
                .application(instance!!)
                .build() }
        @JvmStatic val context: Context by lazy { instance!!.applicationContext }
        @JvmStatic val application by lazy { instance!! }
        @JvmStatic val resource : Resources by lazy { instance!!.applicationContext.resources }
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
