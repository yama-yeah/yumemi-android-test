package jp.co.yumemi.android.code_check.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.domain.model.RepositoryDataModel
import jp.co.yumemi.android.code_check.domain.model.repositoryDiffUtil


/**
 * 検索して出てきたリポジトリのリストを表示するためのアダプター
 * リストをタップすると、リポジトリ詳細画面に遷移する
 */
class SearchResultAdapter(
    private val onClicked: (RepositoryDataModel) -> Unit,
) : ListAdapter<RepositoryDataModel, SearchResultAdapter.ViewHolder>(repositoryDiffUtil) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_repository, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repository = getItem(position)
        (holder.itemView.findViewById<View>(R.id.repositoryNameView) as TextView).text =
            repository.name
        onClicked(repository)
    }
}
