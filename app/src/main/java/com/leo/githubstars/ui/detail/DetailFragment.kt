package com.leo.githubstars.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.databinding.DetailFragmentBinding
import com.leo.githubstars.di.scope.ActivityScoped
import com.leo.githubstars.ui.base.BaseFragment
import com.leo.githubstars.util.Constants
import javax.inject.Inject

/**
 * Detail화면 Fragment.
 * @author LeoPark
 **/
@ActivityScoped
class DetailFragment @Inject constructor() : BaseFragment() {
    internal val tag = this.javaClass.simpleName

    lateinit var viewModel: DetailViewModel
    private lateinit var viewDataBinding: DetailFragmentBinding
    @Inject lateinit var viewModelFactory: DetailViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewDataBinding = DetailFragmentBinding.inflate(inflater, container, false)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)
        viewModel.viewDisposables = viewDisposables
        viewDataBinding.also {
            it.viewModel = viewModel
            it.lifecycleOwner = activity
        }

        subscribe()
        loadData()
    }


    private fun loadData() {
        onActivityList?.let {
            it.onIntent().getSerializableExtra(Constants.INTENT_ACTION_KEY_USERDATA )?.let { userData ->
                if (userData is UserData) {
                    viewModel.loadData(userData)
                }
            }?:let { activity!!.finish() }
        }
    }

    /**
     * ViewModel로 부터 전달 되는 이벤트 들을 관리 한다. ex) observe, liveData 등
     */
    override fun subscribe() {
        with(viewModel) {

        }
    }
}
