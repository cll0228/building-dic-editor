package com.lezhi.app.webapp.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Colin Yan on 2016/7/15.
 */
@Controller
@RequestMapping("/")
public class LoginController {

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "goTo", required = false) String goTo) {
        request.setAttribute("goTo", goTo);
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String loginSubmit(HttpServletRequest request, HttpServletResponse response,
                              String username, String password,
                              @RequestParam(value = "goTo", required = false) String goTo) {
        if ("success".equals(username) && "success".equals(password)) {
            request.getSession(true).setAttribute("username", username);
            request.getSession(true).setAttribute("userId", 123321);
            if (StringUtils.isNotBlank(goTo)) {
                if (goTo.startsWith("/data.do"))
                    return "redirect:" + request.getContextPath() + "result.do";
                return "redirect:" + goTo;
            } else
                return "redirect:" + request.getContextPath();
        }

        return "redirect:" + request.getContextPath() + "/login.do?goTo=" + goTo;
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("username");
        return "redirect:" + request.getContextPath() + "/login.do";
    }
}
