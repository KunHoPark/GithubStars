package com.leo.githubstars.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.subjects.PublishSubject

/**
 * ViwModel의 Base class.
 * @author LeoPark
 **/
open abstract class BaseViewModel: ViewModel() {

   val message: PublishSubject<String> = PublishSubject.create()

}