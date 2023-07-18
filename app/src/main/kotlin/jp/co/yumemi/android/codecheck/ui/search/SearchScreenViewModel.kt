/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.ui.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.codecheck.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.codecheck.domain.models.repository.RepositoryModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

/**
 * 検索画面のViewModel
 */
@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val githubService: SearchScreenGitHubService,
    initStateRepositories: List<RepositoryModel>
) : ViewModel() {
    private val _repositoriesStateFlow = MutableStateFlow(
        initStateRepositories
    )
    val repositoriesStateFlow get() = _repositoriesStateFlow.asStateFlow()

    /**
     * [repositoryName]を元にGitHubからリポジトリを検索して
     * repositoriesStateFlowを更新する
     */
    fun searchRepositories(repositoryName: String): Unit = runBlocking {
        withContext(coroutineContext) {
            // GitHubからRepositoryModelのリストを取得
            val repositories = githubService.getRepositories(repositoryName)
            _repositoriesStateFlow.value = repositories
            // 検索した日時を保存
            lastSearchDate = Date()
        }
    }
}
