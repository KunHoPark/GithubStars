package com.leo.githubstars.di.module;

import com.leo.githubstars.di.scope.ActivityScoped
import com.leo.githubstars.ui.detail.DetailActivity
import com.leo.githubstars.ui.detail.DetailModule
import com.leo.githubstars.ui.main.MainActivity
import com.leo.githubstars.ui.main.MainModule
import com.leo.githubstars.ui.splash.SplashActivity
import com.leo.githubstars.ui.splash.SplashModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * RepositoryModule
 * AppComponent에 연결 된다.
 * @author LeoPark
 **/
@Module
abstract class ActivityModule {

    // ContributesAndroidInjector 어노테이션을 달고 반환 타입을 통해 해당 Activity의 Subcomponent를 생성 한다.
    @ActivityScoped
    @ContributesAndroidInjector(modules = [(MainModule::class)])
    abstract fun mainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [(SplashModule::class)])
    abstract fun splashActivity(): SplashActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [(DetailModule::class)])
    abstract fun detailActivity(): DetailActivity

}
