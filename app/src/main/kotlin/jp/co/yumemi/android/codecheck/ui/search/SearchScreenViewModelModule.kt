package jp.co.yumemi.android.codecheck.ui.search

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import jp.co.yumemi.android.codecheck.domain.services.github.GitHubApi
import jp.co.yumemi.android.codecheck.domain.services.github.GitHubApiImpl
import jp.co.yumemi.android.codecheck.domain.services.github.GitHubService


@Module
@InstallIn(ViewModelComponent::class)
class SearchScreenViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideGithubApi(
        githubApi: GitHubApiImpl
    ): GitHubApi {
        return githubApi
    }

    @Provides
    @ViewModelScoped
    fun provideSearchScreenGithubService(
        githubService: GitHubService
    ): SearchScreenGitHubService {
        return githubService
    }
}