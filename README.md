## websocket-mvc-springboot-starter

&emsp;`websocket-mvc-springboot-starter`是一款基于SpringBoot开发的websocket框架，springboot官方提供的websocket框架虽然可以帮助我们快速搭建一个websocket服务；但是对于持续交互通信而言，便捷的通信方式才能让开发者更好的关注业务流程；本框架最大的优势在于，规范通信内容，以url字段映射到对应的处理器，把客户端的消息直接交给对应url的处理器进行处理即可，这和springMVC如出一辙；同时对于发送给客户端的消息，也可以通过JS动态函数调用去处理，这样一来，开发者不需要使用大量的if-else去处理不同的消息类型。只需要开发具体的处理逻辑即可，当然这只是本框架最大的特别，除此之外，还提供拦截器和登录校验处理器等等便捷的功能。

<br>

&emsp;**`websocket-mvc-springboot-starter`实现了类似`spring-mvc`框架类似的方法映射，并且支持了诸多功能增强，极大简化和提高交互，简化消息处理逻辑**。

<br>

&emsp;🚀项目会持续优化迭代，欢迎大家提ISSUE！麻烦大家能给一颗star，您的star是我们持续更新的动力，感谢！

<br>

&emsp;github项目地址：[https://github.com/TomHusky/websocket-mvc-springboot-starter](https://github.com/TomHusky/websocket-mvc-springboot-starter)

&emsp;示例demo：...

## 功能特性

- [1] [定义控制器接口](#定义控制器接口)
- [2] [消息接收格式和发送格式](#消息接收格式和发送格式)
- [3] [拦截器](#拦截器)
- [4] [参数注入和参数校验](#参数注入和参数校验)
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

### 定义控制器接口

&emsp;控制器接口必须使用`@SocketController` 注解,这样才能被扫描到，否则控制器无法使用

`@SocketRequestMapping`注解在类上时，value可以不填，注解在方法上时必填，否则无法找到对应的处理器，类似于springmvc；

下面按理里面控制器对应的路径为 /test/getValue

```java
@SocketController
@SocketRequestMapping("/test")
public class TestController {
    
    @SocketRequestMapping("/getValue")
    public JsonResult<String> getValue() {

        return JsonResult.success("ok");
    }
}
```


### 配置扫描包
```yaml
web-socket-mvc:
  basePackage: io.github.tomhusky.test.controller
```
配置控制器所在的包路径，则在io.github.tomhusky.test.controller所有注解了`@SocketRequestMapping`的控制器都被扫描到。

### 3.消息接收格式和发送格式
>框架严格控制消息传递的内容，如果格式不对，内容将无法正确的传输，所以这个是必须的！

客户端发送消息的内容格式（服务端接收消息格式）

```json
{
    "url":"/test/getValue",
    "body":"hello"
}
```
- url&emsp;&emsp;&nbsp;控制器的地址，用于映射。
- body&emsp;消息内容，可以为任意字符串，推荐使用json

<br>
服务端消息发送内容格式（客户端接收消息格式）

```json
{
  "status": 200,
  "type": 1,
  "errorMsg": "",
  "url": "/test/getValue",
  "body": "hello world"
}
```
- status&emsp;&emsp;&nbsp;&nbsp;状态； 200 成功 ， 500失败 ；仅在type为1时有效
- type&emsp;&emsp;&emsp;&nbsp;响应类型；1 客户端请求返回 ， 2 服务端主动返回
- errorMsg&emsp;错误原因； 仅在status不是200时有效
- url&emsp;&emsp;&emsp;&emsp;&nbsp;消息对应的地址；同消息接收一致
- body&emsp;&emsp;&emsp;消息内容；同消息接收一致

<br>

### 拦截器

#### 回话拦截器

通过实现`CustomerWebSocketHandler`接口，服务端可以实现客户端连接和消息拦截，根据不同是回调进行相应的业务处理，比如连接成功之后保存用户对应的回话；（注意，实现接口的类必须注入到spring容器里面，否则不起作用）
```java
public interface CustomerWebSocketHandler {

    /**
     * 且 WebSocket 连接已打开并可供使用后调用。
     *
     * @param webSocketSession 会话对象
     */
    void afterConnectionEstablished(WebSocketSession webSocketSession);

    /**
     * 消息到达时调用
     *
     * @param webSocketSession 会话对象
     * @param message          消息内容
     */
    void handleMessage(WebSocketSession webSocketSession, TextMessage message);

    /**
     * 在 WebSocket 连接被任一方关闭后或发生传输错误后调用。
     *
     * @param webSocketSession 会话对象
     * @param status           状态码
     */
    void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus status);
}
```

实现接口，加上@Component注解

```java

@Slf4j
@Component
public class WebSocketMsgHandler implements CustomerWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        log.info("------------连接成功:{}-------------", webSocketSession.getId());
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, TextMessage textMessage) {
        log.info("------------收到消息:{}-------------", webSocketSession.getId());
        log.info(textMessage.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {
        log.info("------------连接关闭{}-------------", webSocketSession.getId());
    }
}
```

#### 登录拦截器

springboot默认的连接是不提供登录校验的，不过你也得开放socket接口；为了方便开放人员，框架实现了JWT方式的登录校验拦截器，客户端只需要在连接的时候，加入token，拦截器则会把token注入到请求头的 `Authorization` 字段，这个字段名可以通过配置文件进行修改；在拦截器里面获取token之后进行逻辑校验;

- 继承LoginValidIntercept接口，加入@Component注解,attemptAuthentication是在握手之前被调用的，返回true则允许客户端连接返回false则拒绝连接。 successfulAuthentication方法在通过客户端连接并且握手成功之后被调用；

- 注意 LoginValidIntercept类只能被一个类基础，多个spring会报数量异常，返回不止一个子类；

```java
@Slf4j
@Component
public class TokenValidIntercept extends LoginValidIntercept {

    public static final String TOKEN_HEAD = "Authorization";

    @Override
    public boolean attemptAuthentication(ServerHttpRequest request, ServerHttpResponse response) {
        String token = request.getHeaders().getFirst(TOKEN_HEAD);
        if (token == null || CharSequenceUtil.isBlank(token)) {
            return false;
        }
        // 进行逻辑判断
        return true;
    }

    @Override
    public void successfulAuthentication(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        log.info("握手成功");
    }
}

```
<br>

### 参数注入和参数校验
#### 参数注入
目前只支持单一参数加一个WebSocketSession回话对象的注入，单个参数支持String，Integer等基本数据类型和它们的包装类。对象参数使用json形式进行构建。

单个参数形式
```java
 @SocketRequestMapping("/getValue")
 public JsonResult<String> getValue(String name) {
     System.out.println(name);
     return JsonResult.success("ok");
 }

对应的客户端消息格式为：
{
    "url":"test/getValue",
    "body":"测试"
}
```

对象注入和回话对象注入
```java
 @SocketRequestMapping("/getInfo")
 public JsonResult<String> getValue(TestVo testVo, WebSocketSession webSocketSession) {
     System.out.println(testVo);
     return JsonResult.success("ok");
 }

对应的客户端消息为

{
    "url":"test/getValue",
    "body":{
        "name":"ttt",
        "age":18
    }
}
```

#### 参数校验

参数校验接入spring的`validation`框架，使用和springMvc或者springboot一致即可。

```java
@SocketRequestMapping("/getInfo")
public JsonResult<String> getInfo2(@Valid TestVo testVo, WebSocketSession webSocketSession) {
    System.out.println(testVo);
    return JsonResult.success("ok");
}


@Data
public class TestVo {
	
    @NotBlank(message = "name不能为空")
    private String name;

    @NotNull(message = "age不能为空")
    private Integer age;

    private String msg;
	
}

前端发送消息格式

{
    "url":"test/getValue",
    "body":{
        "name":"ttt",
        "age":18
    }
}

```
