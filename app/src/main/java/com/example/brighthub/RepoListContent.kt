package com.example.brighthub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun RepoListContent(repos: RepoListState.Success) {
    LazyColumn(verticalArrangement = Arrangement.Absolute.spacedBy(12.dp)) {
        items(repos.repositories) { repo ->
            RepoItem(
                owner = repo.owner.login,
                name = repo.name,
                topContributor = repo.topContributor?.login
            )
        }
    }
}