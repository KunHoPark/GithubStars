package ccom.leo.githubstars.ui.base

import android.arch.lifecycle.ViewModel
import io.reactivex.subjects.PublishSubject

open abstract class BaseViewModel: ViewModel() {

   val message: PublishSubject<String> = PublishSubject.create()

}