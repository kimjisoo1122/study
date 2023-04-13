package com.example.springjsp.controller;

import com.example.springjsp.domain.User;
import com.example.springjsp.validator.UserValidator;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @InitBinder
    public void validUser(WebDataBinder webDataBinder) {
//        webDataBinder.addValidators(new UserValidator());
//        List<Validator> validators = webDataBinder.getValidators();
    }

    @InitBinder
    public void toDate(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(Date.class, "birth",  new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
        webDataBinder.registerCustomEditor(String[].class, new StringArrayPropertyEditor("#"));
    }

    @RequestMapping(value = "/add", method = {RequestMethod.GET})
    public String form(User user) {
        return "registerForm";
    }

    @PostMapping("/add")
    public String register(
            @Valid User user,
            BindingResult bindingResult) {

//        new UserValidator().validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println("bindingResult = " + bindingResult);
            String defaultMessage = bindingResult.getFieldError().getDefaultMessage();
            System.out.println("defaultMessage = " + defaultMessage);
            return "registerForm";
        }

        System.out.println("bindingResult = " + bindingResult);
        return "registerInfo";
    }
}
