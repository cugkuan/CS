package com.k.plugin.transform

import com.k.plugin.CsPluginUtils

class DefaultClassNameFilter : ClassNameFilter {

    private val whiteList = mutableListOf<String>().apply {
        add("kotlin")
        add("org")
        add("androidx")
        add("android")
        add("java")
        add("google")
        add("com/google")
        add("okhttp")
        add("R.class")
        add("META-INF")
        add("com/brightk")
    }

    override fun filter(className: String): Boolean {
        return if (className.endsWith(".class").not()) {
            true
        } else {
            if (CsPluginUtils.scanPackage.isNullOrEmpty()
                    .not() && CsPluginUtils.scanPackage.find { className.startsWith(it) } != null
            ) {
                (className.endsWith("R.class") || className.contains("R$")
                        || className.endsWith("BuildConfig.class"))
            } else {
                (whiteList.find { className.startsWith(it) } != null || className.endsWith("R.class") || className.contains(
                    "R$"
                )
                        || className.endsWith("BuildConfig.class"))
            }
        }
    }

    override fun isTargetClass(className: String) = (className == "com/brightk/cs/CS.class")
}