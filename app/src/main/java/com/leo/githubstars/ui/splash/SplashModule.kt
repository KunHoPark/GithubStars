package com.leo.githubstars.ui.splash

import com.leo.githubstars.data.repository.AuthRepository
import com.leo.githubstars.di.scope.ActivityScoped
import com.leo.githubstars.di.scope.FragmentScoped
import com.leo.githubstars.util.LeoSharedPreferences
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

/**
 * SplashActivity의 subComponet.
 * @author LeoPark
 **/
@Module
abstract class SplashModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun splashFragment(): SplashFragment

    @Module
    companion object {
        @Provides
        @ActivityScoped
        @JvmStatic fun provideViewModelFactory(authRepository: AuthRepository, leoSharedPreferences: LeoSharedPreferences) : SplashViewModelFactory
                = SplashViewModelFactory(authRepository, leoSharedPreferences)
    }

}
