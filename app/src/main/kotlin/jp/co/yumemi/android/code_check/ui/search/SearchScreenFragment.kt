/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.databinding.FragmentOneBinding
import jp.co.yumemi.android.code_check.domain.model.RepositoryDataModel

/**
 * 検索画面かつホーム画面
 * リストタイルをタップすると、リポジトリ詳細画面に遷移する
 */
class SearchScreenFragment : Fragment(R.layout.fragment_one) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentOneBinding.bind(view)

        val viewModel = SearchScreenViewModel()

        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        // アダプターを作成する
        val adapter = SearchResultAdapter(object : SearchResultAdapter.OnItemClickListener {
            // リストタイルをタップしたときの処理
            override fun itemClick(item: RepositoryDataModel) {
                // リポジトリ詳細画面に遷移する
                gotoRepositoryFragment(item)
            }
        })
        // 検索ボタンを押したときの処理
        binding.searchInputText
            .setOnEditorActionListener { editText, action, _ ->
                // キーボードの検索ボタンが押されたとき
                if (action == EditorInfo.IME_ACTION_SEARCH) {
                    // アダプタに検索結果をセットする
                    editText.text.toString().let {
                        viewModel.searchResults(it).apply {
                            adapter.submitList(this)
                        }
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        binding.recyclerView.also {
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = adapter
        }
    }

    /**
     * リポジトリ詳細画面に遷移する
     * @param item リポジトリの情報
     */
    fun gotoRepositoryFragment(item: RepositoryDataModel) {
        val action =
            SearchScreenFragmentDirections.actionRepositoriesFragmentToRepositoryFragment(item = item)
        findNavController().navigate(action)
    }
}

val diff_util = object : DiffUtil.ItemCallback<RepositoryDataModel>() {
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
            .inflate(R.layout.layout_item, parent, false)
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
