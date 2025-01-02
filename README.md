> CS(Component is Service) 一套组件化框架


# 概述
CS 是一个轻量级的组件化框架，支持渐进式的改造。Cs基于组件即服务的思想；把组件看成一个个服务； CS 没有路由的概念，每一次行为（页面跳转，拿取其它组件数据）都是一次 UriRequest。


采用ksp + asm 的方式，编译速度极快。

添加插件依赖和库依赖(插件已经适配了gradle8.0)

```
buildscript {
    dependencies {
      classpath 'top.brightk:cs-plguin:3.0.0'
      classpath 'top.brightk:cs-process:1.0.3'
    }
}
```

每一个 module 添加ksp依赖，请注意，是每一个module ，如果没添加将不会被ksp 扫描

``` 
implemention  'top.brightk:cs:1.1.0'
ksp("top.brightk:cs-process:1.0.3")
```

在 Application  module 中使用插件,和ksp 配置，**请注意由于ksp的工作原理决定，module一定要有java/kotlin代码**

```
apply plugin: 'top.brightk.cs'

ksp{
    arg("application","true")
}
```
如果 Application  module 没有Java/kotlin代码，指定任意模块配置

```
ksp{
    arg("application","true")
}
```



在合适的地方进行Cs的初始化

```java
 CS.init();
```

# 特点
- 轻量级，整个库很小；只有基本的组件服务
- 自动注册；在编译阶段，完成service和 Interceptor的注册，对启动速度没有丝毫影响。
- 采用ksp+asm 组合，极致的编译速度
- 自动检测 Service 配置的 Uri,在编译阶段完成检查
- 所有的组件都是平行独立的。

# 使用

创建 CsService

``` koltin

@CsUri(uri = "qzd://app/container/openScore")
class CsOpenAppScoreService: CsService {
    override fun call(request: UriRequest, listener: OnRequestResultListener?) {
        request.context?.let { context ->
            val type = request.getQueryParameter("type")?:"1"
            val source = request.getQueryParameter("source")?:"0"
            AppScoreActivity.skipToActivity(context, type, source)
            listener?.result(UriRespond.SUCCEED())
        }
    }
}

```
使用

```koltin 
   UriRequestBuild("qzd://app/container/openScore")
                        .addQueryParams("type", type)
                        .addQueryParams("source", source)
                        .call()

```
# 设计思想

CS(Component is Service）；组件即服务，把每个组件看成一个个服务，组件通过 CsService 向外提供服务。由于 Activity的跳转属于 Android层，其启动的方式不停地变化，如ActivityResultLauncher创建时机;启动模式设置等，通过路由写入框架内部不合适；对于Fragment 其复杂的fragmentTransaction更不应该写入框架内部。

CS 框架只完成基本的组件通信能力，真正的业务处理交给对应的 CsService 去完成。CsService 类似于 Servlet,CS 思想类似于微服务架构设计

