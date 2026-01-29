package com.locales.localesfinder

import com.intellij.openapi.project.Project
import com.intellij.ui.DocumentAdapter
import com.intellij.ui.SearchTextField
import com.intellij.ui.table.JBTable
import java.util.stream.Collectors
import javax.swing.event.DocumentEvent

class SearchListner(private val searchTextField: SearchTextField, private val locales: List<String>, private var filteredLocales: List<String>, private val project: Project, private val table: JBTable) : DocumentAdapter() {
    override fun textChanged(e: DocumentEvent) {
        val searchString = searchTextField.text
        println("Search String is : $searchString")

        filteredLocales = if(searchTextField.text.isNotEmpty()) {
            locales.stream().filter { locale -> locale.lowercase().contains(searchString.lowercase()) }.collect(Collectors.toList())
        } else locales
        println("Filtered locales: $filteredLocales")


        val model = table.model as LocalesTableModel
        model.setLocales(filteredLocales)
        model.fireTableDataChanged()
    }


}