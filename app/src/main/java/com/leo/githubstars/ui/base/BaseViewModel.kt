package com.leo.githubstars.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.subjects.PublishSubject

open abstract class BaseViewModel: ViewModel() {

   val message: PublishSubject<String> = PublishSubject.create()

}