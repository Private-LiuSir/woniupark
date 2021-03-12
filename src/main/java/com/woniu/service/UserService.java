package com.woniu.service;

import com.woniu.model.Permission;
import com.woniu.model.Role;
import com.woniu.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxiaoxian
 * @since 2021-03-05
 */
public interface UserService extends IService<User> {
    void role(int uid,int rid);
    //存推荐码在redis里面
    String  verificationCode( int uid);
    //从Redis里面取推荐码数据 根据推荐码取用户id
    int getVerificationCode(String code);
    //把图片路径存入session
    boolean authentication( String path);
    //查询角色
    Role getRole(int uid);
    //验证码存redis
    void noteRedis(String tel,String authCode);
    //redis取出验证码
    String gitNoteRedis(String tel);




    public List<Permission> getPermissions(String username, Integer pid);

}
