package com.example.brighthub.data

import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {
//    @Headers("Authorization: Bearer [TOKEN GOES HERE]")
    @GET("search/repositories?q=stars:%3E0")
    suspend fun listRepos(): GitHubSearchResponse

//    @Headers("Authorization: Bearer [TOKEN GOES HERE]")
    @GET("repos/{owner}/{name}/contributors")
    suspend fun getContributors(
        @Path("owner") owner: String,
        @Path("name") name: String
    ): List<GitHubContributor>
}

data class GitHubSearchResponse(val items: List<Repo>)

