package com.leo.githubstars.util

import com.leo.githubstars.BuildConfig
import timber.log.Timber

/**
 * Timber lib.를 통해 log처리 하기위한 util class.
 * @author LeoPark
 **/
object LeoLog {
    var logTag = "GithubStars"

    /** Log a verbose message with optional format args.  */
    @JvmStatic fun v(message: String, vararg args: Any) {
        if (BuildConfig.DEBUG) {
            Timber.tag(logTag)
            Timber.v(message, *args)
        }
    }

    /** Log a verbose exception and a message with optional format args.  */
    @JvmStatic fun v(t: Throwable, message: String, vararg args: Any) {
        if (BuildConfig.DEBUG) {
            Timber.tag(logTag)
            Timber.v(t, message, *args)
        }
    }

    /** Log a debug message with optional format args.  */
    @JvmStatic fun d(message: String, vararg args: Any) {
        if (BuildConfig.DEBUG) {
            Timber.tag(logTag)
            Timber.d(message, *args)
        }
    }

    /** Log a debug exception and a message with optional format args.  */
    @JvmStatic fun d(t: Throwable, message: String, vararg args: Any) {
        if (BuildConfig.DEBUG) {
            Timber.tag(logTag)
            Timber.d(t, message, *args)
        }
    }

    /** Log an info message with optional format args.  */
    @JvmStatic fun i(message: String, vararg args: Any) {
        Timber.tag(logTag)
        Timber.i(message, *args)
    }

    @JvmStatic fun i(message1: String, message2: String="") {
        Timber.tag(logTag)
        Timber.i("$message1 $message2")
    }

    /** Log an info exception and a message with optional format args.  */
    @JvmStatic fun i(t: Throwable, message: String, vararg args: Any) {
        if (BuildConfig.DEBUG) {
            Timber.tag(logTag)
            Timber.i(t, message, *args)
        }
    }

    /** Log a warning message with optional format args.  */
    @JvmStatic fun w(message: String, vararg args: Any) {
        if (BuildConfig.DEBUG) {
            Timber.tag(logTag)
            Timber.w(message, *args)
        }
    }

    /** Log a warning exception and a message with optional format args.  */
    @JvmStatic fun w(t: Throwable, message: String, vararg args: Any) {
        if (BuildConfig.DEBUG) {
            Timber.tag(logTag)
            Timber.w(t, message, *args)
        }
    }

    /** Log an error message with optional format args.  */
    @JvmStatic fun e(message: String, vararg args: Any) {
        if (BuildConfig.DEBUG) {
            Timber.tag(logTag)
            Timber.e(message, *args)
        }
    }

    /** Log an error exception and a message with optional format args.  */
    @JvmStatic fun e(t: Throwable, message: String, vararg args: Any) {
        if (BuildConfig.DEBUG) {
            Timber.tag(logTag)
            Timber.e(t, message, *args)
        }
    }

    /** Log an assert message with optional format args.  */
    @JvmStatic fun wtf(message: String, vararg args: Any) {
        if (BuildConfig.DEBUG) {
            Timber.tag(logTag)
            Timber.wtf(message, *args)
        }
    }

    /** Log an assert exception and a message with optional format args.  */
    @JvmStatic fun wtf(t: Throwable, message: String, vararg args: Any) {
        if (BuildConfig.DEBUG) {
            Timber.tag(logTag)
            Timber.wtf(t, message, *args)
        }
    }

    /** Log at `priority` a message with optional format args.  */
    @JvmStatic fun log(priority: Int, message: String, vararg args: Any) {
        if (BuildConfig.DEBUG) {
            Timber.tag(logTag)
            Timber.log(priority, message, *args)
        }
    }

    /** Log a debug message with optional format localTag,args.  */
    @JvmStatic fun log(priority: Int, localTag: String, message: String, vararg args: Any) {
        if (BuildConfig.DEBUG) {
            Timber.tag(logTag)
            Timber.log(priority, "$localTag: $message", *args)
        }
    }

    /** Log at `priority` an exception and a message with optional format args.  */
    @JvmStatic fun log(priority: Int, t: Throwable, message: String, vararg args: Any) {
        if (BuildConfig.DEBUG) {
            Timber.tag(logTag)
            Timber.log(priority, t, message, *args)
        }
    }
}