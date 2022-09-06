package de.ibveecnk.rolladensteuerung_kotlin.helpers

import android.util.Log

const val TAG = "de.ibveecnk.rolladensteuerung"

object Logger {
    private fun getCaller(): String {
        val trace = Throwable().stackTrace[3];
        return "${trace.className.substringAfterLast('.')}::${trace.methodName}"
    }

    private fun formatText(text: String) = "${getCaller()}: $text"

    fun info(text: String) = Log.i(TAG, formatText(text))
    fun error(text: String) = Log.e(TAG, formatText(text))
    fun error(ex: Exception) = ex.message?.let { formatText(it) }?.let { Log.e(TAG, it) }
}
