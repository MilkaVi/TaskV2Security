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
public class AdminController {

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    static FileService fileRepository = new FileServiceImpl();
    static UserRepository users = new UserRepositoryImpl();

    @GetMapping("/admin/order")
    public String getOrderPage(Model model) {

        model.addAttribute("files", fileRepository.getAll());
        model.addAttribute("users", users.getAll());
        return "admin/order";
    }

    @GetMapping("/admin/update/{files_id}")
    public String updateItemPage(@PathVariable("files_id") Integer id, //@RequestParam("user_id") int user_id,
                                 Model model) {
        // id - file
        model.addAttribute("file", new File());
        model.addAttribute("files_id", id);
        return "admin/update";
    }

    @PostMapping("/admin/update/{files_id}")
    public String updateItem(@RequestParam("file_id") Integer id,
                             @Valid File file,
                             Errors errors,
                             Model model) {

        if(errors.hasErrors()){
            model.addAttribute("files_id", id);
            return "admin/update";
        }

        fileRepository.update(id, file.getUser_id(), file.getName(), file.getDate());
        model.addAttribute("files", fileRepository.getAll());
        model.addAttribute("users", users.getAll());
        return "admin/order";
    }

    @DeleteMapping("/admin/delete/{id}")
    public String deleteItem(@PathVariable Integer id, Model model) {
        //id - file

        fileRepository.delete(id);
        model.addAttribute("files", fileRepository.getAll());
        model.addAttribute("users", users.getAll());

        return "admin/order";
    }

    @GetMapping("/admin/select")
    public String getOrderFilter(@RequestParam(value = "file_id", required = false, defaultValue = "0") Integer file_id,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "date", required = false) String date, Model model) {



        model.addAttribute("files", fileRepository.select(0, file_id, name, date));


        model.addAttribute("users", users.getAll());

        return "admin/order";
    }

    @GetMapping("/admin/sort")
    public String sort(@RequestParam(value = "field") String field, Model model) {

        model.addAttribute("files",fileRepository.sort(0,field));
        model.addAttribute("users", users.getAll());

        return "admin/order";
    }




    @GetMapping("/admin/file/{files_id}/edit")
    public String editA(@PathVariable("files_id") Integer id,
                       Model model) {
        model.addAttribute("doc", fileRepository.getById(id));
        return "admin/update";
    }

    @PatchMapping("/admin/file/{id}")
    public String updateA(@ModelAttribute("doc") @Valid File file,
                         Errors errors, @PathVariable("id") int id,
                         Model model) {

        if(errors.hasErrors()){
            model.addAttribute("file", fileRepository.getById(id));
            return "admin/update";
        }

        fileRepository.update(id, file.getUser_id(), file.getName(), file.getDate());

        model.addAttribute("files", fileRepository.getAll());

        return "redirect:/admin/order";
    }


    @DeleteMapping("/admin/file/{id}")
    public String deleteA(@PathVariable("id") int id, Model model) {

        fileRepository.delete(id);
        model.addAttribute("files", fileRepository.getAll());

        return "redirect:/admin/order";
    }








}
