package pak.controllers;

import pak.entity.User;
import pak.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/listUsers";
    }

    @GetMapping("/edit/{id}")
    public String getEditView(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "/users/edit";
    }

    @PostMapping("/{id}")
    public String editModel(@ModelAttribute("user") User user) {
        userService.update(user);
        return "redirect:/users/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/users/list";
    }

    @PostMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "/users/new";    }

    @PostMapping
    public String newUserPost(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/users/list";
    }
}
