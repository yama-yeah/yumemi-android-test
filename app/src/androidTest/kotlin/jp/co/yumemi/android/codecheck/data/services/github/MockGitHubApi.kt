package jp.co.yumemi.android.codecheck.data.services.github

import dagger.hilt.android.scopes.ViewModelScoped
import jp.co.yumemi.android.codecheck.fakedata.RepositoriesFakeJson
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

@ViewModelScoped
class MockGitHubApi @Inject constructor() : GitHubApi {
    /**
     * GitHubのAPIのドキュメントにあるJSONを拝借
     */
    override suspend fun getRepositoriesJson(repositoryName: String): JSONArray? {
        return JSONObject(RepositoriesFakeJson).optJSONArray("items")
    }
}