package top.brightk.cs.process

data class CsServiceNode(
    val className:String,
    val key:String)

data class CsInterceptorNode(
    val className:String,
    val priority:Int,
    val name:String
)