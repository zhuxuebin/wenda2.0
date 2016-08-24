package com.nowcoder.controller;

import com.nowcoder.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by xuery on 2016/8/23.
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    /**
     * 注册用户
     * @param model
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value="/reg",method = RequestMethod.POST)
    public String reg(Model model,
                      @RequestParam("username")String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rememberme",defaultValue = "false") boolean rememberme,
                      @RequestParam(value = "next",required = false) String next,
                      HttpServletResponse response){
        try {
            Map<String, Object> map = userService.register(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        }catch(Exception e){
            logger.info("登陆异常"+e.getMessage());
            return "login";
        }
    }

    /**
     * 用户登陆
     * @param model
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value="/login",method = RequestMethod.POST)
    public String login(Model model,
                      @RequestParam("username")String username,
                      @RequestParam("password") String password,
                        @RequestParam(value = "rememberme",defaultValue = "false") boolean rememberme,
                        @RequestParam(value = "next",required = false) String next,
                        HttpServletResponse response){
        try {
            Map<String, Object> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        }catch(Exception e){
            logger.info("登陆异常"+e.getMessage());
            return "login";
        }
    }


    /**
     * 跳转到注册登录页面
     * @param model
     * @param next
     * @return
     */
    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
    public String regloginPage(Model model, @RequestParam(value = "next", required = false) String next) {
        model.addAttribute("next", next);
        return "login";
    }

    /**
     * 登出
     * @param ticket
     * @return
     */
    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET,RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "login";
    }



}
