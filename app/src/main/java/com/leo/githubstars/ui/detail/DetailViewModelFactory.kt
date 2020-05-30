package com.leo.githubstars.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.leo.githubstars.data.repository.AuthRepository
import com.leo.githubstars.data.repository.RemoteRepository

class DetailViewModelFactory(private val remoteRepository: RemoteRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return DetailViewModel(remoteRepository) as T
    }

}

