package com.example.brighthub.data

data class Repo(
    val id: String,
    val name: String,
    val owner: GitHubContributor,
)