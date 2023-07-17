package jp.co.yumemi.android.codecheck.domain.services.github

import dagger.hilt.android.scopes.ViewModelScoped
import jp.co.yumemi.android.codecheck.domain.models.repository.RepositoryModel
import jp.co.yumemi.android.codecheck.ui.search.SearchScreenGitHubService
import javax.inject.Inject

/**
 * GitHubのAPIから情報をモデルに起こして取得するサービス
 */
@ViewModelScoped
class GitHubService @Inject constructor(private val githubApi: GitHubApi) :
    SearchScreenGitHubService {
    override suspend fun getRepositories(repositoryName: String): List<RepositoryModel> {
        val jsonRepositories = githubApi.getRepositoriesJson(repositoryName)

        val repositories = mutableListOf<RepositoryModel>()

        // 検索結果がない場合は空のリストを返す
        // Toastか何か出さないと検索終わったかわかりにくい
        if (jsonRepositories == null || jsonRepositories.length() == 0) {
            return emptyList()
        }

        //JSONからRepositoriesに変換する
        for (i in 0 until jsonRepositories.length()) {
            val jsonRepository = jsonRepositories.optJSONObject(i)!!

            repositories.add(RepositoryModel.fromJson(jsonRepository))
        }
        return repositories
    }
}
