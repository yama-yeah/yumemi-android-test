/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.search

import androidx.lifecycle.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.domain.model.RepositoryDataModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.util.Date

/**
 * OneFragmentのViewModel
 */
class OneViewModel : ViewModel() {

    /**
     * 検索結果を返す
     * @param inputText リポジトリ名
     */
    fun searchResults(inputText: String): List<RepositoryDataModel> = runBlocking {
        val client = HttpClient(Android)

        return@runBlocking GlobalScope.async {
            // 検索結果をApiから取得（json形式）
            val response: HttpResponse = client.get("https://api.github.com/search/repositories") {
                header("Accept", "application/vnd.github.v3+json")
                parameter("q", inputText)
            }

            val jsonBody = JSONObject(response.body<String>())
            // jsonからリポジトリたちを取得
            val jsonItems = jsonBody.optJSONArray("items")!!

            val items = mutableListOf<RepositoryDataModel>()

            /**
             * リポジトリたちから一個ずつ情報を取得して、itemsにItemとして追加していく
             */
            for (i in 0 until jsonItems.length()) {
                val jsonItem = jsonItems.optJSONObject(i)!!
                val name = jsonItem.optString("full_name")
                val ownerIconUrl = jsonItem.optJSONObject("owner")!!.optString("avatar_url")
                val language = jsonItem.optString("language")
                val stargazersCount = jsonItem.optLong("stargazers_count")
                val watchersCount = jsonItem.optLong("watchers_count")
                val forksCount = jsonItem.optLong("forks_count")
                val openIssuesCount = jsonItem.optLong("open_issues_count")

                items.add(
                    RepositoryDataModel(
                        name = name,
                        ownerIconUrl = ownerIconUrl,
                        language = language,
                        stargazersCount = stargazersCount,
                        watchersCount = watchersCount,
                        forksCount = forksCount,
                        openIssuesCount = openIssuesCount
                    )
                )
            }
            // 検索した日時を保存
            lastSearchDate = Date()

            return@async items.toList()
        }.await()
    }
}
