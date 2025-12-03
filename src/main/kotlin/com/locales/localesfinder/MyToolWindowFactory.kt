import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import javax.swing.JLabel
import javax.swing.JPanel

class MyToolWindowFactory : ToolWindowFactory {


    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        // Create a panel with your UI (e.g., a label or table)
        val panel = JPanel()
        panel.add(JLabel("Welcome to locales finder!"))
        val description = JLabel("Click right with your mouse on the locale file and choose extract locales option,\n the file should be json extension")
        panel.add(description)
        // Add the panel to the tool window
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(panel, "", false)
        toolWindow.contentManager.addContent(content)
    }
}