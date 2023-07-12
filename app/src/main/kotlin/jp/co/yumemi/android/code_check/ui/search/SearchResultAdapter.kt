package jp.co.yumemi.android.code_check.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.domain.model.RepositoryDataModel


/**
 * 検索して出てきたリポジトリのリストを表示するためのアダプター
 * @param itemClickListener リポジトリのリストをタップしたときのコールバック
 */
class SearchResultAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<RepositoryDataModel, SearchResultAdapter.ViewHolder>(diff_util) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    /**
     * リポジトリのリストをタップしたときのコールバックのインターフェース
     */
    interface OnItemClickListener {
        fun itemClick(item: RepositoryDataModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_repository, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        (holder.itemView.findViewById<View>(R.id.repositoryNameView) as TextView).text =
            item.name
        // リポジトリのリストをタップしたときのコールバック
        holder.itemView.setOnClickListener {
            itemClickListener.itemClick(item)
        }
    }
}
