package com.percivalruiz.tawk.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity
data class User (
    @ColumnInfo(name = "login")
    val login: String,

    @PrimaryKey val id: Long,

    @ColumnInfo(name = "node_id")
    @Json(name = "node_id")
    val nodeID: String,

    @ColumnInfo(name = "avatar_url")
    @Json(name = "avatar_url")
    val avatarURL: String,

    @ColumnInfo(name = "gravatar_id")
    @Json(name = "gravatar_id")
    val gravatarID: String,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "html_url")
    @Json(name = "html_url")
    val htmlURL: String,

    @ColumnInfo(name = "followers_url")
    @Json(name = "followers_url")
    val followersURL: String,

    @ColumnInfo(name = "following_url")
    @Json(name = "following_url")
    val followingURL: String,

    @ColumnInfo(name = "gists_url")
    @Json(name = "gists_url")
    val gistsURL: String,

    @ColumnInfo(name = "starred_url")
    @Json(name = "starred_url")
    val starredURL: String,

    @ColumnInfo(name = "subscriptions_url")
    @Json(name = "subscriptions_url")
    val subscriptionsURL: String,

    @ColumnInfo(name = "organizations_url")
    @Json(name = "organizations_url")
    val organizationsURL: String,

    @ColumnInfo(name = "repos_url")
    @Json(name = "repos_url")
    val reposURL: String,

    @ColumnInfo(name = "events_url")
    @Json(name = "events_url")
    val eventsURL: String,

    @ColumnInfo(name = "received_events_url")
    @Json(name = "received_events_url")
    val receivedEventsURL: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "site_admin")
    @Json(name = "site_admin")
    val siteAdmin: Boolean
)