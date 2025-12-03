package com.locales.localesfinder

import com.intellij.openapi.ide.CopyPasteManager
import java.awt.datatransfer.StringSelection

class Tools {
    fun copyToClipboard(text: String) {
        val contents = StringSelection(text)
        CopyPasteManager.getInstance().setContents(contents)
    }
}