package com.leo.githubstars.data.local

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 서버를 통한 검색 결과 데이타형식
 * @author LeoPark
 */
data class SearchData(

        @SerializedName("total_count")
        var totalCount:String,                            // 검색 데이타의 총 카운터

        var items:ArrayList<UserData>             // 사용자 정보 리스트

) : Serializable






