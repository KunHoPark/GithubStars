package com.leo.githubstars.extension

import java.util.regex.Pattern


/**
* StringExt
* @author LeoPark
**/

/**
 *  한글 구분
 */
fun String?.isHangul() : Boolean = Pattern.matches("^[ㄱ-ㅎ가-힣0-9]*\$", this)

/**
 *  영문 구분
 */
fun String?.isEnglish() : Boolean = Pattern.matches("^[a-zA-Z\\s]+\$", this)

/**
 *  숫자 구분
 */
fun String?.isNumeric() : Boolean =   Pattern.matches("-?\\d+(\\.\\d+)?", this)



