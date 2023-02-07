import java.io.OutputStream

const val CS_URI_DES = "com.brightk.cs.core.annotation.CsUri"

fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}