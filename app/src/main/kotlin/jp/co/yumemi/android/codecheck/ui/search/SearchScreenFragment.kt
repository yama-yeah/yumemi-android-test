/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yumemi.android.codecheck.R
import jp.co.yumemi.android.codecheck.databinding.FragmentSearchScreenBinding
import jp.co.yumemi.android.codecheck.domain.model.RepositoryModel
import jp.co.yumemi.android.codecheck.util.autoCleared
import jp.co.yumemi.android.codecheck.util.setOnSearchActionListener

/**
 * 検索画面かつホーム画面
 * リストタイルをタップすると、リポジトリ詳細画面に遷移する
 */
class SearchScreenFragment : Fragment(R.layout.fragment_search_screen) {
    private val viewModel: SearchScreenViewModel by viewModels()

    // アダプターを作成する
    private var adapter by autoCleared<SearchResultAdapter>()

    private var binding by autoCleared<FragmentSearchScreenBinding>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SearchResultAdapter {
            gotoDetailScreen(it)
        }
        binding = FragmentSearchScreenBinding.bind(view)

        val repositoriesFlow = viewModel.repositoriesStateFlow
        // アダプターのリストを初期化
        adapter.submitList(repositoriesFlow.value)

        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), layoutManager.orientation)

        // 検索ボタンを押したときの処理
        binding.searchInputText.setOnSearchActionListener {
            viewModel.searchRepositories(it.text.toString())
            adapter.submitList(repositoriesFlow.value)
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
    private fun gotoDetailScreen(repository: RepositoryModel) {
        val action =
            SearchScreenFragmentDirections.actionSearchToDetail(repository)
        findNavController().navigate(action)
    }
}
