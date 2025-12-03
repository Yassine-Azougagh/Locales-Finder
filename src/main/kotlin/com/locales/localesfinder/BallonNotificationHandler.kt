package com.locales.localesfinder

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project


class BallonNotificationHandler {

    fun notifyError(project: Project?, content: String, notificationType: NotificationType) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Custom Notification Group")
            .createNotification(content, notificationType)
            .notify(project)
    }
}