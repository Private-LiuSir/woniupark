package com.woniu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.woniu.model.User;
import com.woniu.service.UserService;
import com.woniu.util.JwtUtils;
import com.woniu.util.Result;
import com.woniu.util.SaltUtils;
import com.woniu.util.SendSMSUtil;
import com.woniu.vo.Tadd;
import com.woniu.vo.UserVo;
import com.woniu.vo.UserVo1;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    //获取所有的用户
    @GetMapping("getUsers")
    public Result getUsers() {
        List<User> users = userService.list(null);
        return new Result(users);
    }


//    //忘记密码
//    @RequestMapping("/sendSMS")
//    public void SendSMS(HttpServletRequest request, HttpServletResponse response) {
//        PrintWriter out = null;
//
//        try {
//            out = response.getWriter();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String phoneNumber = request.getParameter("phone");
//        System.out.println(phoneNumber);
//
//        // 发送短信
//        SendSMSUtil sendSMS = new SendSMSUtil();
//        String result = sendSMS.senSMSUtil(phoneNumber);
//
//        if (result == null || !result.equals("OK")) {// 发送不成功
//            System.out.println("失败");
//        }
//
//        // 获取验证码
//        int code = sendSMS.getCode();
//        out.print(code);
//        // 将数据存入SESSION
//        request.getSession().setAttribute("checkCode", code);
//        // 将验证码生成时间存入SESSION，若超过五分钟则不通过校验
//        // 将验证码生成时间存入SESSION，若超过五分钟则不通过校验
//        request.getSession().setAttribute("createTime", System.currentTimeMillis());
//    }
//
//    // 验证码检测
//    @RequestMapping("/checkCode")
//    public void checkCode(HttpServletRequest request, HttpServletResponse response) {
//        HttpSession session = request.getSession();
//        response.setContentType("text/html;charset=utf8");
//        PrintWriter out = null;
//        try {
//            out = response.getWriter();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String checkCode = request.getParameter("checkCode");
//        String code = String.valueOf(session.getAttribute("checkCode"));
//        if (checkCode.equals(code)) {
//            out.print("验证码正确");
//        } else {
//            out.print("验证码错误");
//        }
//    }

    //修改密码
    @PostMapping("password")
    public Result updatePassword(@RequestBody UserVo userVo) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userVo.getUserId());
        User userDB = userService.getOne(queryWrapper);
//        System.out.println(userDB+"++++++++");
        //如果不为空
        if (!ObjectUtils.isEmpty(userDB)) {
//            System.out.println("jinlail");
            //前端得到的密码，数据库得到的盐值，散列次数
            Md5Hash md5Hash = new Md5Hash(userVo.getPassword1(), userDB.getSalt(), 1024);
            String md5Password = md5Hash.toHex();
//            System.out.println(md5Password);
//            System.out.println(userDB.getSalt());
//            System.out.println(userVo.getPassword1()+"33333333");
//            System.out.println(userVo.getPass()+"2222222");
//            System.out.println(userDB.getPassword());
            //加密后的密码和数据库查询到的密码作比较
            if (userDB.getPassword().equals(md5Password)) {
//                System.out.println("hah");
                UpdateWrapper<User> wrapper = new UpdateWrapper<>();
                wrapper.eq("user_id",userDB.getUserId());
                //设置盐
                String salt = SaltUtils
                        .getSalt(8);
               Md5Hash md5Hash1 = new Md5Hash(userVo.getPass(),salt,1024);
               userDB.setPassword(md5Hash1.toHex());
               userDB.setSalt(salt);
                boolean save = userService.update(userDB,wrapper);
                if (save){
                    return new Result("修改成功");
                }
                return new Result("修改失败");
            }

        }
        return new Result("原密码不符");
    }

    //忘记密码
//    @PostMapping("forgetPassword")
//    public Result forgetPassword(@RequestBody UserVo1 userVo1){
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("tel", userVo1.getTel());
//        User userDB = userService.getOne(queryWrapper);
//        if(userDB.getTel().equals(userVo1.getTel())){
//
//        }
//    }


}