package com.leo.githubstars.ui.main


import android.view.View
import android.widget.EditText
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.google.android.material.tabs.TabLayout
import com.jakewharton.rxbinding2.widget.RxTextView
import com.leo.githubstars.R
import com.leo.githubstars.callback.OnItemClickListener
import com.leo.githubstars.data.local.SearchData
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.data.repository.RemoteRepository
import com.leo.githubstars.event.SingleLiveEvent
import com.leo.githubstars.extension.hideKeyboard
import com.leo.githubstars.ui.base.BaseViewModel
import com.leo.githubstars.util.Constants
import com.leo.githubstars.util.InfiniteScrollListener
import com.leo.githubstars.util.LeoLog
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Main화면의 ViewModel
 * Main화면에서는 하나의 viewModel만 존재하며, 각 TabFragment(View)는 ViewModel를 통해 정보를 set/get 한다.
 * @author LeoPark
 **/
class MainViewModel
@Inject constructor(private val remoteRepository: RemoteRepository) : BaseViewModel() {
    internal val tag = this.javaClass.simpleName


    val githubUserData = ObservableArrayList<UserData>()
    val bookmarkUserData = ObservableArrayList<UserData>()
    val githubSearchWord = ObservableField<String>("")
    val bookmarkSearchWord = ObservableField<String>("")
    val isReloadGithubList = ObservableField<Boolean>(false)        // GithubTab list 갱신, Bookmark 변경
    val startDetailActivityLiveData = SingleLiveEvent<UserData>()

    private val bookmarkUserHash = HashMap<String, UserData>()
    private var totalCount = 0
    private var currentPage = 0
    private var beforeSearchWord: String? = null
    var scrollListener: InfiniteScrollListener?= null


    /**
     * BookmarkTabFragment의 search edittext의 이벤트 처리.
     */
    val onBookmarkEditText: (EditText)->Unit = { editText ->
        RxTextView.textChangeEvents(editText)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(300, TimeUnit.MILLISECONDS)
            .map {
                it.text().toString()
            }
            .flatMap {
               bookmarkSearchWord.set(it)
                remoteRepository.loadSearchDataFromDb(it).toObservable()
            }
            .subscribe(
                {
                    bookmarkUserData.clear()
                    bookmarkUserData.addAll(it)
                },
                {
                    message.onNext(it.localizedMessage)
                }
            )
            .apply {
                viewDisposables.add(this)
            }
    }

    /**
     * GithubTabFragment의 search edittext의 이벤트 처리.
     */
    val onGithubEditText: (EditText)->Unit = { editText ->
        RxTextView.textChangeEvents(editText)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(1000, TimeUnit.MILLISECONDS)
            .map { it.text().toString() }
            .filter {
                it.length >= 2
            }
            .map { searchWord ->

                // 이전에 입력 했던 키 값이 변경 되었으면 모든 값을 초기화 한다.
                LeoLog.i(tag, "loadSearchDataFromGithub beforeSearchWord=$beforeSearchWord,  searchWord=$searchWord")
                beforeSearchWord?.let {
                    if (it != searchWord) {
                        totalCount = 0
                        currentPage = 0
                        githubUserData.clear()
                    }
                }
                beforeSearchWord = searchWord

                // 더보기 값을 초기화 한다.
                scrollListener?.let {
                    it.previousTotal = 0
                }

                return@map searchWord
            }
            .subscribe(
                {
                    githubSearchWord.set(it)
                    onGithubSearchMoreData(it)
                },
                {
                    message.onNext(it.localizedMessage)
                }
            )
            .apply {
                viewDisposables.add(this)
            }
    }

    /**
     * GithubTagFragment 리스트의 더보기 할 때 다음 데이터를 github api를 통해 가져 온다.
     */
    val onGithubSearchMoreData: (String)->Unit = { searchWord ->
         Flowable.just(searchWord)
            .filter {
                //다음 페이지가 있는지를 확인 한다.
                totalCount > currentPage * Constants.PERPAGE || currentPage == 0
            }
            .flatMap { searchWord ->
                //Github 서버를 통해 값을 가져 온다.
                LeoLog.i(tag, "loadSearchDataFromGithub before server searchWord=$searchWord")
                remoteRepository.loadSearchDataFromGithub(
                    searchWord,
                    ++currentPage,
                    Constants.PERPAGE
                )
            }
             .map {
                 // 서버로 부터 가져온 데이터와 DB 값을 비교 후 bookmark를 표시 한다.
                 it.items
                     .map {
                         it.isBookmark = bookmarkUserHash[it.id] != null
                     }
                 return@map it
             }
             .subscribe(
                 {
                     githubSearchWord.set(searchWord)
                     LeoLog.i(tag, "loadSearchDataFromGithub githubSearchWord=${githubSearchWord.get()}, searchWord=$searchWord")
                     totalCount = it.totalCount.toInt()
                     githubUserData.addAll(it.items)     //리스트 갱신. AdapterBindings를 통해 업데이트 된다.

                     scrollListener?.let { listener->
                         listener.previousTotal = 0
                     }
                 },
                 {
                     message.onNext(it.localizedMessage)
                 }
             )
             .apply {
                 viewDisposables.add(this)
             }
    }


    val mergeGithubDataAndBookmarkData:(Observable<SearchData>, Observable<HashMap<String, UserData>>)->Unit = { githubData, bookmarkData ->

        Observable.combineLatest(githubData, bookmarkData,
            BiFunction<SearchData, HashMap<String, UserData>, Pair<SearchData, HashMap<String, UserData>>> { t1, t2 -> Pair(t1, t2) })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { pair ->
                // 서버로 부터 가져온 데이터와 DB 값을 비교 후 bookmark를 표시 한다.
                pair.first.items
                    .map {
                        it.isBookmark = pair.second[it.id] != null
                    }
                return@map pair
            }
            .subscribe(
                {
                    it?.let {
                        totalCount = it.first.totalCount.toInt()
                        githubUserData.addAll(it.first.items)     //리스트 갱신.
                    }
                },
                {
                    message.onNext(it.localizedMessage)
                }
            )
            .apply {
                viewDisposables.add(this)
            }
    }

    /**
     * GithubTabFragment에서 호출.
     * DB 정보가 갱신 되면 github api를 통해 가져온 UserData 중 bookmark 데이터도 같이 업데이트 한다.
     * 결론적으로 리스트에 북마크 아이콘 변경.
     */
     val updateBookmarkData: (List<UserData>)->Unit= { userDataFromDb ->
        synchronized(this) {

            Observable.just(userDataFromDb)
                .subscribeOn(Schedulers.io())
                .debounce(300, TimeUnit.MILLISECONDS)
                .map {
                    isReloadGithubList.set(false)
                    return@map it
                }
                .map {
                    // hash 값은 만들어 주자, 검색 할 때 사용 하기 때문.
                    bookmarkUserHash.clear()
                    it.forEach {
                        bookmarkUserHash[it.id] = it
                    }
                    return@map githubUserData
                }
                .filter {
                    githubUserData.size > 0
                }
                .map {
                    it.map { userData ->
                        userData?.let {
                            userData.isBookmark = bookmarkUserHash[userData.id] != null
                        }
                    }
                }
                .subscribe {
                    isReloadGithubList.set(true)
                }
                .apply {
                    viewDisposables.add(this)
                }

        }
    }

    /**
     * 리스트의 Listener
     */
    val onItemClickListener = object : OnItemClickListener {
        override fun onItemClick(item: UserData, view: View, position: Int) {
            LeoLog.i(tag, "onItemClick = ${item.url}, isBookmarked=${item.isBookmark}")
            view.hideKeyboard()

            when(view.id) {
                R.id.ivBookmark -> {
                    // Bookmark db에 저장 또는 삭제 처리 한다. bookmark 여부멩 따라 처리 된다.
                    when (item.isBookmark) {
                        true -> {
                            item.isBookmark = false
                            remoteRepository.deleteUserDataFromDb(item)
                        }
                        else -> {
                            item.isBookmark = true
                            remoteRepository.insertUserDataFromDb(item)
                        }
                    }
                }
                else -> {
                    startDetailActivityLiveData.value =
                    remoteRepository.getUserDataFromDbById(item)?.let {
                        it
                    }?:let {
                        item
                    }
                }
            }
        }
    }

    /**
     * Tablayout index 선택.
     */
    val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
        }
    }


    /**
     * Bookmark db 값이 변경 되면 호출된다.
     */
    val getUserData = remoteRepository.getUserDataFromDb()
}
