package se.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import se.domain.File;
import se.repository.UserRepository;
import se.repository.UserRepositoryImpl;
import se.service.FileService;
import se.service.FileServiceImpl;

import javax.validation.Valid;


@Controller
public class UserController {
    static FileService fileRepository = new FileServiceImpl();
    static UserRepository users = new UserRepositoryImpl();

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @GetMapping("/user/order")
    public String getOrderPage(Model model) {
        System.out.println("order-user " +users.getUserByUsername(getCurrentUsername()));
        int id = users.getUserByUsername(getCurrentUsername()).getId();
        model.addAttribute("files", fileRepository.getAllById(id));
        return "user/order";
    }

    @GetMapping("/user/add-new-order")
    public String addNewOrderPage(Model model) {//@PathVariable("id") Integer id,
                model.addAttribute("file", new File());

        System.out.println("addGET-user " +users.getUserByUsername(getCurrentUsername()));
        return "user/addNewOrder";
    }

    @PostMapping("/user/add-new-order")
    public String addNewOrder(@Valid File file, Errors errors,
                              Model model) {
        System.out.println("addpost-user " +users.getUserByUsername(getCurrentUsername()));
        if(errors.hasErrors()){
            return "user/addNewOrder";
        }

        int user_id = users.getUserByUsername(getCurrentUsername()).getId();
        file.setFile_user(user_id);

        fileRepository.save(file);

        model.addAttribute("files", fileRepository.getAllById(user_id));

        return "user/order";
    }


    @GetMapping("/user/update/{files_id}")
    public String updateItemPage(@PathVariable("files_id") Integer id, //@RequestParam("user_id") int user_id,
                                 Model model) {
        System.out.println("++++++++++++1");
        // id - file
        model.addAttribute("file", new File());
        model.addAttribute("file_id", id);
        return "user/update";
    }


    @PostMapping("/user/update/{files_id}")
    public String updateItem(@RequestParam("file_id") Integer id,
                             @Valid File file,
                             Errors errors,
                             Model model) {
        System.out.println("++++++++++++2");

        if(errors.hasErrors()){
            model.addAttribute("files_id", id);
            return "user/update";
        }
        // id - file
        fileRepository.update(id, file.getUser_id(), file.getName(), file.getDate());
        model.addAttribute("files", fileRepository.getAllById(fileRepository.getById(id).getFile_user()));
        return "user/order";
    }


    @GetMapping("/user/delete/{id}")
    public String deleteItem(
            @PathVariable Integer id,
            Model model) {
        //id - file
        int oldId = fileRepository.getById(id).getFile_user();

        fileRepository.delete(id);

        model.addAttribute("files", fileRepository.getAllById(oldId));

        return "user/order";
    }


    @GetMapping("/user/select")
    public String getOrderFilter(@RequestParam(value = "file_id", required = false, defaultValue = "0")
                                             Integer file_id,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "date", required = false) String date, Model model) {

       
        int user_id = users.getUserByUsername(getCurrentUsername()).getId();
        System.out.println(file_id + "=filed_id  " +user_id + "user_id"  );
        model.addAttribute("files", fileRepository.select(user_id, file_id, name, date));
        return "user/order";
    }



}
