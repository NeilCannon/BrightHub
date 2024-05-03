package com.example.brighthub

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun RepoListError(repos: RepoListState.Error) {
    Text("Error: ${repos.message}")
}