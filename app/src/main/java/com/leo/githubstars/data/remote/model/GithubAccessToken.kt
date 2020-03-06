package com.leo.githubstars.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Github Token 데이터 타임.
 * @author LeoPark
 */
class GithubAccessToken(
        @SerializedName("access_token") val accessToken: String,
        val scope: String,
        @SerializedName("token_type") val tokenType: String)
