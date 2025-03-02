package com.softuniproject.app.web.controller;

import com.softuniproject.app.security.AuthenticationMetadata;
import com.softuniproject.app.user.entity.User;
import com.softuniproject.app.user.service.UserService;
import com.softuniproject.app.web.dto.UserLoginRequest;
import com.softuniproject.app.web.dto.UserRegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserService userService;

    @GetMapping("/")
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage(@RequestParam(value = "error", required = false) String errorParam) {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("loginRequest", new UserLoginRequest());

        if (errorParam != null) {
            modelAndView.addObject("errorMessage", "Incorrect email or password!");
        }

        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("registerRequest", new UserRegisterRequest());
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerNewUser(@Valid UserRegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }

        userService.register(registerRequest.getEmail(), registerRequest.getPassword());

        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/home")
    public ModelAndView getHomePage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getById(authenticationMetadata.getUserId());
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
