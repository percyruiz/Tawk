package com.percivalruiz.tawk.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class UserProfile (
    val login: String,
    val id: Long,

    @Json(name = "node_id")
    val nodeID: String,

    @Json(name = "avatar_url")
    val avatarURL: String,

    @Json(name = "gravatar_id")
    val gravatarID: String,

    val url: String,

    @Json(name = "html_url")
    val htmlURL: String,

    @Json(name = "followers_url")
    val followersURL: String,

    @Json(name = "following_url")
    val followingURL: String,

    @Json(name = "gists_url")
    val gistsURL: String,

    @Json(name = "starred_url")
    val starredURL: String,

    @Json(name = "subscriptions_url")
    val subscriptionsURL: String,

    @Json(name = "organizations_url")
    val organizationsURL: String,

    @Json(name = "repos_url")
    val reposURL: String,

    @Json(name = "events_url")
    val eventsURL: String,

    @Json(name = "received_events_url")
    val receivedEventsURL: String,

    val type: String,

    @Json(name = "site_admin")
    val siteAdmin: Boolean,

    val name: String,
    val company: String? = null,
    val blog: String? = null,
    val location: String?,
    val email: Any? = null,
    val hireable: Any? = null,
    val bio: Any? = null,

    @Json(name = "twitter_username")
    val twitterUsername: String? = null,

    @Json(name = "public_repos")
    val publicRepos: Long? = null,

    @Json(name = "public_gists")
    val publicGists: Long? = null,

    val followers: Long,
    val following: Long,

    @Json(name = "created_at")
    val createdAt: String,

    @Json(name = "updated_at")
    val updatedAt: String
)