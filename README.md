> CS(Component is Service) 一套组件化框架


# 概述
CS 是一个轻量级的组件化框架，支持渐进式的改造。Cs基于组件即服务的思想；把组件看成一个个服务； CS 没有路由的概念，每一次行为（页面跳转，拿取其它组件数据）都是一次 UriRequest。



添加插件依赖和库依赖

```
buildscript {
    dependencies {
      classpath 'top.brightk.cs:cs-plguin:0.1.0'
    }
}
```

``` 
api 'com.brightk.cs:cs:0.3.6'
```

使用插件

```
apply plugin: 'com.brightk.cs'
```

# 特点

- Service 自动注册；采用了 ASM 技术，在编译阶段，遍历服务，然后生成注册代码，插入对应位置，对启动速度没有丝毫影响。
- 自动检测 Service 配置的 Uri,在编译阶段杜绝了重复 Uri 的可能性
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

CS(Component is Service）；组件即服务，把每个组件看成一个个服务，组件通过 CsService 向外提供服务。由于 Activity的跳转属于 Android层，其启动的方式不停地变化，如ActivityResultLauncher创建时机;启动模式设置等，通过路由写入框架内部不合适；对于Fragment 其复杂的事物更不应该写入框架内部。

CS 框架只处理请求的转发，真正的处理逻辑 交给 对应的 CsService 去处理。CsService 类似于 Servlet,CS 思想类似于微服务架构设计

