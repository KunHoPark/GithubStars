package com.leo.githubstars.ui.detail

import com.leo.githubstars.data.repository.AuthRepository
import com.leo.githubstars.data.repository.RemoteRepository
import com.leo.githubstars.di.scope.ActivityScoped
import com.leo.githubstars.di.scope.FragmentScoped
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

/**
 * DetailActivity의 subComponet.
 * @author LeoPark
 **/
@Module
abstract class DetailModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun detailFragment(): DetailFragment

    @Module
    companion object {
        @Provides
        @ActivityScoped
        @JvmStatic fun provideViewModelFactory(remoteRepository: RemoteRepository) : DetailViewModelFactory
                = DetailViewModelFactory(remoteRepository)
    }

}
