package com.nowcoder.controller;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by xuery on 2016/8/20.
 */
//@Controller
public class IndexController {

    public static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value="/",method = RequestMethod.GET)
    @ResponseBody
    public String index(HttpSession httpSession){
        logger.info("VISIT HOME");
        return "hello nowcoder "+httpSession.getAttribute("msg");
    }

    @RequestMapping(value="/profile/{groupid}/{userid}",method = RequestMethod.GET)
    @ResponseBody
    public String profile(@PathVariable("groupid") String groupid,
                          @PathVariable("userid") int userid,
                          @RequestParam(value="type",defaultValue = "1") int type){
        return String.format("Profile page of   groupid %s,userid %d,type %d",groupid,userid,type);
    }

    @RequestMapping(path={"/vm"},method = RequestMethod.GET)
    public String template(Model model){
        model.addAttribute("value1","vvvvvv1");

        List<String> colors = Arrays.asList(new String[]{"RED","GREEN","BLUE"});
        model.addAttribute("colors",colors);

        Map<String,String> map = new HashMap<>();
        for(int i=0;i<4;i++){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("map",map);

        return "home";  //返回到home.html
    }

    @RequestMapping(value="/request")
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          @CookieValue(value = "username",required = false) String sessionId){
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            sb.append(name+"::"+request.getHeader(name)+"<br>");
        }

        if(request.getCookies()!=null){
            for(Cookie cookie:request.getCookies())
            sb.append("cookie:"+cookie+"<br>");
        }

        sb.append("sessionId::" + sessionId);

        response.addCookie(new Cookie("username","asjdjashdasda"));

        return sb.toString();
    }

    /***
     * 重定向  301：永久定向，302：临时重定向
     *
     * 302
     */
    @RequestMapping(value = "/redirect/{code}",method = RequestMethod.GET)
    public RedirectView redirect(@PathVariable("code") String code,
                           HttpSession httpSession){
        httpSession.setAttribute("msg","redirect"+code);
        RedirectView redirectView = new RedirectView("/",true);
        if("301".equals(code)){
            //强制301永久定向
            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return redirectView;

        //return "redirect:/";
    }


    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value="key",required=false) String key){
        if("admin".equals(key)){
            return "hello admin";
        }

        throw new IllegalArgumentException("参数错误");
    }


    /**
     * 用于捕获异常
     * 和上面的throw配套使用
     *
     * 捕获Spring MVC外的Exception或Spring MVC没有处理的Exception
     * @param e
     * @return
     */
    @ExceptionHandler
    @ResponseBody
    public String error(Exception e){
        return "error:"+e.getMessage();
    }
}
