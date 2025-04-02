# AuthProxy

一个简单的 Minecraft Fabric mod，允许服务端使用代理服务器进行用户验证。

## 使用方法

运行服务器时给服务器传递以下两类参数中的一类：

- Socks5 代理：`socksProxyHost` 设置为代理服务器的地址，`socksProxyPort` 设置为代理服务器的端口。
- HTTP/HTTPS 代理：`http.proxyHost` 设置为代理服务器的地址，`http.proxyPort` 设置为代理服务器的端口。

然后启动你的服务器，观察日志中是否出现：

- "Use SOCKS proxy: 地址:端口"
- "Use HTTP proxy: 地址:端口"

如果没有设置代理服务器，则会出现：

- "No proxy used"

# 兼容性

只测试过 1.21.5 可以正常使用，其他的版本兼容性尚未测试。
