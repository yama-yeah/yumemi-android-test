package jp.co.yumemi.android.codecheck

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.karumi.shot.ScreenshotTest
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.testing.TestInstallIn
import jp.co.yumemi.android.codecheck.domain.services.github.GitHubApi
import jp.co.yumemi.android.codecheck.domain.services.github.GitHubApiImpl
import jp.co.yumemi.android.codecheck.domain.services.github.GitHubService
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
@LargeTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class VisualAppTest : ScreenshotTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule: ActivityScenarioRule<TopActivity> =
        ActivityScenarioRule(TopActivity::class.java)


    @Test
    fun testEvent() {
        hiltRule.inject()
        onView(
            withId(R.id.searchInputText),
        ).perform(
            ViewActions.replaceText("flutter"),
            ViewActions.closeSoftKeyboard(),
            pressImeActionButton()
        )
        activityRule.scenario.onActivity {
            compareScreenshot(it, name = "normal_state_search_screen")
        }
        Thread.sleep(256)
        activityRule.scenario.onActivity {
            compareScreenshot(it, name = "searching_state_search_screen")
        }
        onView(withText("flutter/samples")).perform(ViewActions.click())
        Thread.sleep(256)
        activityRule.scenario.onActivity {
            compareScreenshot(it, name = "normal_state_detail_screen")
        }
    }
}

//
@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [SearchScreenViewModelModule::class]
)
class FakeModule {

    @ViewModelScoped
    @Provides
    fun provideFakeGitHubApi(api: GitHubApiImpl): GitHubApi {
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