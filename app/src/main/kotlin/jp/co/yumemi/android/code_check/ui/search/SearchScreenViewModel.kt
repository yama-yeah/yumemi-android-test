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
 * 検索画面のViewModel
 */
class SearchScreenViewModel : ViewModel() {

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
            val jsonRepositories = jsonBody.optJSONArray("items")

            val repositories = mutableListOf<RepositoryDataModel>()

            // 検索結果がない場合は空のリストを返す
            if (jsonRepositories == null || jsonRepositories.length() == 0) {
                return@async repositories.toList()
            }

            /**
             * リポジトリたちから一個ずつ情報を取得して、itemsにItemとして追加していく
             */
            for (i in 0 until jsonRepositories.length()) {
                val jsonRepository = jsonRepositories.optJSONObject(i)!!

                repositories.add(RepositoryDataModel.fromJson(jsonRepository))
            }
            // 検索した日時を保存
            lastSearchDate = Date()

            return@async repositories.toList()
        }.await()
    }
}
