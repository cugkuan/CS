package top.brightk.cs.process

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.security.MessageDigest

fun String.md5(): String {
    // Get an instance of MessageDigest for MD5
    val md = MessageDigest.getInstance("MD5")
    // Apply the MD5 hash function to the input string
    val hashBytes = md.digest(this.toByteArray())
    // Convert the byte array to a hexadecimal string
    return hashBytes.joinToString("") { "%02x".format(it) }
}

private val gson = GsonBuilder()
    .create()
private val escapeRegex = "\"".toRegex()
private val underlineRegex = "_[a-zA-Z]".toRegex()

private fun <T> T.toJsonStr(): String {
    return gson.toJson(this)
}

private fun String.toEscapeStr(): String = replace(escapeRegex) {
    "\\${it.value}"
}

fun <T> T.toTransitContentJson(): String = toJsonStr().toEscapeStr()

fun String.formatNodeName(): String = replaceFirstChar { it.uppercaseChar() }.removeUnderline()

fun String.className2VariableName(): String = replaceFirstChar { it.lowercaseChar() }

private fun String.removeUnderline(): String = replace(underlineRegex) {
    it.value[1].uppercase()
}

fun <T : Any> String.parseFromJson(clazz: Class<T>): T {
    return gson.fromJson(this, clazz)
}

fun <T : Any> String.parseFromJson(): T {
    val token = object : TypeToken<T>() {}
    return gson.fromJson(this, token.type)
}

fun String.getServiceNodes():List<CsServiceNode>{
    return gson.fromJson(this,object :TypeToken<List<CsServiceNode>>(){}.type)
}


