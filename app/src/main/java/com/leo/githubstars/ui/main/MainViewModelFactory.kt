package com.leo.githubstars.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.leo.githubstars.data.repository.RemoteRepository

class MainViewModelFactory(private val remoteRepository: RemoteRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(remoteRepository) as T
    }

}

