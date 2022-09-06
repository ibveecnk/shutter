package de.ibveecnk.rolladensteuerung_kotlin.helpers

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.R
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import kotlin.concurrent.thread

@SuppressLint("")
object TransitionManager {
    fun default(
        context: AppCompatActivity,
        intent: Intent,
        addToHistory: Boolean = true
    ) {
        fade(context, intent, addToHistory)
    }

    private fun fade(context: AppCompatActivity, intent: Intent, addToHistory: Boolean) {
        customTransition(context, intent, R.anim.abc_fade_in, R.anim.abc_fade_out, addToHistory)
    }

    private fun customTransition(
        context: AppCompatActivity,
        intent: Intent,
        animationIn: Int,
        animationOut: Int,
        addToHistory: Boolean
    ) {
        thread {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(context, intent, null)
            context.overridePendingTransition(animationIn, animationOut)

            // Don't push to history but wait for animation (100ms is sensible)
            if (!addToHistory) {
                Thread.sleep(100)
                context.finish()
            }
        }
    }
}
