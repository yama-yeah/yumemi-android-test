/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.codecheck

import androidx.appcompat.app.AppCompatActivity
import java.util.Date

/**
 * ここからアプリは実行される
 */
class TopActivity : AppCompatActivity(R.layout.activity_top) {

    companion object {
        // 検索した日時
        lateinit var lastSearchDate: Date
    }
}
