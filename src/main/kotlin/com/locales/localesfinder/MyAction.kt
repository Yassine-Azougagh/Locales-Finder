package com.locales.localesfinder

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.SearchTextField
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import com.intellij.ui.util.preferredHeight
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants


class MyAction() : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val project = e.project ?: return

        if (file == null) {
            Messages.showErrorDialog(project, "No file selected.", "Error")
            return
        }

        if ("json" != file.extension) {
            Messages.showInfoMessage(project, "The selected file should be a json file", "Extract Locales")
        }

        val extractedLocales = extractLocales(file.inputStream);
        println(extractedLocales)
        displayLocalesInToolWindow(extractedLocales, e)

    }

    private fun displayLocalesInToolWindow(extractedLocales: List<String>, e: AnActionEvent) {
        val filteredLocales = ArrayList(extractedLocales)
        val project = e.project ?: return

        val toolWindowManager = ToolWindowManager.getInstance(project)
        val myToolWindow = toolWindowManager.getToolWindow("Locales Extraction Window")

        myToolWindow?.show() {
            println("Tool window shown!")
            val contents = myToolWindow.contentManager.contents
            if (contents.isNotEmpty()) {
                val component = contents[0].component
                component.removeAll()
                val table =  createDataTable(filteredLocales)
                addExctractedLocalesTitle(component)
                addSearchBar(component, extractedLocales, filteredLocales, project,table)
                addDataTable(component, filteredLocales, project, table)

            }
        }

        //show locales in tool window

//        val myToolWindowService : MyToolWindowService = project.getService(MyToolWindowService::class.java)
//        myToolWindowService.fillToolWindow(table)

    }

    private fun addDataTable(component: JComponent, locales: List<String>, project: Project, table: JBTable) : JBTable{
        //cell event double click handler
        table.addMouseListener(CopyPathListner(table, project))
        showDataTable(table, component)

        return table
    }

    private fun showDataTable(table: JBTable, component: JComponent) {
        val panel = JPanel()
        panel.setSize(730, 370)
        val scrollPane = JBScrollPane(table)
        scrollPane.preferredHeight = 500
        component.add(scrollPane)
    }

    private fun createDataTable(locales: List<String>) : JBTable{
        //data model creation
        val localesTableModel = LocalesTableModel()
        localesTableModel.setLocales(locales)

        //table creation
        val table = JBTable(localesTableModel)
        table.fillsViewportHeight = true
        table.tableHeader.resizingAllowed = true
        table.cellSelectionEnabled = true
        table.columnModel.getColumn(0).preferredWidth = 400
        table.columnModel.getColumn(0).resizable = true
        table.columnModel.getColumn(1).preferredWidth = 150
        table.columnModel.getColumn(1).resizable = true

        return table
    }

    private fun addExctractedLocalesTitle(component: JComponent) {
        val panel2 = JPanel()
        val title = JLabel("Extracted Locales (double click on row to copy locales to clipboard)", AllIcons.General.Information, SwingConstants.CENTER)
        panel2.add(title)
        component.add(panel2)
    }

    private fun addSearchBar(component: JComponent, locales: List<String>, filteredLocales: List<String>, project: Project, table: JBTable) {
        val panel3 = JPanel()
        val searchTextField = SearchTextField()
        searchTextField.addDocumentListener(SearchListner(searchTextField, locales, filteredLocales, project, table))
        panel3.add(searchTextField)
        component.add(panel3)
    }

    private fun extractLocales(inputStream: InputStream): List<String>{
        val content = BufferedReader(InputStreamReader(inputStream)).readText();
        val mapper = ObjectMapper()
        val rootNode = mapper.readTree(content) ?: return arrayListOf()
        rootNode.traverse()
        val locales = arrayListOf<String>()
        val path = findLocales(rootNode, locales, "")
        return path
    }

    private fun findLocales(rootNode: JsonNode, locales: ArrayList<String>, baseKey: String): List<String> {
        val result = arrayListOf<String>()

        rootNode.fields().forEach { field ->
            if(field.value.isTextual) {
                if (locales.isNotEmpty()){
                    val lastElement = locales.last().substringBefore(" - ")
                    result.add(baseKey.plus(lastElement.plus(field.key.plus(" - " + field.value.textValue()))))

                }else{
                    result.add(field.key.plus(" - " + field.value.textValue()))
                }
            }else if( field.value.isObject){
                val deepLevelLocales = findLocales(field.value, locales, field.key.plus("."))
                deepLevelLocales.forEach { locale ->
                    result.add(field.key.plus(".").plus(locale))
                }
            }
        }
        return result
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = (file != null)
    }
}