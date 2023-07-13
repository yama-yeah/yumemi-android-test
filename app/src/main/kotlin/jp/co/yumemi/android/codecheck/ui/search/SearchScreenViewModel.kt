/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.ui.search

import androidx.lifecycle.ViewModel
import jp.co.yumemi.android.codecheck.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.codecheck.domain.model.RepositoryModel
import jp.co.yumemi.android.codecheck.domain.services.github.GitHubApi
import jp.co.yumemi.android.codecheck.domain.services.github.GithubApiImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import java.util.Date

/**
 * 検索画面のViewModel
 */
class SearchScreenViewModel : ViewModel() {
    private val _repositoriesStateFlow = MutableStateFlow<List<RepositoryModel>>(
        emptyList()
    )
    val repositoriesStateFlow get() = _repositoriesStateFlow.asStateFlow()

    /**
     * 検索結果を返す
     * @param repositoryName リポジトリ名
     */
    fun searchRepositories(repositoryName: String): Unit = runBlocking {
        val githubApi: GitHubApi = GithubApiImpl()

        CoroutineScope(coroutineContext).async {
            // GitHubのAPIからリポジトリの情報のJSONを取得する
            val jsonRepositories = githubApi.getRepositoriesJson(repositoryName)

            _repositoriesStateFlow.value = listOf()

            val repositories = mutableListOf<RepositoryModel>()

            // 検索結果がない場合は空のリストを返す
            // Toastか何か出さないと検索終わったかわかりにくい
            if (jsonRepositories == null || jsonRepositories.length() == 0) {
                return@async
            }

            //リポジトリたちから一個ずつ情報を取得して、jsonRepositoriesにrepositoryとして追加していく
            for (i in 0 until jsonRepositories.length()) {
                val jsonRepository = jsonRepositories.optJSONObject(i)!!

                repositories.add(RepositoryModel.fromJson(jsonRepository))
            }
            _repositoriesStateFlow.value = repositories
            // 検索した日時を保存
            lastSearchDate = Date()
        }.await()
    }
}
