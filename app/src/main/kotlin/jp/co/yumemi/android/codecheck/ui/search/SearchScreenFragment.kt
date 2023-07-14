/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.codecheck.R
import jp.co.yumemi.android.codecheck.databinding.FragmentSearchScreenBinding
import jp.co.yumemi.android.codecheck.util.autoCleared
import jp.co.yumemi.android.codecheck.util.setOnSearchActionListener
import javax.inject.Inject

/**
 * 検索画面かつホーム画面
 * リストタイルをタップすると、リポジトリ詳細画面に遷移する
 */
@AndroidEntryPoint
class SearchScreenFragment : Fragment(R.layout.fragment_search_screen) {
    private val viewModel: SearchScreenViewModel by viewModels()

    private var binding by autoCleared<FragmentSearchScreenBinding>()

    @Inject
    lateinit var adapter: SearchScreenAdapter

    @Inject
    lateinit var dividerItemDecoration: DividerItemDecoration

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchScreenBinding.bind(view)

        val repositoriesFlow = viewModel.repositoriesStateFlow
        // アダプターのリストを初期化
        adapter.submitList(repositoriesFlow.value)

        // 検索ボタンを押したときの処理
        binding.searchInputText.setOnSearchActionListener {
            viewModel.searchRepositories(it.text.toString())
            adapter.submitList(repositoriesFlow.value)
        }
        binding.recyclerView.also {
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }


}
