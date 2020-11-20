package se.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import se.domain.File;
import se.service.FileService;
import se.service.FileServiceImpl;
import se.service.UserService;
import se.service.UserServiceImpl;

import javax.validation.Valid;

@org.springframework.stereotype.Controller
public class Controller {
    static FileService fileRepository = new FileServiceImpl();
    static UserService userService = new UserServiceImpl();

    public String getCurrentUsername() {
        //to obtains a user name
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public String distribute(Model model) {
        //distribute to user and admin
        if (userService.getUserByUsername(getCurrentUsername()).getRole().equals("ROLE_USER")) {
            int id = userService.getUserByUsername(getCurrentUsername()).getId();
            model.addAttribute("files", fileRepository.getAllById(id));
            return "user/files";
        } else {
            model.addAttribute("files", fileRepository.getAll());
            model.addAttribute("users", userService.getAll());
            return "admin/files";
        }
    }


    @GetMapping("/files")
    public String getOrderPage(Model model) {

        return distribute(model);
    }


    @GetMapping("/files/new")
    public String addNewOrderPage(Model model) {
        model.addAttribute("doc", new File());
        return "addNewFile";
    }


    @PostMapping("/files")
    public String addNewOrder(@RequestBody @Valid File file, Errors errors,
                              Model model) {
        if (errors.hasErrors()) {
            return "addNewFile";
        }

        int user_id = userService.getUserByUsername(getCurrentUsername()).getId();
        file.setFile_user(user_id);
        fileRepository.save(file);

        return distribute(model);

    }


    @GetMapping("/files/{files_id}/edit")
    public String edit(@PathVariable("files_id") Integer id,
                       Model model) {
        if (userService.getUserByUsername(getCurrentUsername()).getId() == fileRepository.getById(id).getFile_user()) {
            model.addAttribute("doc", fileRepository.getById(id));
            return "update";
        } else
            return "403";
    }


    @PatchMapping("/files/{id}")
    public String update(@RequestBody @Valid File file,
                         Errors errors, @PathVariable("id") int id,
                         Model model) {
        if (userService.getUserByUsername(getCurrentUsername()).getId() != fileRepository.getById(id).getFile_user()) {

            return "403";
        }

        if (errors.hasErrors()) {
            model.addAttribute("doc", fileRepository.getById(id));
            return "update";
        }

        fileRepository.update(id, file.getUser_id(), file.getName(), file.getDate());

        return distribute(model);
    }


    @DeleteMapping("/files/{id}")
    public String delete(@PathVariable("id") int id, Model model) {


        if (userService.getUserByUsername(getCurrentUsername()).getId() != fileRepository.getById(id).getFile_user()) {

            return "403";
        }

                fileRepository.delete(id);

        return distribute(model);

    }

    @GetMapping("/select")
    public String getOrderFilter( @RequestBody File file,
                               Model model) {
        int user_id = userService.getUserByUsername(getCurrentUsername()).getId();
        if (userService.getUserByUsername(getCurrentUsername()).getRole().equals("ROLE_USER")) {
            model.addAttribute("files", fileRepository.select(0, file.getUser_id(), file.getName(), file.getDate()));
            model.addAttribute("users", userService.getAll());
            return "admin/files";
        } else {
            model.addAttribute("files", fileRepository.select(user_id, file.getUser_id(), file.getName(), file.getDate()));
            return "user/files";
        }

    }
    @GetMapping("/sort")
    public String sort(@RequestParam(value = "field") String field, Model model) {

        System.out.println("_---------" + field);
        if (userService.getUserByUsername(getCurrentUsername()).getRole().equals("ROLE_USER")) {
            return "403";
        }



        model.addAttribute("files",fileRepository.sort(0,field));
        model.addAttribute("users", userService.getAll());

        return "admin/files";
    }


}
