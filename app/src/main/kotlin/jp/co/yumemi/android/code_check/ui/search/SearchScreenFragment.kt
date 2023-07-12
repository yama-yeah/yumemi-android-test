/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.databinding.FragmentSearchScreenBinding
import jp.co.yumemi.android.code_check.domain.model.RepositoryDataModel
import jp.co.yumemi.android.code_check.util.setOnSearchActionListener

/**
 * 検索画面かつホーム画面
 * リストタイルをタップすると、リポジトリ詳細画面に遷移する
 */
class SearchScreenFragment : Fragment(R.layout.fragment_search_screen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSearchScreenBinding.bind(view)

        val viewModel = SearchScreenViewModel()

        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        // アダプターを作成する
        val adapter = SearchResultAdapter {
            gotoDetailScreen(it)
        }
        // 検索ボタンを押したときの処理
        binding.searchInputText.setOnSearchActionListener {
            viewModel.searchResults(it.text.toString())
            adapter.submitList(viewModel.repositories.value)
        }
        binding.recyclerView.also {
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = adapter
        }
    }

    /**
     * リポジトリ詳細画面に遷移する
     * @param repository 選択したリポジトリ
     */
    private fun gotoDetailScreen(repository: RepositoryDataModel) {
        val action =
            SearchScreenFragmentDirections.actionSearchToDetail(repository)
        findNavController().navigate(action)
    }
}
