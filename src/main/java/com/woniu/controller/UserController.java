package com.woniu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.woniu.DTO.Result;
import com.woniu.DTO.StatusCode;
import com.woniu.model.Plot;
import com.woniu.model.User;
import com.woniu.service.PlotService;
import com.woniu.service.UserService;
import com.woniu.util.JWTutil;
import com.woniu.util.Mysalt;
import com.woniu.vo.Tadd;
import com.woniu.vo.UserVo;
import com.woniu.vo.plotVo;
import com.woniu.vo.wuyeVo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

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
    @Resource
    private PlotService plotService;

    @PostMapping("register")
    public Result register11(@RequestBody wuyeVo wuyeVo){
        System.out.println("register==="+wuyeVo);
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username",userVo.getUsername());
       // User userDB = userService.getOne(queryWrapper);
       // if(ObjectUtils.isEmpty(userDB)){
            User user=new User();
            String salt = Mysalt.getSalt(8);
            Md5Hash md5Hash=new Md5Hash(wuyeVo.getPassword(),salt,1024);
            user.setNickname(wuyeVo.getNickname());
            user.setPassword(md5Hash.toHex());
            user.setSalt(salt);

            userService.save(user);
            Plot plot=new Plot();
            plot.setPlotAddress(wuyeVo.getPlotAddress());
            plot.setPlotName(wuyeVo.getPlotName());
           plotService.save(plot);
            //if(save){
                return new Result(true, StatusCode.OK,"注册成功");
           // }
           // return new Result(false, StatusCode.ERRORREGISTER,"注册失败");
        }
       // return new Result(false, StatusCode.MUCHNAME,"注册名重复");



//    @PostMapping("login")
//    public Result login(@RequestBody UserVo userVo){
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username",userVo.getUsername());
//        User userDB = userService.getOne(queryWrapper);
//
//        if(!ObjectUtils.isEmpty(userDB)){
//            Md5Hash md5Hash = new Md5Hash(userVo.getPassword(), userDB.getSalt(), 1024);
//            String md5Password = md5Hash.toHex();
//            if(userDB.getPassword().equals(md5Password)){
//                //登陆成功
//                HashMap<String, String> map = new HashMap<>();
//                map.put("username",userVo.getUsername());
////                map.put("id",userDB.getId().toString());
//                String jwtToken = JWTutil.createJWT(map);
//                Tadd tadd=new Tadd();
//                tadd.setJwtToken(jwtToken);
//                tadd.setUser(userDB);
//                return new Result(true, StatusCode.OK,"登陆成功",tadd);
//            }
//            return new Result(false, StatusCode.IncorrectCredentials,"凭证错误");
//        }
//        return new Result(false, StatusCode.UnknownAccount,"该用户不存在");
//    }



}

