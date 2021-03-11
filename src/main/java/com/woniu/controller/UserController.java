package com.woniu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.generator.config.IFileCreate;
import com.woniu.model.Role;
import com.woniu.model.UserInfo;
import com.woniu.service.RoleService;
import com.woniu.service.UserInfoService;
import com.woniu.util.*;
import com.woniu.model.User;
import com.woniu.service.UserService;
import com.woniu.vo.Tadd;
import com.woniu.vo.UserInfoVo;
import com.woniu.vo.UserVo;
import com.woniu.vo.cardYz;
import io.swagger.annotations.Api;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.apache.velocity.shaded.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    private UserInfoService userInfoService;


    //注册方法
    @PostMapping("register")
    public Result register(@RequestBody UserVo userVo) {
        //调用查询redis的方法 获取验证码
        String s = userService.gitNoteRedis(userVo.getTel());
        //如果等于1 代表为null 那么验证码时效或者没有点击验证码按钮
        if (s.equals(userVo.getResearch())) {
            User user = new User();
            String salt = SaltUtils.getSalt(8);
            Md5Hash md5Hash = new Md5Hash(userVo.getPassword(), salt, 1024);
            user.setNickname(userVo.getNickname());
            user.setTel(userVo.getTel());
            user.setPassword(md5Hash.toHex());
            user.setSalt(salt);


            boolean save = userService.save(user);
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            //查询当前用户注册返回id
            wrapper.eq("tel", userVo.getTel());
            User userdb = userService.getOne(wrapper);
            //判断是否用了推荐码
            boolean boll = false;
            //前端设置了选项框属性 为1就是用户 2就是租客
            if (userVo.getRadio().equals("1")) {
                //给用赋予角色
                userService.role(userdb.getUserId(), 6);
                //判断用户是否用了推荐码
                if (!ObjectUtils.isEmpty(userVo.getReferral())) {

                    boll = true;
                }
            } else {
                userService.role(userdb.getUserId(), 5);

            }

            if (boll) {
                int uid = userService.getVerificationCode(userVo.getReferral());
                //从redis查询 如果没查到 自定义返回0
                if (uid != 0) {
                    //把查询出来的当前用户注册的id存入详情表 并且余额+10
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserId(userdb.getUserId());
                    userInfo.setMoney(new BigDecimal(10));
                    userInfoService.save(userInfo);
                    //给推荐人的money+10
                    QueryWrapper<UserInfo> wrapper1 = new QueryWrapper<>();
                    //查询推荐人的余额
                    wrapper1.eq("user_id", uid);
                    UserInfo info = userInfoService.getOne(wrapper1);
                    BigDecimal bigDecimal = new BigDecimal(10);
                    if (info.getMoney() == null) {
                        info.setMoney(bigDecimal);
                        UpdateWrapper<UserInfo> wrapper2 = new UpdateWrapper<>();
                        wrapper2.eq("user_id", uid);
                        userInfoService.update(info, wrapper2);
                    } else {
                        bigDecimal = bigDecimal.add(info.getMoney());
                        info.setMoney(bigDecimal);
                        //给推荐人+10
                        UpdateWrapper<UserInfo> wrapper2 = new UpdateWrapper<>();
                        wrapper2.eq("user_id", uid);
                        userInfoService.update(info, wrapper2);
                    }


                }
            }
            if (save) {
                return new Result(true, StatusCode.OK, "注册成功");
            }
            return new Result(false, StatusCode.ERRORREGISTER, "注册失败");

        } else {
            //如果返回1 代表redis没有查询到数据
            return new Result(true, StatusCode.ERRORREGISTER, "验证码不正确");
        }


    }

    //校验登录昵称是否存在
    @PostMapping("verify")
    public String verify(@RequestBody User user) {
        System.out.println(user.getNickname());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nickname", user.getNickname());
        User userDB = userService.getOne(queryWrapper);
        //判断是否存在
        if (ObjectUtils.isEmpty(userDB)) {
            // 返回1代表可以注册
            return "1";
        } else {
            return "2";
        }
    }

    //校验电话号码是否存在
    @PostMapping("verify1")
    public String verify1(@RequestBody User user) {
        System.out.println(user.getTel());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tel", user.getTel());
        User userDB = userService.getOne(queryWrapper);
        //判断是否存在
        if (ObjectUtils.isEmpty(userDB)) {
            // 返回1代表可以注册
            return "1";
        } else {
            return "2";
        }
    }

    //密码登录
    @PostMapping("loginPassword")
    public Result loginPassword(@RequestBody UserVo userVo) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nickname", userVo.getNickname());
        User userDB = userService.getOne(queryWrapper);
        if (!ObjectUtils.isEmpty(userDB)) {
            Md5Hash md5Hash = new Md5Hash(userVo.getPassword(), userDB.getSalt(), 1024);
            String md5Password = md5Hash.toHex();
            if (userDB.getPassword().equals(md5Password)) {
                //登陆成功查询角色
                Role role = userService.getRole(userDB.getUserId());
                HashMap<String, String> map = new HashMap<>();
                map.put("uid", userDB.getUserId() + "");
                String jwtToken = JWTutil.createJWT(map);
                System.out.println("登录生成的JWT" + jwtToken);
                Tadd tadd = new Tadd();
                tadd.setJwtToken(jwtToken);
                tadd.setUser(userDB);
                tadd.setRoleId(role.getRoleId());
                System.out.println(tadd);
                return new Result(true, StatusCode.OK, "登陆成功", tadd);
            }
            return new Result(false, StatusCode.IncorrectCredentials, "凭证错误");
        }
        return new Result(false, StatusCode.UnknownAccount, "该用户不存在");
    }

    //电话登录
    @PostMapping("loginTel")
    public Result loginTel(@RequestBody UserVo userVo) {
        System.out.println(userVo);
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tel", userVo.getTel());
            User userDB = userService.getOne(queryWrapper);
            if (!ObjectUtils.isEmpty(userDB)) {
                //登陆成功查询角色
                //调用查询redis的方法 获取验证码
                String s = userService.gitNoteRedis(userVo.getTel());
                //如果等于1 代表为null 那么验证码时效或者没有点击验证码按钮
                System.out.println(s+"--------------------");
                if (s.equals(userVo.getResearch())) {
                    System.out.println("进来了");
                Role role = userService.getRole(userDB.getUserId());
                HashMap<String, String> map = new HashMap<>();
                map.put("uid", userDB.getUserId() + "");
                String jwtToken = JWTutil.createJWT(map);
                System.out.println("登录生成的JWT" + jwtToken);
                Tadd tadd = new Tadd();
                tadd.setJwtToken(jwtToken);
                tadd.setRoleId(role.getRoleId());
                tadd.setUser(userDB);

                return new Result(true, StatusCode.OK, "登陆成功", tadd);
            }else{
                    return new Result(true, StatusCode.UnknownAccount, "验证码不正确");
                }

        }
        return new Result(false, StatusCode.UnknownAccount, "账号不存在");
    }





    //上传照片
    // 设置固定的日期格式
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // 将 yml 中的自定义配置注入到这里
    @Value("${gorit.file.root.path}")
    private String filePath;
    // 日志打印
    private Logger log = LoggerFactory.getLogger("FileController");

    // 文件上传 （可以多文件上传）
    @PostMapping("upload")
    public String fileUploads(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {

        // 得到格式化后的日期
        String format = sdf.format(new Date());
        // 获取上传的文件名称
        String fileName = file.getOriginalFilename();
        // 时间 和 日期拼接
        String newFileName = format + "_" + fileName;
        // 得到文件保存的位置以及新文件名
        File dest = new File(filePath + newFileName);

        try {
            // 上传的文件被保存了
            file.transferTo(dest);
            // 打印日志
            log.info("上传成功，当前上传的文件保存在 {}", filePath + newFileName);
            //获取当前用户id
//            HttpServletRequest httpRequest1=(HttpServletRequest)request;
            //获取请求头中的token
//            String token = request.getHeader("token");
//            String uid = JWTutil.vertify(token).getClaim("uid").asString();
//            int userid = Integer.parseInt(uid);
//            boolean authentication = userService.authentication(userid, dest);
//            System.out.println("认证"+authentication);
            System.out.println(filePath + newFileName);
            return filePath + newFileName;

            // 自定义返回的统一的 JSON 格式的数据，可以直接返回这个字符串也是可以的。


        } catch (IOException e) {
            log.error(e.toString());
        }
        // 待完成 —— 文件类型校验工作
        return "上传错误";

    }

    //实名认证信息
    @PostMapping("userInfo")
    public Result userInfo(@RequestBody UserInfo userInfo ,ServletRequest request){
        boolean bo1 = userService.authentication(userInfo.getDl());
        boolean bo2 = userService.authentication(userInfo.getIdcard());
        if (bo1||bo2){
            //转为httprequest
            HttpServletRequest httpRequest=(HttpServletRequest)request;
            //获取请求头中的token
            String token = httpRequest.getHeader("token");
            String uid = JWTutil.vertify(token).getClaim("uid").asString();
            int userid = Integer.parseInt(uid);
            userInfo.setUserId(userid);
            boolean save = userInfoService.save(userInfo);
            User user = new User();
            user.setStatus(1);
            //获取jwt里面的id 然后去新增userInfo
            UpdateWrapper<User> wrapper = new UpdateWrapper<>();
            wrapper.eq("user_id",userid);
            userService.update(user,wrapper);
            return new Result(true, StatusCode.OK,"修改成功");
        }else {
            return new Result(false, StatusCode.ERRORPAGE,"身份认证错误");
        }


    }
    //获取短信验证
    @PostMapping("authCode")
    public String authCode(@RequestBody User user){
//        String authCode = SendCodeUtil.sendCode(user.getTel());
        String authCode = "000000";
        System.out.println("短信验证码: "+authCode);
        userService.noteRedis(user.getTel(),authCode);
        return authCode;
    }

}

