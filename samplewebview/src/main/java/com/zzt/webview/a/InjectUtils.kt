package com.zzt.webview.a

import android.content.Context

/**
 * @author: zeting
 * @date: 2021/12/27
 *
 */
object InjectUtils {

    fun Context.injectVConsoleJs(): String? {
        return try {
            val vconsoleJs = resources.assets.open("js/vconsole.min.js").use {
                val buffer = ByteArray(it.available())
                it.read(buffer)
                String(buffer)
            }
            """ $vconsoleJs
                 var vConsole = new VConsole();
            """.trimIndent()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}