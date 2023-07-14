package jp.co.yumemi.android.codecheck.domain.services.github

import dagger.hilt.android.scopes.ViewModelScoped
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

@ViewModelScoped
class MockGitHubApi @Inject constructor() : GitHubApi {
    /**
     * GitHubのAPIのドキュメントにあるJSONを拝借
     */
    override suspend fun getRepositoriesJson(repositoryName: String): JSONArray? {
        val fakeJson = """{
  "total_count": 2,
  "incomplete_results": false,
  "items": [
    {
      "id": 418327088,
      "node_id": "MDU6TGFiZWw0MTgzMjcwODg=",
      "url": "https://api.github.com/repos/octocat/linguist/labels/enhancement",
      "name": "enhancement",
      "color": "84b6eb",
      "default": true,
      "description": "New feature or request.",
      "score": 1
    },
    {
      "id": 418327086,
      "node_id": "MDU6TGFiZWw0MTgzMjcwODY=",
      "url": "https://api.github.com/repos/octocat/linguist/labels/bug",
      "name": "bug",
      "color": "ee0701",
      "default": true,
      "description": "Something isn't working.",
      "score": 1
    }
  ]
}"""
        return JSONObject(fakeJson).optJSONArray("items")
    }
}