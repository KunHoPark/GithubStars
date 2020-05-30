package com.leo.githubstars.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.leo.githubstars.data.repository.AuthRepository
import com.leo.githubstars.util.LeoSharedPreferences

class SplashViewModelFactory(
    private val authRepository: AuthRepository,
    private val leoSharedPreferences: LeoSharedPreferences): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SplashViewModel(authRepository, leoSharedPreferences) as T
    }

}

