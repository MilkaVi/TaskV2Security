package se;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import se.repository.UserRepository;
import se.repository.UserRepositoryImpl;

@org.springframework.stereotype.Controller
public class Controller {

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
    UserRepository users = new UserRepositoryImpl();

    @GetMapping("/greeting")
    public String hello() {
        System.out.println("gret " +users.getUserByUsername(getCurrentUsername()));
        return "greeting";
    }
    @GetMapping("/home")
    public String home() {
        return "home";
    }
    @GetMapping("/hello")
    public String helloy() {
        System.out.println("hi " +users.getUserByUsername(getCurrentUsername()));
        return "hello";
    }
//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }


    @GetMapping("/admin/greeting")
    public String helloa() {
        System.out.println("gret " +users.getUserByUsername(getCurrentUsername()));
        return "hello";
    }
    @GetMapping("/admin/home")
    public String homea() {
        System.out.println("gret " +users.getUserByUsername(getCurrentUsername()));
        return "hello";
    }







}
