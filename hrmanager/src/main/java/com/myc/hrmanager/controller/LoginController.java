package com.myc.hrmanager.controller;



import com.myc.hrmanager.config.VerificationCode;
import com.myc.hrmanager.model.RespBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;


/**
 *
 * @Author
 * @Description
 */

@RestController
public class LoginController {
    @GetMapping("/login")
    public RespBean login() {
        return RespBean.error("尚未登录，请登录!");
    }
    //验证码：通过工具生成并且通过流输出到前端
    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        VerificationCode code = new VerificationCode();
//        图片
        BufferedImage image = code.getImage();
//        图片上的文本
        String text = code.getText();
//        文本存到session里面
//        后面会从session中取出来和请求输入的验证码进行比较
        HttpSession session = request.getSession(true);
        session.setAttribute("verify_code", text);
//        写出去
        VerificationCode.output(image,resp.getOutputStream());
    }
//    ======================over1===========================================================

}
