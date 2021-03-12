package com.woniu.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;

import com.woniu.config.AliPayConfig;
import com.woniu.mapper.UserInfoMapper;
import com.woniu.model.UserInfo;
import com.woniu.service.UserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@RestController

public class tsController {

    @Resource
    AliPayConfig AlipayConfig;

    @Resource
    private UserInfoMapper userInfoMapper;

    private Integer a=null;

//    @RequestMapping("/alipay")
    @GetMapping("/alipay/{outTradeNo}/{subjects}/{totalAmount}/{bodys}")
    public String payController(@PathVariable String outTradeNo, @PathVariable String subjects, @PathVariable String totalAmount,@PathVariable  String bodys) throws IOException {


        // 获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.APP_ID, AlipayConfig.APP_PRIVATE_KEY, "json", AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.sign_type);

        // 设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = outTradeNo;
        // 付款金额，必填
        String total_amount = totalAmount;
        a= Integer.valueOf(totalAmount);
        // 订单名称，必填
        String subject = subjects;
        // 商品描述，可空
        String body = bodys;

//        UserInfo user = userInfoMapper.selectById(4);
        //String money=user.getMoney()+totalAmount;
       // BigDecimal money1 = user.getMoney();
        // double v = user.getMoney().doubleValue();
      // double money=v+Double.parseDouble(total_amount);

//        user.setMoney(new BigDecimal(money));
//        userInfoMapper.updateById(user);


        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        // 请求
        String form = null;
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return form;
    }


//    支付宝成功后
    @GetMapping("success")
    public void  success(Integer id,UserInfo userInfo){
        UserInfo user = userInfoMapper.selectById(4);
        BigDecimal money1 = user.getMoney();
        Integer money2=money1.intValue();
        int money=a+money2;
        user.setMoney(new BigDecimal(money));
        userInfoMapper.updateById(user);
       // System.out.println("钱的值"+a);
        System.out.println("--------支付宝来了----------");
    }





}
