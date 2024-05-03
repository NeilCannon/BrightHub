package com.example.brighthub

import com.example.brighthub.data.GitHubContributor
import com.example.brighthub.data.Repo

/**
 * the per-repo data for display
 */
data class DisplayRepo(
    val id: String,
    val name: String,
    val owner: GitHubContributor,
    val topContributor: GitHubContributor? = null
) {
    constructor(repo: Repo) : this(repo.id, repo.name, repo.owner)
}