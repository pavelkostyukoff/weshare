package com.gpbdigital.wishninja.ui.watcher

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by bender on 03/07/2017.
 */
class WishNameToDescriptionWatcher(val maxLength: Int, val overflowListener: (String) -> Unit): TextWatcher {
    var lastWord = ""
    var wishLengthName = 0

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s != null && s.length > maxLength && count >= 1) {
            wishLengthName = s.length
            if (count == 1) {
                wishLengthName--
            }
            wordCount(s.toString())
            overflowListener.invoke(s.substring(wishLengthName - lastWord.length))
        }
    }

    override fun afterTextChanged(s: Editable?) {
        if (s != null && s.length > maxLength) {
            if (s.endsWith(" ")) {
                s.delete(s.length - 1, s.length)
            }
            s.delete(s.length - lastWord.length, s.length)
        }
    }

    fun wordCount(str: String) {
        val sentence = str.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var words = sentence.size
        lastWord = sentence.get(words - 1)
    }
}