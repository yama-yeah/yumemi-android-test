package jp.co.yumemi.android.code_check.domain.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize


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
) : Parcelable


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
