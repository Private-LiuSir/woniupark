package com.woniu.DTO;

public class StatusCode {
    public static final Integer OK=20000;
    public static final Integer Error=20001;
    public static final Integer ERRORLOGIN=20002;//登录失败
    public static final Integer ERRORREGISTER=20003;//注册失败
    public static final Integer ERRORPAGE=20004;    //分页模糊查询失败
    public static final Integer MUCHNAME=20005;   //注册名字重复
    public static final Integer UnknownAccount=20006; //未知账户
    public static final Integer IncorrectCredentials=20007; //凭证错误异常

}
