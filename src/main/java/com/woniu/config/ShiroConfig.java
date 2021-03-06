package com.woniu.config;

import com.woniu.filter.CustomFilter;
import com.woniu.util.MyRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public Realm realm(){
       MyRealm myRealm = new MyRealm();

       return myRealm;
    }

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(realm());
        return manager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        factoryBean.setSecurityManager(defaultWebSecurityManager());
        Map<String, Filter> filters = factoryBean.getFilters();
        filters.put("jwt",new CustomFilter());
        //创建linkedmap 用于设置拦截的请求
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("/login","anon");
        map.put("/register","anon");
        map.put("/**","anon");
        //
        factoryBean.setFilterChainDefinitionMap(map);
        return factoryBean;
    }
}
