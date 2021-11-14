package com.aoc4456.radarchart

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider

/*
 * androidx.fragment：fragment-testingライブラリのlaunchFragmentInContainerは、
 * @ AndroidEntryPointでアノテーションが付けられていない内部でハードコードされたアクティビティ
 * （[EmptyFragmentActivity]など）を使用しているため、現在は使用できません。
 *
 * 回避策として、同等のこの関数を使用してください。
 * [HiltTestActivity]をデバッグフォルダーに追加し、
 * このプロジェクトにあるデバッグAndroidManifest.xmlファイルに含める必要があります。
 *
 * 【参考】
 * https://github.com/android/architecture-samples/tree/dev-hilt
 */
inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.FragmentScenarioEmptyFragmentActivityTheme,
    crossinline action: Fragment.() -> Unit = {}
) {
    val startActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra(
        "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY",
        themeResId
    )

    ActivityScenario.launch<HiltTestActivity>(startActivityIntent).onActivity { activity ->
        val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )
        fragment.arguments = fragmentArgs
        activity.supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()

        fragment.action()
    }
}
