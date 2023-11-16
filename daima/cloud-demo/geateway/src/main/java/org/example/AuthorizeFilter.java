package org.example;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
//网关自定义过滤器
//@Order(-1)//用注解设置过滤器的优先等级，数字越小优先级越高，范围：（-2147483647-2149483647）
@Component
public class AuthorizeFilter implements GlobalFilter,Order{
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1：获取请求参数
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> params = request.getQueryParams();
        //2：获取参数中的authorization参数
        String auth = params.getFirst("anthorization");
        //判断参数值是否等于admin
        if ("admin".equals(auth)){
            //4:是，放行
            return chain.filter(exchange);
        }
        //5：否，拦截
        //5.1：设置状态码为未登录
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED) ;
    //5.2：拦截请求
        return exchange.getResponse().setComplete();
    }
//用接口设置过滤器的优先级
    @Override
    public int value() {
        return -1;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
