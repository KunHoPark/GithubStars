package com.leo.githubstars.ui.base

import androidx.lifecycle.ViewModel
import com.leo.githubstars.util.AutoClearedDisposable
import io.reactivex.subjects.PublishSubject

/**
 * ViwModel의 Base class.
 * @author LeoPark
 **/
open abstract class BaseViewModel: ViewModel() {

   lateinit var viewDisposables: AutoClearedDisposable
   val message: PublishSubject<String> = PublishSubject.create()

}