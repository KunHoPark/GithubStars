package com.leo.githubstars.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * 리스트의 더보기를 처리한다.
 * 1번 째 인자 값(고차함수)를 통해 더보기시 실행할 함수를 호출한다.
 * @author LeoPark
 **/
class InfiniteScrollListener(
    val func: () -> Unit,
    val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

  var previousTotal = 0
  private var loading = true
  private var visibleThreshold = 2
  private var firstVisibleItem = 0
  private var visibleItemCount = 0
  private var totalItemCount = 0

  override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
    super.onScrolled(recyclerView, dx, dy)

    if (dy > 0) {
      visibleItemCount = recyclerView.childCount
      totalItemCount = layoutManager.itemCount
      firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

      if (loading) {
        if (totalItemCount >= previousTotal) {
          loading = false
          previousTotal = totalItemCount
        }
      }
      if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
        // End has been reached
        func()
        loading = true
      }
    }
  }
}