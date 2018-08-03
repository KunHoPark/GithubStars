package com.leo.githubstars.ui.splash

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.leo.githubstars.data.repository.RemoteRepository

class SplashViewModelFactory(private val remoteRepository: RemoteRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SplashViewModel(remoteRepository) as T
    }

}

