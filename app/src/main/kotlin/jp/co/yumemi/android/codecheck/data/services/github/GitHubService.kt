package jp.co.yumemi.android.codecheck.data.services.github

import dagger.hilt.android.scopes.ViewModelScoped
import jp.co.yumemi.android.codecheck.data.models.repository.RepositoryModel
import javax.inject.Inject

/**
 * GitHubのAPIから情報をモデルに起こして取得するサービス
 */
@ViewModelScoped
class GitHubServiceImpl @Inject constructor(private val githubApi: GitHubApi) :
    GitHubService {
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

interface GitHubService {
    /**
     * リポジトリを検索する
     * @param repositoryName 検索するリポジトリ名
     * @return 検索結果のリポジトリのリスト
     */
    suspend fun getRepositories(repositoryName: String): List<RepositoryModel>

}