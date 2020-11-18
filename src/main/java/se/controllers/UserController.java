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
@RequestMapping("/user")
public class UserController {
    static FileService fileRepository = new FileServiceImpl();
    static UserRepository users = new UserRepositoryImpl();

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @GetMapping("/order")
    public String getOrderPage(Model model) {

        int id = users.getUserByUsername(getCurrentUsername()).getId();
        model.addAttribute("files", fileRepository.getAllById(id));
        return "user/order";
    }

    @GetMapping("/add-new-order")
    public String addNewOrderPage(Model model) {//@PathVariable("id") Integer id,
                model.addAttribute("file", new File());

        return "user/addNewOrder";
    }

    @PostMapping("/add-new-order")
    public String addNewOrder(@Valid File file, Errors errors,
                              Model model) {
        if(errors.hasErrors()){
            return "user/addNewOrder";
        }

        int user_id = users.getUserByUsername(getCurrentUsername()).getId();
        file.setFile_user(user_id);

        fileRepository.save(file);

        model.addAttribute("files", fileRepository.getAllById(user_id));

        return "user/order";
    }

    @GetMapping("/select")
    public String getOrderFilter(@RequestParam(value = "file_id", required = false, defaultValue = "0")
                                             Integer file_id,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "date", required = false) String date, Model model) {

       
        int user_id = users.getUserByUsername(getCurrentUsername()).getId();
        model.addAttribute("files", fileRepository.select(user_id, file_id, name, date));
        return "user/order";
    }


    @GetMapping("/file/{files_id}/edit")
    public String edit(@PathVariable("files_id") Integer id,
                                 Model model) {



        model.addAttribute("doc", fileRepository.getById(id));
        return "user/update";
    }

    @PatchMapping("/file/{id}")
    public String update(@ModelAttribute("doc") @Valid File file,
                         Errors errors, @PathVariable("id") int id,
                             Model model) {

        if(errors.hasErrors()){
            model.addAttribute("file", fileRepository.getById(id));
            return "user/update";
        }

        fileRepository.update(id, file.getUser_id(), file.getName(), file.getDate());
        model.addAttribute("files", fileRepository.getAllById(fileRepository.getById(id).getFile_user()));
        return "redirect:/user/order";
    }


    @DeleteMapping("/file/{id}")
    public String delete(@PathVariable("id") int id, Model model) {

        int oldId = fileRepository.getById(id).getFile_user();
        fileRepository.delete(id);
        model.addAttribute("files", fileRepository.getAllById(oldId));

        return "redirect:/user/order";
    }













}
