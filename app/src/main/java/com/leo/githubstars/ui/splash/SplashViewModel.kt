package com.leo.githubstars.ui.splash


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import javax.inject.Inject

class SplashViewModel
@Inject constructor() : ViewModel() {
    internal val tag = this.javaClass.simpleName

    var isLoadingSuccess: MutableLiveData<Boolean> = MutableLiveData()

    fun loadCollections(){
        //Getty site로 부터 파서 후 이미지 정보 가져 오기.
//        gettyImageRepository.getCollections(true, object: OnLoadListener{
//            override fun onSuccess(gettyImageEntity: List<GettyImageEntity>?) {
//                isLoadingSuccess.postValue(true)
//            }
//
//            override fun onFail(error: String?) {
//                isLoadingSuccess.postValue(false)
//                LeoLog.e(tag, error!!)
//            }
//
//        })
    }

}
