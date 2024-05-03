package com.example.brighthub

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun RepoListScreen(vm: RepoListViewModel) {
    when (val repos = vm.state.collectAsState(initial = RepoListState.Loading).value) {
        is RepoListState.Loading -> LoadingProgress()
        is RepoListState.Success -> RepoListContent(repos)
        is RepoListState.Error -> RepoListError(repos)
    }
}
