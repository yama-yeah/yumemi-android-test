/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.TwoFragmentArgs
import jp.co.yumemi.android.code_check.databinding.FragmentTwoBinding

/**
 * リポジトリ詳細画面
 */
class TwoFragment : Fragment(R.layout.fragment_two) {

    private val args: TwoFragmentArgs by navArgs()

    private var binding: FragmentTwoBinding? = null
    private val _binding get() = binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("検索した日時", lastSearchDate.toString())

        binding = FragmentTwoBinding.bind(view)

        val item = args.item
        // 画面にリポジトリの情報を表示する
        _binding.ownerIconView.load(item.ownerIconUrl)
        _binding.nameView.text = item.name
        _binding.languageView.text = getString(R.string.repo_written_language, item.language)
        _binding.starsView.text =
            getString(R.string.repo_stars_cnt, item.stargazersCount.toString())
        _binding.watchersView.text =
            getString(R.string.repo_watchers_cnt, item.watchersCount.toString())
        _binding.forksView.text = getString(R.string.repo_forks_cnt, item.forksCount.toString())
        _binding.openIssuesView.text =
            getString(R.string.repo_open_issues_cnt, item.openIssuesCount.toString())
    }
}
