package jp.co.yumemi.android.codecheck.domain.models.repository

import androidx.recyclerview.widget.DiffUtil
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

/**
 * リポジトリの差分を計算するクラス
 */
@FragmentScoped
class RepositoryDiffUtil @Inject constructor() : DiffUtil.ItemCallback<RepositoryModel>() {
    override fun areItemsTheSame(
        oldItem: RepositoryModel,
        newItem: RepositoryModel
    ): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: RepositoryModel,
        newItem: RepositoryModel
    ): Boolean {
        return oldItem == newItem
    }
}