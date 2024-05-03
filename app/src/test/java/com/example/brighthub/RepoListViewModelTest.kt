@file:OptIn(ExperimentalTime::class, ExperimentalTime::class)

package com.example.brighthub

import app.cash.turbine.test
import com.example.brighthub.data.GitHubContributor
import com.example.brighthub.data.GitHubSearchRepository
import com.example.brighthub.data.GitHubSearchResponse
import com.example.brighthub.data.Repo
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.time.ExperimentalTime

class RepoListViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val gitHubSearchRepository = mockk<GitHubSearchRepository>()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun smokeTest() = runBlocking {
        val viewModel = RepoListViewModel(gitHubSearchRepository)

        coEvery { gitHubSearchRepository.fetchGithubRepos() } returns GitHubSearchResponse(repos)
        coEvery {
            gitHubSearchRepository.getTopContributor("owner1", "repo1")
        } returns contrib1

        viewModel.state.test {
            assertEquals(RepoListState.Loading, awaitItem())
            advanceUntilIdle()
            assertEquals(RepoListState.Success(displayRepos), awaitItem())
            delay(100) // needed to let the coroutines launch
            advanceUntilIdle()
            assertEquals(RepoListState.Success(displayReposUpdated), awaitItem())
            expectNoEvents()
        }
    }

    private fun advanceUntilIdle() = dispatcher.scheduler.advanceUntilIdle()

    companion object {
        val repos = listOf(Repo("1", "repo1", GitHubContributor("owner1")))
        val displayRepos = listOf(DisplayRepo("1", "repo1", GitHubContributor("owner1")))

        val contrib1 = GitHubContributor("contrib1")
        val displayReposUpdated =
            listOf(DisplayRepo("1", "repo1", GitHubContributor("owner1"), contrib1))

    }
}