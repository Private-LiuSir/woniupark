package com.woniu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class AliPayConfig {
    // 应用ID,您的APPID,收款账号既是您的APPID对应支付宝账号,开发时使用沙箱提供的APPID，生产环境改成自己的APPID
    public static String APP_ID = "2016110100784723";

    // 商户私钥，
    public static String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC/sWKath6LuLd8MvoMtuvamC92HiRCroxOnlSrGHrl8RZPp2EnluplrTZTPUNBYEtiDygsXCW4OorCyuDxCTbkLS3gT9Ljn4FHMw9IjQkO0QjUCqPXlL+0wqO0587h21HJfOgnk4C83DRWwCi3f9TZPqeux4/vrL+0SLSaD/QMPR82tIv4rmJrSCX7Gy93LGocHX9w16hIEx8Aotax3LDcge+jen5C5VwMpIjA3V6yoZLYxI3xE3RoVVa/iXuZqLJyUVStu1MXL1Xu74dtd51CNmcyT0xcB7sFaiV/O2reOmKfZAaE2ucVvtNC2c01IYd3Nj6YuTZDkqxwzDvpGlf/AgMBAAECggEAB+033JnnTXPH8pTRDjGKMzHT1JiOe0todd/KNIEAo/2CJK0RMWfmLWoqMur3mv52X4ms6Zit3p5dGoz1HEJ7SJgtq3bhi5QO7njICwH5++jc6pe+Cq4V902NI2/qeCBvk1tc5jYNPsIEUAoGJQBeNtWGbY3G/+wuyS+weCZsuwzBj2e9O1gi2PzZNHgb3Xa2+a7pCUe++KkgwNKJ++cnoc4IAFCLrvTgL/UDh4PzKwvp8PWwcX8TUMpIYWNghjGPPNHpKVF4DTKomObEjaLFPqZspTQw72BAFXgsfW2FR8nuP9m/ATAxFf9U2rthkPxNP70CkL4ZrjXmoUgrgOj3EQKBgQD2MRwyv8iWe2lWRNS85NbPqPEOquyc/r+OnDsR/Km3m2XIfVCrGUvpLA78sT6DbHb5LsI8nCHWPSoCXRnm1CIgdsbTU9CCZtU9Qs4b66HyIAvOYfzApTf4cjs4J7GbiTIUqBWIbfJfyA0AWnjIH2Gp645+2A/KBV7G6UzJdhxzXQKBgQDHVHHvghK+arQYU1c7EQ0zomq4K61t3pJYGIW7aJMu0dyw2KaEzboGirJQHsi8AavshZSbF2XsBTt3jnq8fMedqQ8ixSt9Hm5UyyzOqEBUqIbcfFvKSnFYnJr/tK4EfJfjPccjUJubsKeKiqXDHSFUwaotKpatxbR6e7VoJRu/CwKBgE2lw50F+//qQJji8K37Bv1L7WuQ4TvK5SPXhpHvvJ+aNYn8bDrqqZSLPtbYGD1fMk75Pm3bWJi3hc5lCsxQz8qpa6AvaS4XvN2relI4CUXScabQJOWltnd3n9HgQxYpOVBmixQSiUVIVUFtR3bm3ui20KSVBLXU58WYw40u4V5JAoGBAKTOUy2+FBVQT/tU3Oi/XnT0stv3cPtIbSNdkySygA7UAW5Oks928Xn7LxdDfKDFmQqEH6xjmiec4+Aj86LoYHsgjp0m/KAEiyCcpD1xq07T0D14rMQC7NhhE16cWszm3wZJy96NBTuiwcOwgwFhkm9reHLDpbWjo2cp5YFoPRqlAoGAQnS3z2MXrMbDNQhEp8AbMhVEszWQ3Dc9o4Fsgt6JGLyoOKPpulzRZPn96UpEz/ZMjNTO4CKs/Cy4kyT70xaD1I0enuZQ7s/pxRiOLMgslBcgeVTexpaqpPlwH9+rVr1TDTLP1/vviJRO+xxgPHrtt6c+Iy15xfAcuDqXj8IcFh0=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv7FimrYei7i3fDL6DLbr2pgvdh4kQq6MTp5Uqxh65fEWT6dhJ5bqZa02Uz1DQWBLYg8oLFwluDqKwsrg8Qk25C0t4E/S45+BRzMPSI0JDtEI1Aqj15S/tMKjtOfO4dtRyXzoJ5OAvNw0VsAot3/U2T6nrseP76y/tEi0mg/0DD0fNrSL+K5ia0gl+xsvdyxqHB1/cNeoSBMfAKLWsdyw3IHvo3p+QuVcDKSIwN1esqGS2MSN8RN0aFVWv4l7maiyclFUrbtTFy9V7u+HbXedQjZnMk9MXAe7BWolfztq3jpin2QGhNrnFb7TQtnNNSGHdzY+mLk2Q5KscMw76RpX/wIDAQAB";

    // 服务器异步通知页面路径,需http://格式的完整路径，不能加自定义参数，必须外网可以正常访问
    public static String notify_url = "http://vmavua.natappfree.cc";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能自定义参数，支付成功后返回的页面
    public static String return_url = " http://localhost:8080/#/login";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String CHARSET = "utf-8";

    // 支付宝网关，这是沙箱的网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    // public static String log_path = "https://openapi.alipay.com/gateway.do";
}
