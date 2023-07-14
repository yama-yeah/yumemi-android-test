package jp.co.yumemi.android.codecheck.ui.search

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.scopes.FragmentScoped
import jp.co.yumemi.android.codecheck.domain.model.RepositoryModel
import javax.inject.Inject

@FragmentScoped
class SearchScreenNavigator @Inject constructor(private val fragment: SearchScreenFragment) {

    /**
     * リポジトリ詳細画面に遷移する
     * @param repository 選択したリポジトリ
     */
    fun gotoDetailScreen(repository: RepositoryModel) {
        val action =
            SearchScreenFragmentDirections.actionSearchToDetail(repository)
        fragment.findNavController().navigate(action)
    }
}