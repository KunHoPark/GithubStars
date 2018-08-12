package com.leo.githubstars.data.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
* UserData
* @author KunHo Park
* @since 2018. 08. 3. PM 7:41
**/
@Entity(tableName = "bookmark_user_table")
data class UserData(

        @PrimaryKey @ColumnInfo(name = "id")
        var id:String,                            // id 로 다른 데이타를 호출 할 때의 키 값으로 사용 한다.

        @ColumnInfo(name = "login")
        var login:String,                         // login

        @ColumnInfo(name = "avatar_url")
        @SerializedName("avatar_url")
        var avatarUrl:String,                    // 유저의 이미지 정보

        @ColumnInfo(name = "url")
        var url:String,                            // 유저의 상세 정보를 조회 할 때 사용 한다.

//        @ColumnInfo(name = "user_name")
//        var name:String,

        @ColumnInfo(name = "is_bookmark")          // Bookmark 여부.
        var isBookmark:Boolean


) : Serializable






