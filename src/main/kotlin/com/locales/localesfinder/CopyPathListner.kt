package com.locales.localesfinder

import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.ui.table.JBTable
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class CopyPathListner(private val table: JBTable, private val project: Project) : MouseAdapter() {


    override fun mouseClicked(e: MouseEvent) {
        if(e.clickCount >= 2 && e.component != null) {
            val selectedRow = table.selectedRow
            val selectedCol = 0// related to column key chain with index 0
            if(selectedRow >= 0 && selectedRow < table.rowCount) {
                val keyChain = table.getValueAt(selectedRow, selectedCol)
                Tools().copyToClipboard(keyChain.toString())
                BallonNotificationHandler().notifyError(project," Value $keyChain is copied to clipboard", NotificationType.INFORMATION)
                println("Value: $keyChain is copied to clipboard")
            }
        }
    }
}