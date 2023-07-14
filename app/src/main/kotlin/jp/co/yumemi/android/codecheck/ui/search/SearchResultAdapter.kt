package jp.co.yumemi.android.codecheck.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.scopes.FragmentScoped
import jp.co.yumemi.android.codecheck.R
import jp.co.yumemi.android.codecheck.databinding.LayoutRepositoryBinding
import jp.co.yumemi.android.codecheck.domain.model.RepositoryModel
import jp.co.yumemi.android.codecheck.domain.model.repositoryDiffUtil
import javax.inject.Inject


/**
 * 検索して出てきたリポジトリのリストを表示するためのアダプター
 * リストをタップすると、リポジトリ詳細画面に遷移する
 */
@FragmentScoped
class SearchResultAdapter @Inject constructor(
    private val navigator: SearchScreenNavigator
) : ListAdapter<RepositoryModel, SearchResultAdapter.ViewHolder>(repositoryDiffUtil) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_repository, parent, false)
        val binding = LayoutRepositoryBinding.bind(view)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repository = getItem(position)
        (holder.itemView.findViewById<View>(R.id.repositoryNameView) as TextView).text =
            repository.name
        holder.itemView.setOnClickListener {
            navigator.gotoDetailScreen(repository)
        }
    }
}
