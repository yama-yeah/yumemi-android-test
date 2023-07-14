package jp.co.yumemi.android.codecheck

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import jp.co.yumemi.android.codecheck.ui.search.SearchScreenFragment
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
class ExampleInstrumentedTest : ScreenshotTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun testEvent() {
        val fakeRepositoriesJson = """"""
        val scenario = launchFragmentInHiltContainer<SearchScreenFragment> {

        }
    }
}