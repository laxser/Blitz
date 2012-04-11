#Blitz

Blitz 是一个开源的基于Servlet 规范和Spring规范的Restful响应框架。通过配置Filter Bean 的方式介入处理请求。
配置运行Blitz的方法



##Quick Start


###How?

在web.ini中加入以下代码
```xml
<filter-name>blitzFilter</filter-name>
  <filter-class>com.laxser.blitz.BlitzFilter</filter-class>
 </filter>
 <filter-mapping>
  <filter-name>blitzFilter</filter-name>
  <url-pattern>/*</url-pattern>
  <dispatcher>REQUEST</dispatcher>
  <dispatcher>FORWARD</dispatcher>
  <dispatcher>INCLUDE</dispatcher>
 </filter-mapping>

```

###A little bit further 

####若要控制并发
```xml
<context-param>
  <param-name>portalExecutorCorePoolSize</param-name>
  <param-value>200</param-value>
 </context-param>
 
```

####需要使用Log4j查看日志

```xml
<listener>
  <listener-class>org.springframework.web.util.Log4jConfigListener</listener-class>
 </listener>
```


####配置忽略Blitz 的模块的方法,在Blitz需要被忽略的模块中配置一个blitz-info.properties

```
blitz.ignore=true
```
###现在 你已经轻松的完成了Blitz平台的搭建～

###Hello World

####1. 编写HelloWorldController 放在Controllers包下面
```java

import com.laxser.blitz.web.Invocation;
import com.laxser.blitz.web.annotation.Path;
import com.laxser.blitz.web.annotation.rest.Get;



@Path("helloworld")
public class FriendController
{
    @Get
    public String sayHelloWorld(Invocation inv) {
    	
    	return "@Hello world";
    }
    
}
```
然后，访问  http://localhost:8080/XXX/helloworld



