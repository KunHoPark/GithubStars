package com.kona.artmining.util

/**
 * Created by parkkh on 2018. 1. 18..
 */

fun Long?.waitTest(){
    this?.let {
        Thread.sleep(it)
    }
}

fun Int?.waitTest(){
    this?.let {
        try {
            var value = it * 1000L
            Thread.sleep(value)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}