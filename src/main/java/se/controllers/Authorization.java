package se.controllers;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.domain.User;
import se.repository.UserRepository;
import se.repository.UserRepositoryImpl;
import se.service.FileService;
import se.service.FileServiceImpl;

import javax.validation.Valid;

@Controller
public class Authorization {
    static FileService fileRepository = new FileServiceImpl();
    static UserRepository users = new UserRepositoryImpl();
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
    @PostMapping("/login_success_handler")
    public String loginSuccessHandler(@RequestParam(value = "username", required = false) String username,
                                      @RequestParam(value = "password", required = false) String password,
                                      Model model) {

        if (users.getByLogPass(username, password).getRole().equals("ROLE_ADMIN")) {

                model.addAttribute("files", fileRepository.getAll());
                model.addAttribute("users", users.getAll());
                return "admin/order";
            } else {
                model.addAttribute("user", new User());
            int id = users.getUserByUsername(getCurrentUsername()).getId();
                model.addAttribute("files", fileRepository.getAllById(id));
                return "user/order";
            }
    }

    @GetMapping("/login_failure_handler")
    public String loginFailureHandler() {

        return "login";
    }

    @PostMapping("/login_failure_handler")
    public String postloginFailureHandler(Model model) {
        System.out.println("Login failure handler....");
        model.addAttribute("error", "bad login and password");
        return "login";
    }



    @GetMapping("/logout_success")
    public String logout_success() {
        return "logout_success";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/registration")
    public String registration(Model model) {

        model.addAttribute("user", new User());
        return "registration";
    }


    @PostMapping("/registration")
    public String addUser(@Valid User user, BindingResult bindingResult,
                          Model model) {
        if(users.getUserByUsername(user.getLogin())!=null){
            model.addAttribute("error", "username is already exist");
            return "registration";
        }

        if (bindingResult.hasErrors()){
            System.out.println("+++++++++++++");
            return "registration";
        }

        User userFromDB = users.getByLogPass(user.getLogin(), user.getPassword());
        if (userFromDB == null) {
            userFromDB = new User();
            userFromDB.setLogin(user.getLogin());
            userFromDB.setPassword(user.getPassword());
            userFromDB.setRole("ROLE_USER");
            users.save(userFromDB);
        }

        model.addAttribute("files", fileRepository.getAllById(users.getId(userFromDB)));
        return "user/order";
    }





}
