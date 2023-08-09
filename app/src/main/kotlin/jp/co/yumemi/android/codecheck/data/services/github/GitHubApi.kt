package jp.co.yumemi.android.codecheck.data.services.github

import android.util.Log
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

@ViewModelScoped
class GitHubApiImpl @Inject constructor() : GitHubApi {
    private val client = HttpClient(Android)
    override suspend fun getRepositoriesJson(repositoryName: String): JSONArray? {
        return try {
            val response: HttpResponse = client.get("https://api.github.com/search/repositories") {
                header("Accept", "application/vnd.github.v3+json")
                parameter("q", repositoryName)
            }

            val jsonBody = JSONObject(response.body<String>())

            // jsonからリポジトリたちを取得
            return jsonBody.optJSONArray("items")
        } catch (e: Exception) {
            Log.e("GitHubApiとの通信でエラー", e.toString())
            null
        }

    }
}

/**
 * GitHubのAPIからの情報をそのまま返す
 */
interface GitHubApi {
    /**
     * リポジトリのJSONArrayを取得する
     */
    suspend fun getRepositoriesJson(repositoryName: String): JSONArray?
}