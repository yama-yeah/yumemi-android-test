/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.search

import androidx.lifecycle.ViewModel
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.domain.model.RepositoryDataModel
import jp.co.yumemi.android.code_check.domain.services.github.GithubApi
import jp.co.yumemi.android.code_check.domain.services.github.IGitHubApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import java.util.Date

/**
 * 検索画面のViewModel
 */
class SearchScreenViewModel : ViewModel() {
    val repositories = MutableStateFlow<MutableList<RepositoryDataModel>>(
        mutableListOf()
    )

    /**
     * 検索結果を返す
     * @param inputText リポジトリ名
     */
    fun searchResults(inputText: String): Unit = runBlocking {
        val githubApi: IGitHubApi = GithubApi()

        CoroutineScope(coroutineContext).async {
            // GitHubのAPIからリポジトリの情報のJSONを取得する
            val jsonRepositories = githubApi.getRepositoriesJson(inputText)

            repositories.value = mutableListOf()

            // 検索結果がない場合は空のリストを返す
            // Toastか何か出さないと検索終わったかわかりにくい
            if (jsonRepositories == null || jsonRepositories.length() == 0) {
                return@async
            }

            /**
             * リポジトリたちから一個ずつ情報を取得して、itemsにItemとして追加していく
             */
            for (i in 0 until jsonRepositories.length()) {
                val jsonRepository = jsonRepositories.optJSONObject(i)!!

                repositories.value.add(RepositoryDataModel.fromJson(jsonRepository))
            }
            // 検索した日時を保存
            lastSearchDate = Date()
        }.await()
    }
}
