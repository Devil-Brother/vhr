package com.myc.hrmanager.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.myc.hrmanager.model.Hr;
import com.myc.hrmanager.model.RespBean;
import com.myc.hrmanager.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    HrService hrService;
    @Autowired
    CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;
    @Autowired
    CustomUrlDecisionManager customUrlDecisionManager;
    //??????
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // ??????????????????????????????
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
// ?????????????????? UserDetailsService ????????? getDefaultUserDetailsService() ??????
        auth.userDetailsService(hrService);
    }
    //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    @Override
    public void configure(WebSecurity web) throws Exception {
//        ???????????????????????????
        web.ignoring().antMatchers("/css/**", "/js/**", "/index.html", "/img/**", "/fonts/**", "/favicon.ico", "/verifyCode");
    }

    /**
     * ???????????????????????????????????????????????????
     * @return
     * @throws Exception
     */
    @Bean
    LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
//        ????????????????????????
        loginFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    Hr hr = (Hr) authentication.getPrincipal();
                    hr.setPassword(null);
                    RespBean ok = RespBean.ok("????????????!", hr);
                    String s = new ObjectMapper().writeValueAsString(ok);
                    out.write(s);
                    out.flush();
                    out.close();
                }
        );
        //???????????????????????????????????????
        loginFilter.setAuthenticationFailureHandler((request, response, exception) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    RespBean respBean = RespBean.error(exception.getMessage());
                    if (exception instanceof LockedException) {
                        respBean.setMsg("????????????????????????????????????!");
                    } else if (exception instanceof CredentialsExpiredException) {
                        respBean.setMsg("?????????????????????????????????!");
                    } else if (exception instanceof AccountExpiredException) {
                        respBean.setMsg("?????????????????????????????????!");
                    } else if (exception instanceof DisabledException) {
                        respBean.setMsg("????????????????????????????????????!");
                    } else if (exception instanceof BadCredentialsException) {
                        respBean.setMsg("???????????????????????????????????????????????????!");
                    }
                    out.write(new ObjectMapper().writeValueAsString(respBean));
                    out.flush();
                    out.close();
                }
        );


        loginFilter.setAuthenticationManager(authenticationManagerBean());
        loginFilter.setFilterProcessesUrl("/doLogin");
        ConcurrentSessionControlAuthenticationStrategy sessionStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        sessionStrategy.setMaximumSessions(1);
        loginFilter.setSessionAuthenticationStrategy(sessionStrategy);
        return loginFilter;
    }

    @Bean
    SessionRegistryImpl sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * ?????????????????????
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(customUrlDecisionManager);
                        object.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                        return object;
                    }
                })
                .and()
                .logout()
                //???????????????????????????
//        ??? LogoutFilter ???????????????????????????????????????????????????????????????????????????????????????????????????????????? LogoutHandler ???????????????????????????????????????
                .logoutSuccessHandler((req, resp, authentication) -> {
                            resp.setContentType("application/json;charset=utf-8");
                            PrintWriter out = resp.getWriter();
                            out.write(new ObjectMapper().writeValueAsString(RespBean.ok("????????????!")));
                            out.flush();
                            out.close();
                        }
                )
                .permitAll()
                .and()
                .csrf().disable().exceptionHandling()
                //?????????????????????????????????????????????????????????
                .authenticationEntryPoint((req, resp, authException) -> {
                            resp.setContentType("application/json;charset=utf-8");
                            resp.setStatus(401);
                            PrintWriter out = resp.getWriter();
                            RespBean respBean = RespBean.error("????????????!");
                            if (authException instanceof InsufficientAuthenticationException) {
                                respBean.setMsg("?????????????????????????????????!");
                            }
                            out.write(new ObjectMapper().writeValueAsString(respBean));
                            out.flush();
                            out.close();
                        }
                );
        http.addFilterAt(new ConcurrentSessionFilter(sessionRegistry(), event -> {
            HttpServletResponse resp = event.getResponse();
            resp.setContentType("application/json;charset=utf-8");
            resp.setStatus(401);
            PrintWriter out = resp.getWriter();
            out.write(new ObjectMapper().writeValueAsString(RespBean.error("??????????????????????????????????????????????????????!")));
            out.flush();
            out.close();
        }), ConcurrentSessionFilter.class);
//        ?????????????????????????????????????????????
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
/*

@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    HrService hrService;

    @Autowired
    CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;

    @Autowired
    CustomUrlDecisionManager customUrlMyDecisionManager;
//    ??????????????????
    @Autowired
    VerificationCodeFilter verificationCodeFilter;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/index.html", "/img/**", "/fonts/**", "/favicon.ico", "/verifyCode");
    }
    //????????????configure?????????hrService?????????
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(hrService);
    }

    //???????????????????????????????????????????????????json??????
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(verificationCodeFilter,UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                //??????????????????????????????????????????????????????
//                .anyRequest().authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(customUrlMyDecisionManager);
                        object.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                        return object;
                    }
                })
                .and()
                //????????????
                .formLogin()
                //?????????????????????username
                .usernameParameter("username")
                //?????????????????????password
                .passwordParameter("password")
                //?????????????????????url??????
                .loginProcessingUrl("/doLogin")
                //??????????????????????????????????????????????????????????????????????????????????????????
                .loginPage("/login")
                //?????????????????????
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                        //?????????????????????????????????json
                        resp.setContentType("application/json;charset=utf-8");
                        //??????????????????
                        PrintWriter out = resp.getWriter();
                        //???????????????hr??????
                        Hr hr = (Hr)authentication.getPrincipal();
                        hr.setPassword(null);
                        RespBean ok = RespBean.ok("???????????????", hr);
                        //???hr???????????????
                        String s = new ObjectMapper().writeValueAsString(ok);
                        //?????????????????????
                        out.write(s);
                        out.flush();
                        out.close();


                    }
                })
                //?????????????????????
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException exception) throws IOException, ServletException {
                        //?????????????????????????????????json
                        resp.setContentType("application/json;charset=utf-8");
                        //??????????????????
                        PrintWriter out = resp.getWriter();
                        RespBean respBean = RespBean.error("???????????????");
                        if(exception instanceof LockedException){
                            respBean.setMsg("???????????????????????????????????????");
                        }else if (exception instanceof CredentialsExpiredException){
                            respBean.setMsg("????????????????????????????????????");
                        }else if (exception instanceof AccountExpiredException){
                            respBean.setMsg("????????????????????????????????????");
                        }else if (exception instanceof DisabledException){
                            respBean.setMsg("???????????????????????????????????????");
                        }else if (exception instanceof BadCredentialsException){
                            respBean.setMsg("??????????????????????????????????????????????????????");
                        }
                        out.write(new ObjectMapper().writeValueAsString(respBean));
                        out.flush();
                        out.close();
                    }
                })
                //??????????????????????????????????????????
                .permitAll()
                .and()
                .logout()
                //????????????????????????
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        out.write(new ObjectMapper().writeValueAsString(RespBean.ok("???????????????")));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and()
                //??????csrf??????
                .csrf().disable();

    }

}
*/
