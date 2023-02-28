package com.k.plugin.transform

interface ClassNameFilter {
    /**
     * true;进行过滤
     */
    fun filter(className: String): Boolean

    fun isTargetClass(className: String) :Boolean
}