package com.essence_evoke.controller;

import com.essence_evoke.model.User;
import com.essence_evoke.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin/users")
    public String viewAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @PostMapping("/admin/update-user-role")
    public String updateUserRole(
            @RequestParam("userEmail") String email,
            @RequestParam("role") String role,
            RedirectAttributes redirectAttributes
    ){
        User user = userService.findByEmail(email).orElseThrow();
        user.setRole(role);
        userService.save(user);
        redirectAttributes.addFlashAttribute("success", "Role updated successfully!");
        return "redirect:/admin/users";
    }
}
