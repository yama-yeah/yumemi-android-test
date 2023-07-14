package jp.co.yumemi.android.codecheck

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.testing.TestInstallIn
import jp.co.yumemi.android.codecheck.domain.services.github.GitHubApi
import jp.co.yumemi.android.codecheck.domain.services.github.GitHubService
import jp.co.yumemi.android.codecheck.domain.services.github.MockGitHubApi
import jp.co.yumemi.android.codecheck.ui.search.SearchScreenFragment
import jp.co.yumemi.android.codecheck.ui.search.SearchScreenGitHubService
import jp.co.yumemi.android.codecheck.ui.search.SearchScreenViewModelModule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class VisualAppTest : ScreenshotTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun testEvent() {
        val scenario = launchFragmentInHiltContainer<SearchScreenFragment> {
            compareScreenshot(this.requireActivity())
        }
    }
}

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [SearchScreenViewModelModule::class]
)
class FakeModule {

    @ViewModelScoped
    @Provides
    fun provideFakeGitHubApi(api: MockGitHubApi): GitHubApi {
        return api
    }

    @Provides
    @ViewModelScoped
    fun provideSearchScreenGithubService(
        githubService: GitHubService
    ): SearchScreenGitHubService {
        return githubService
    }
}