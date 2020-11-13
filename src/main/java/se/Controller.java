package se;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping("/greeting")
    public String hello() {
        return "greeting";
    }
    @GetMapping("/home")
    public String home() {
        return "home";
    }
    @GetMapping("/hello")
    public String helloy() {
        return "hello";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
