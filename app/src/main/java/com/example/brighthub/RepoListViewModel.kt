package com.example.brighthub

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brighthub.RepoListState.Loading
import com.example.brighthub.data.GitHubContributor
import com.example.brighthub.data.GitHubSearchRepository
import com.example.brighthub.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed interface RepoListState {

    /**
     * Update the state with a new repo. This is a no-op for [Loading] and [Error] states.
     */
    fun update(repo: DisplayRepo): RepoListState = this

    data object Loading : RepoListState

    data class Success(val repositories: List<DisplayRepo>) : RepoListState {
        override fun update(repo: DisplayRepo): RepoListState = Success(repositories.replace(repo))
    }

    data class Error(val message: String) : RepoListState

    companion object {
        fun List<DisplayRepo>.replace(repo: DisplayRepo): List<DisplayRepo> = map {
            if (it.id == repo.id) repo else it
        }
    }
}


@HiltViewModel
class RepoListViewModel @Inject constructor(private val repository: GitHubSearchRepository) :
    ViewModel() {

    private val _state = MutableStateFlow<RepoListState>(Loading)
    val state: Flow<RepoListState> = _state

    // no params needed, so load eagerly
    init {
        viewModelScope.launch { load() }
    }

    private suspend fun load() {
        try {
            repository.fetchGithubRepos().items.let { repos ->
                _state.value = RepoListState.Success(repos.map { DisplayRepo(it) })
                fetchAllTopContributors(repos)
            }
        } catch (e: Exception) {
            _state.value = RepoListState.Error(e.message ?: "An error occurred")
        }
    }

    /**
     * Update the top contributor for all repos. Concurrency is limited by [dispatcher]
     * This will emit a new [Success] state for each repo that is updated - we rely on Compose to optimize this.
     */
    private suspend fun fetchAllTopContributors(repos: List<Repo>) = withContext(dispatcher) {
        repos.forEach { repo ->
            launch {
                try {
                    repository.getTopContributor(repo.owner.login, repo.name)?.let {
                        updateOneRepo(DisplayRepo(repo), it)
                    }
                } catch (e: Exception) {
                    // ignore: some repos throw errors when fetching contributors
                    Log.e("RepoListViewModel", "Error fetching top contributor for ${repo.name}", e)
                }
            }

        }
    }

    private fun updateOneRepo(repo: DisplayRepo, topContributor: GitHubContributor) {
        _state.getAndUpdate { it.update(repo.copy(topContributor = topContributor)) }
    }

    companion object {
        private val dispatcher = Dispatchers.IO.limitedParallelism(3)
    }
}