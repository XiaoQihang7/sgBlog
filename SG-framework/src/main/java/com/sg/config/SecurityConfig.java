package com.sg.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers("/login").anonymous()
                //退出不可以设置匿名访问，不要脑抽了
                //这里前端存在问题，浏览器会自动登入，但redis已过期，这时想退出登入也会出现403，但又不会显示登入已过期
                //【这里暂且先设置为可访问，后续有时间再改下前端】
                .antMatchers("/logout").permitAll()
                //设置一些不需要认证就看访问的接口
                .antMatchers(
                        "/article/**"
                ).permitAll()
                .antMatchers(
                        "/category/**"
                ).permitAll()
                .antMatchers(
                        "/user/register"
                ).permitAll()
                .antMatchers("/upload/**").permitAll()
                .antMatchers("/swagger-ui.html/**").permitAll() //必须写/
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/**").permitAll()
                // 除上面外的所有请求全部需要认证即可访问
                .anyRequest().authenticated();


        http.logout().disable();

        //把jwtAuthenticationTokenFilter添加到SpringSecurity的过滤器链中
        //在UsernamePasswordAuthenticationFilter之前执行
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //允许跨域
        http.cors();
    }
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}