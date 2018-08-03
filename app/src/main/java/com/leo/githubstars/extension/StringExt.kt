package com.konai.cryptox.kotlin.extension

import com.leo.githubstars.application.Constants


/**
 *  "?id="에 해당하는 value 리턴.
 */
fun String.subStringTagId(tag: String) : String {
    val s = this.indexOf(tag) + tag.length

    return this.substring(s, this.length)
}

fun String?.toImageUrlForThumbnail(): String? {
    this?.let {
        return Constants.GETTY_BASE_URL + it
    }
    return null
}
