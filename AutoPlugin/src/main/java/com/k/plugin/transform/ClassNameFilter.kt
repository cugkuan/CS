package com.k.plugin.transform

interface ClassNameFilter {
    fun filter(className: String): Boolean

    fun isTargetClass(className: String) :Boolean
}