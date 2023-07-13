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
import jp.co.yumemi.android.code_check.databinding.FragmentDetailScreenBinding
import jp.co.yumemi.android.code_check.util.autoCleared

/**
 * リポジトリ詳細画面
 */
class DetailScreenFragment : Fragment(R.layout.fragment_detail_screen) {

    private val args: DetailScreenFragmentArgs by navArgs()

    private var binding by autoCleared<FragmentDetailScreenBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("検索した日時", lastSearchDate.toString())

        binding = FragmentDetailScreenBinding.bind(view)

        val item = args.item
        // 画面にリポジトリの情報を表示する
        binding.ownerIconView.load(item.ownerIconUrl)
        binding.nameView.text = item.name
        binding.languageView.text = getString(R.string.repo_written_language, item.language)
        binding.starsView.text =
            getString(R.string.repo_stars_cnt, item.stargazersCount.toString())
        binding.watchersView.text =
            getString(R.string.repo_watchers_cnt, item.watchersCount.toString())
        binding.forksView.text = getString(R.string.repo_forks_cnt, item.forksCount.toString())
        binding.openIssuesView.text =
            getString(R.string.repo_open_issues_cnt, item.openIssuesCount.toString())
    }
}
