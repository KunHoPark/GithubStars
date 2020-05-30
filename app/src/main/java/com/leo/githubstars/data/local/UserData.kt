package com.leo.githubstars.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import javax.annotation.Nullable

/**
 * 북마크된 데이타를 저장하기 위한 형식
 * @author LeoPark
 */
@Entity(tableName = "bookmark_user_table")
data class UserData(

        @PrimaryKey @ColumnInfo(name = "id")
        var id:String,

        @ColumnInfo(name = "login")
        var login:String,

        @ColumnInfo(name = "avatar_url")
        @SerializedName("avatar_url")
        var avatarUrl:String,                    // 유저의 이미지 정보

        @ColumnInfo(name = "url")
        var url:String,

        @ColumnInfo(name = "is_bookmark")
        var isBookmark:Boolean,

        var name: String?,

        var email: String?,

        val followers: Long= 0,

        val following: Long= 0,

        val created_at: String?

) : Serializable






