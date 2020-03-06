package ccom.leo.githubstars.ui.base

import com.leo.githubstars.ui.base.BaseFragment
import com.leo.githubstars.ui.main.MainViewModel

/**
 * Main화면의 TabFragment의 Base class
 * @author LeoPark
 **/
open abstract class BaseTabFragment: BaseFragment() {

    protected var viewModel: MainViewModel?= null

}