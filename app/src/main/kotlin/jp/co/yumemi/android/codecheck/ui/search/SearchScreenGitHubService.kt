package jp.co.yumemi.android.codecheck.ui.search

import jp.co.yumemi.android.codecheck.domain.models.repository.RepositoryModel

/**
 * GitHubのAPIから情報をモデルに起こして取得するサービス
 * 検索画面でのみ使う
 */
interface SearchScreenGitHubService {
    /**
     * リポジトリを検索する
     * @param repositoryName 検索するリポジトリ名
     * @return 検索結果のリポジトリのリスト
     */
    suspend fun getRepositories(repositoryName: String): List<RepositoryModel>
}