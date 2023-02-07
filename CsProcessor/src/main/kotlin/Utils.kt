import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSValueArgument
import java.io.OutputStream

const val CS_URI_DES = "com.brightk.cs.core.annotation.CsUri"

fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}
fun Sequence<KSAnnotation>.getAnnotation(target: String): KSAnnotation {
    return getAnnotationIfExist(target) ?: throw NoSuchElementException("Sequence contains no element matching the predicate.")
}
fun Sequence<KSAnnotation>.getAnnotationIfExist(target: String): KSAnnotation? {
    for (element in this) if (element.shortName.asString() == target) return element
    return null
}

fun Sequence<KSAnnotation>.hasAnnotation(target: String): Boolean {
    for (element in this) if (element.shortName.asString() == target) return true
    return false
}

@Suppress("UNCHECKED_CAST")
fun <T> List<KSValueArgument>.getParameterValue(target: String): T {
    return getParameterValueIfExist(target) ?: throw NoSuchElementException("Sequence contains no element matching the predicate.")
}


@Suppress("UNCHECKED_CAST")
fun <T> List<KSValueArgument>.getParameterValueIfExist(target: String): T? {
    for (element in this) if (element.name?.asString() == target) (element.value as? T)?.let { return it }
    return null
}