package com.leo.githubstars.data.local

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
* UserData
* @author KunHo Park
* @since 2018. 08. 3. PM 7:41
**/
data class SearchData(

        @SerializedName("total_count")
        var totalCount:String,                            // 검색 데이타의 총 카운터

        var items:ArrayList<UserData>             // 사용자 정보 리스트

) : Serializable






