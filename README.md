## websocket-mvc-springboot-starter

&emsp;`websocket-mvc-springboot-starter`是一款基于SpringBoot开发的websocket框架，springboot官方提供的websocket框架虽然可以帮助我们快速搭建一个websocket服务；但是对于持续交互通信而言，便捷的通信方式才能让开发者更好的关注业务流程；本框架最大的优势在于，规范通信内容，以url字段映射到对应的处理器，把客户端的消息直接交给对应url的处理器进行处理即可，这和springMVC如出一辙；同时对于发送给客户端的消息，也可以通过JS动态函数调用去处理，这样一来，开发者不需要使用大量的if-else去处理不同的消息类型。只需要开发具体的处理逻辑即可，当然这只是本框架最大的特别，除此之外，还提供拦截器和登录校验处理器等等便捷的功能。

<br>

**`websocket-mvc-springboot-starter`实现了类似`spring-mvc`框架类似的方法映射，并且支持了诸多功能增强，极大简化和提高交互，简化消息处理逻辑**。

<br>

🚀项目会持续优化迭代，欢迎大家提ISSUE！麻烦大家能给一颗star，您的star是我们持续更新的动力，感谢！

<br>

github项目地址：[https://github.com/TomHusky/websocket-mvc-springboot-starter](https://github.com/TomHusky/websocket-mvc-springboot-starter)

示例demo：...

## 功能特性

- [1] [消息方法映射](#消息方法映射)
- [2] [握手token校验](#注解式拦截器)
- [3] [拦截器](#拦截器)
- [4] [@Valid参数校验](#日志打印)
  <br>

## 快速使用

### 引入依赖
**建议使用最新版本，功能最全，bug最少**

```xml
<dependency>
    <groupId>io.github.tomhusky</groupId>
   <artifactId>websocket-mvc-springboot-starter</artifactId>
   <version>1.0.3</version>
</dependency>
```
<br>

### 1.定义控制器接口

控制器接口必须使用`@SocketRequestMapping` 注解,这样才能被扫描到，否则控制器无法使用。@SocketRequestMapping注解在类上时，value可以不填，注解在方法上时必填，否则无法找到对应的处理器。类似于springmvc；

```java
@SocketRequestMapping("/test")
public class TestController {
    
    @SocketRequestMapping("/getValue")
    public JsonResult<String> getValue() {

        return JsonResult.success("ok");
    }
}
```


### 2.配置扫描包
```yaml
web-socket-mvc:
  basePackage: io.github.tomhusky.test.controller
```
配置控制器所在的包路径，则在io.github.tomhusky.test.controller所有注解了`@SocketRequestMapping`的控制器都被扫描到。

### 3.消息接收格式和发送格式

消息接收格式（客户端发送消息的内容格式）

```json
{
    "url":"/test/getValue",
    "body":"hello"
}
```
框架严格控制消息传递的内容，如果格式不对，内容将无法正确的传输，所以这个是必须的！
- url&emsp;&emsp;&nbsp;控制器的地址，用于映射。
- body&emsp;消息内容，可以为任意字符串，推荐使用json

<br>

消息发送内容格式

```json
{
  "status": 200,
  "type": 1,
  "errorMsg": "",
  "url": "/test/getValue",
  "body": "ok"
}
```
- status&emsp;&emsp;&nbsp;&nbsp;状态； 200 成功 ， 500失败 ；仅在type为1时有效
- type&emsp;&emsp;&emsp;&nbsp;响应类型；1 客户端请求返回 ， 2 服务端主动返回
- errorMsg&emsp;错误原因； 仅在status不是200时有效
- url&emsp;&emsp;&emsp;&emsp;&nbsp;消息对应的地址；同消息接收一致
- body&emsp;&emsp;&emsp;消息内容；同消息接收一致
