package jp.co.yumemi.android.codecheck.domain.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize
import org.json.JSONObject


/**
 * リポジトリを表すデータクラス
 */
@Parcelize
data class RepositoryDataModel(
    val name: String,
    val ownerIconUrl: String,
    val language: String,
    val stargazersCount: Long,
    val watchersCount: Long,
    val forksCount: Long,
    val openIssuesCount: Long,
) : Parcelable {
    companion object {
        fun fromJson(json: JSONObject): RepositoryDataModel {
            val name =
                json.optString("full_name")
            val ownerIconUrl = json.optJSONObject("owner")?.optString("avatar_url")
                ?: "https://via.placeholder.com/500x500.png?text=Owner%20has%20not%20Picture"
            val language = json.optString("language")
            val stargazersCount = json.optLong("stargazers_count")
            val watchersCount = json.optLong("watchers_count")
            val forksCount = json.optLong("forks_count")
            val openIssuesCount = json.optLong("open_issues_count")
            return RepositoryDataModel(
                name = name,
                ownerIconUrl = ownerIconUrl,
                language = language,
                stargazersCount = stargazersCount,
                watchersCount = watchersCount,
                forksCount = forksCount,
                openIssuesCount = openIssuesCount
            )
        }
    }
}


val repositoryDiffUtil = object : DiffUtil.ItemCallback<RepositoryDataModel>() {
    override fun areItemsTheSame(
        oldItem: RepositoryDataModel,
        newItem: RepositoryDataModel
    ): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: RepositoryDataModel,
        newItem: RepositoryDataModel
    ): Boolean {
        return oldItem == newItem
    }

}
