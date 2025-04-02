package top.fifthlight.authproxy.mixin;

import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.logging.LogUtils;
import net.minecraft.server.Main;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Mixin(Main.class)
public abstract class MainMixin {
    @Redirect(
            method = "main",
            at = @At(
                    value = "NEW",
                    target = "Lcom/mojang/authlib/yggdrasil/YggdrasilAuthenticationService;"
            )
    )
    private static YggdrasilAuthenticationService createAuthenticationService(Proxy proxy) {
        Logger logger = LogUtils.getLogger();

        String socksProxyHost = System.getProperty("socksProxyHost");
        String socksProxyPort = System.getProperty("socksProxyPort");
        String httpProxyHost = System.getProperty("http.proxyHost");
        String httpProxyPort = System.getProperty("http.proxyPort");
        String httpsProxyHost = System.getProperty("https.proxyHost");
        String httpsProxyPort = System.getProperty("https.proxyPort");

        // First, try SOCKS5
        if (socksProxyHost != null && socksProxyPort != null) {
            try {
                int port = Integer.parseInt(socksProxyPort);
                logger.info("Use SOCKS proxy: {}:{}", socksProxyPort, port);
                return new YggdrasilAuthenticationService(
                        new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(socksProxyHost, port))
                );
            } catch (NumberFormatException e) {
                logger.warn("Invalid SOCKS proxy port: {}", socksProxyPort);
            }
        }

        // Second try HTTPS
        if (httpsProxyHost != null && httpsProxyPort != null) {
            try {
                int port = Integer.parseInt(httpsProxyPort);
                logger.info("Use HTTPS proxy: {}:{}", httpsProxyHost, port);
                return new YggdrasilAuthenticationService(
                        new Proxy(Proxy.Type.HTTP, new InetSocketAddress(httpsProxyHost, port))
                );
            } catch (NumberFormatException e) {
                logger.warn("Invalid HTTPS proxy port: {}", httpsProxyPort);
            }
        }

        // Third try HTTP
        if (httpProxyHost != null && httpProxyPort != null) {
            try {
                int port = Integer.parseInt(httpProxyPort);
                logger.info("Use HTTP proxy: {}:{}", httpProxyPort, port);
                return new YggdrasilAuthenticationService(
                        new Proxy(Proxy.Type.HTTP, new InetSocketAddress(httpProxyHost, port))
                );
            } catch (NumberFormatException e) {
                logger.warn("Invalid HTTP proxy port: {}", httpProxyPort);
            }
        }

        // Fallback to no proxy
        logger.info("No proxy used");
        return new YggdrasilAuthenticationService(Proxy.NO_PROXY);
    }
}
