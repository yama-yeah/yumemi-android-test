package jp.co.yumemi.android.codecheck

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.util.TreeIterables
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.karumi.shot.ScreenshotTest
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.testing.TestInstallIn
import jp.co.yumemi.android.codecheck.domain.models.repository.RepositoryModel
import jp.co.yumemi.android.codecheck.domain.services.github.GitHubApi
import jp.co.yumemi.android.codecheck.domain.services.github.GitHubService
import jp.co.yumemi.android.codecheck.domain.services.github.MockGitHubApi
import jp.co.yumemi.android.codecheck.fakedata.RepositoriesFakeJson
import jp.co.yumemi.android.codecheck.ui.search.SearchScreenGitHubService
import jp.co.yumemi.android.codecheck.ui.search.SearchScreenViewModelModule
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.json.JSONObject
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

    private lateinit var device: UiDevice

    @Test
    fun testUseCase() {
        Thread.sleep(1024)
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        onView(
            ViewMatchers.withId(R.id.searchInputText),
        ).perform(
            ViewActions.replaceText("flutter"),
            ViewActions.closeSoftKeyboard(),
            ViewActions.pressImeActionButton()
        )
        activityRule.scenario.onActivity {
            compareBitmap(it, name = "normal_state_search_screen")
        }
        Thread.sleep(1024)
        activityRule.scenario.onActivity {
            compareBitmap(it, name = "searching_state_search_screen")
        }
        val searchInputText = device.findObject(
            By.text("flutter/samples")
        ).click()
        //waitForView(ViewMatchers.withText("flutter/samples")).perform(ViewActions.click())
        //onView(ViewMatchers.withText("flutter/samples")).perform(ViewActions.click())
        Thread.sleep(256)
        activityRule.scenario.onActivity {
            compareBitmap(it, name = "normal_state_detail_screen")
        }
    }


    private fun searchForView(viewMatcher: Matcher<View>): ViewAction {
        return object : ViewAction {
            override fun getConstraints() = isRoot()
            override fun getDescription() = "search for view with $viewMatcher in the root view"

            override fun perform(uiController: UiController, view: View) {
                TreeIterables.breadthFirstViewTraversal(view).forEach {
                    if (viewMatcher.matches(it)) {
                        return
                    }
                }

                throw NoMatchingViewException.Builder()
                    .withRootView(view)
                    .withViewMatcher(viewMatcher)
                    .build()
            }
        }
    }

    fun waitForView(
        vararg viewMatchers: Matcher<View>,
        retries: Int = 64,
        wait: Long = 1000L,
    ): ViewInteraction {
        require(retries > 0 && wait > 0)
        val viewMatcher = allOf(*viewMatchers)
        for (i in 0 until retries) {
            try {
                onView(isRoot()).perform(searchForView(viewMatcher))
                break
            } catch (e: Exception) {
                if (i == retries) {
                    throw Exception("${e.message} after ${retries * wait}ms: ${e.stackTraceToString()}")
                }

                Thread.sleep(wait)
            }
        }
        return onView(viewMatcher)
    }

    fun captureView(view: View, window: Window, bitmapCallback: (Bitmap) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Above Android O, use PixelCopy
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val location = IntArray(2)
            view.getLocationInWindow(location)
            PixelCopy.request(
                window,
                Rect(location[0], location[1], location[0] + view.width, location[1] + view.height),
                bitmap,
                {
                    if (it == PixelCopy.SUCCESS) {
                        bitmapCallback.invoke(bitmap)
                    }
                },
                Handler(Looper.getMainLooper())
            )
        } else {
            val tBitmap = Bitmap.createBitmap(
                view.width, view.height, Bitmap.Config.RGB_565
            )
            val canvas = Canvas(tBitmap)
            view.draw(canvas)
            canvas.setBitmap(null)
            bitmapCallback.invoke(tBitmap)
        }
    }

    fun compareBitmap(activity: Activity, name: String) {
        captureView(activity.findViewById(android.R.id.content), activity.window) { bm ->
            compareScreenshot(bm, name = name)
        }
    }

    fun compareBitmap(fragment: Fragment, name: String) {
        captureView(fragment.requireView(), fragment.requireActivity().window) { bm ->
            compareScreenshot(bm, name = name)
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

    @Provides
    @ViewModelScoped
    fun provideViewModelState(): List<RepositoryModel> {
        val jsonRepositories = JSONObject(RepositoriesFakeJson).optJSONArray("items")

        val repositories = mutableListOf<RepositoryModel>()

        // 検索結果がない場合は空のリストを返す
        // Toastか何か出さないと検索終わったかわかりにくい
        if (jsonRepositories == null || jsonRepositories.length() == 0) {
            return emptyList()
        }

        //JSONからRepositoriesに変換する
        for (i in 0 until jsonRepositories.length()) {
            val jsonRepository = jsonRepositories.optJSONObject(i)!!

            repositories.add(RepositoryModel.fromJson(jsonRepository))
        }
        return emptyList()
    }
}