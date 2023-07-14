package jp.co.yumemi.android.codecheck.ui.search

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
class SearchScreenFragmentModule {

    @Provides
    @FragmentScoped
    fun provideFragment(
        fragment: Fragment
    ): SearchScreenFragment {
        return fragment as SearchScreenFragment
    }

    @Provides
    @FragmentScoped
    fun provideDividerItemDecoration(
        @ApplicationContext context: Context,
    ): DividerItemDecoration {
        return DividerItemDecoration(context, RecyclerView.VERTICAL)
    }
}