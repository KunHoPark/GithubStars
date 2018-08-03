package com.leo.githubstars.ui.splash

import com.leo.githubstars.data.repository.RemoteRepository
import com.leo.githubstars.di.scope.ActivityScoped
import com.leo.githubstars.di.scope.FragmentScoped
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class SplashModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun splashFragment(): SplashFragment

    @Module
    companion object {
        @Provides
        @ActivityScoped
        @JvmStatic fun provideViewModelFactory(remoteRepository: RemoteRepository) : SplashViewModelFactory
                = SplashViewModelFactory(remoteRepository)
    }

}
