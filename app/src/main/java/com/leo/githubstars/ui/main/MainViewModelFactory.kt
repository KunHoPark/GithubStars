package com.leo.githubstars.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.leo.githubstars.data.repository.RemoteRepository

class MainViewModelFactory(private val remoteRepository: RemoteRepository): ViewModelProvider.Factory {

    var viewModel: ViewModel?=null
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return viewModel?.let {
            viewModel as T
        }?: MainViewModel(remoteRepository).let {
            viewModel = it
            viewModel as T
        }
    }

}

