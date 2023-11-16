package cn.itcast.order.service;

import cn.itcast.order.mapper.OrderMapper;
import cn.itcast.order.pojo.Order;
import org.example.feign.ciients.UserClient;
import org.example.feign.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserClient userClient;

    public Order queryOrderById(Long orderId) {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
        //用fegin远程调用
        User user = userClient.findById(order.getUserId());
        //封装user到order
        order.setUser(user);
        // 4.返回
        return order;



//        // 1.查询订单
//        Order order = orderMapper.findById(orderId);
//        //2.1利用restTemplate发起http请求，查询用户
//        //2.2利用url来查询
//        String url="http://userserver/user/"+order.getUserId();
//        //2.3发送http请求，实现远程调用
//        User user = restTemplate.getForObject(url, User.class);
//        order.setUser(user);
//        // 4.返回
//        return order;
    }
}
