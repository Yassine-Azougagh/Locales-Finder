package com.locales.localesfinder

import javax.swing.table.AbstractTableModel

class LocalesTableModel: AbstractTableModel() {
    private val columns = arrayOf("Key chain", "Value")
    private val locales = ArrayList<LocaleDataModel>()

    override fun getRowCount(): Int {
        return locales.size
    }

    override fun getColumnCount(): Int {
        return columns.size
    }

    override fun getColumnName(columnIndex: Int): String {
        return columns[columnIndex]
    }

    override fun getColumnClass(columnIndex: Int): Class<*> {
        return when (columnIndex) {
            0, 1 -> String::class.java

            else -> Any::class.java
        }
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        return when (columnIndex) {
            0 ->
                locales[rowIndex].keyChain

            1 ->
                locales[rowIndex].value

            else -> throw IllegalArgumentException()
        }
    }

    public fun setLocales(locales: List<String>) {
        this.locales.clear()
        this.locales.addAll(formatLocales(locales))
    }

    private fun formatLocales(locales: List<String>): List<LocaleDataModel> {
        val result = ArrayList<LocaleDataModel>()

        locales.forEach { locale ->
            val values = locale.split(" - ")
            if (values.size == 2)
                result.add(LocaleDataModel(values[0], values[1]))
        }

        return result
    }


}