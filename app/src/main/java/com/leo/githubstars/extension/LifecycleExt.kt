package com.leo.githubstars.extension

import androidx.lifecycle.Lifecycle
import com.leo.githubstars.util.AutoClearedDisposable

operator fun Lifecycle.plusAssign(observer: AutoClearedDisposable)
        = this.addObserver(observer)
