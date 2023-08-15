package com.example.gateway.Filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    // 令牌头名字
    private static final String AUTHORIZE_TOKEN = "Authorization";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取Request、Response对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 获取请求的URI
        String path = request.getURI().getPath();

        // 如果是登录、goods等开放的微服务[这里的goods部分开放],则直接放行,这里不做完整演示，完整演示需要设计一套权限系统
        // 未登录下只放行登录和搜索
        if (path.startsWith("/user/login") || path.startsWith("/brand/search/")) {
            // 放行
            Mono<Void> filter = chain.filter(exchange);
            return filter;
        }

        // 从头文件中获取的令牌信息
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        // 如果为true：说明令牌在头文件中， false：令牌不在头文件中，将令牌封装入头文件，再传递给其他微服务
        boolean hasToken = true;

        // 如果头文件中没有令牌信息，则从请求参数中获取
        if (StringUtils.isEmpty(token)) {
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
            hasToken = false;
        }

        // 如果为空，则输出错误代码
        if (StringUtils.isEmpty(token)) {
            // 设置方法不允许被访问，405错误代码
            response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
            return response.setComplete();
        }

        // 如果不为空，则解析令牌数据
        /*
        try {
            Claims claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            // 解析失败，响应401错误
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

         */
        // 放行之前，将令牌封装到头文件中(这一步是为了方便AUTH2校验令牌)
        request.mutate().header(AUTHORIZE_TOKEN, token);

        // 放行
        return chain.filter(exchange);
    }

    /**
     * 过滤器执行顺序
     *
     * @return
     */
    @Override
    public int getOrder() {
        // 首位
        return 0;
    }
}
