package top.brightk.cs.process.create

import top.brightk.cs.process.md5
import java.io.OutputStream
import java.util.UUID

abstract  class BaseTransfer {

    protected fun getCreateName():String{
       return "Cs_"+UUID.randomUUID().toString().md5()
    }

    protected fun OutputStream.appendText(str: String) {
        write(str.toByteArray())
    }

    protected fun OutputStream.newLine(count: Int = 1) {
        repeat(count) {
            appendText("\n")
        }
    }

    protected fun OutputStream.appendTextWithTab(text: String, count: Int = 1) {
        repeat(count) {
            appendText("\t")
        }
        appendText(text)
    }
}