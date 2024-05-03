package com.example.brighthub.data

import javax.inject.Inject

class GitHubDataSource @Inject constructor(private val service: GitHubService) {
    suspend fun fetchRepos(): GitHubSearchResponse = service.listRepos()
    suspend fun getContributors(owner: String, name: String): List<GitHubContributor> =
        service.getContributors(owner, name)
}

