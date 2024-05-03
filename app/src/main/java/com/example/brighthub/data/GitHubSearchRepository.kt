package com.example.brighthub.data

import javax.inject.Inject

/**
 * naming: "Repository" means a repository in the Android sense.
 * we use [Repo] to represent a GitHub repo.
 */
class GitHubSearchRepository @Inject constructor(private val dataSource: GitHubDataSource) {
    suspend fun fetchGithubRepos(): GitHubSearchResponse = dataSource.fetchRepos()
    suspend fun getContributors(owner: String, name: String) =
        dataSource.getContributors(owner, name)

    // contributors are returned sorted: the first one is the top contributor
    suspend fun getTopContributor(owner: String, name: String): GitHubContributor? =
        getContributors(owner, name).firstOrNull()

}