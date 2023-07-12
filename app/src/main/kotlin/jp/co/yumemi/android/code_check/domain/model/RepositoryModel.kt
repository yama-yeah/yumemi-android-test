package jp.co.yumemi.android.code_check.domain.model

import android.os.Parcelable
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