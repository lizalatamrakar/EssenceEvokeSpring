package com.essence_evoke.controller;

import com.essence_evoke.model.User;
import com.essence_evoke.model.VerificationToken;
import com.essence_evoke.repository.VerificationTokenRepository;
import com.essence_evoke.service.EmailService;
import com.essence_evoke.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.UUID;

@Controller
public class AuthController {

    private final UserServiceImpl userService;
    private final EmailService emailService;
    private final VerificationTokenRepository tokenRepository;

    @Autowired
    public AuthController(
            UserServiceImpl userService,
            EmailService emailService,
            VerificationTokenRepository tokenRepository
    ) {
        this.userService = userService;
        this.emailService = emailService;
        this.tokenRepository = tokenRepository;
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup"; // signup.html (Thymeleaf)
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model, HttpServletRequest request) {
        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email already exists!");
            return "signup";
        }

        // save user disabled
        user.setEnabled(false);
        User savedUser = userService.registerNewUser(user);

        // generate token
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(savedUser, token);

        // build confirmation URL
        String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String confirmationUrl = appUrl + "/confirm?token=" + token;

        // send email
        emailService.sendEmail(
                savedUser.getEmail(),
                "Email Confirmation",
                "Thank you for registering. Please click the link to activate your account: " + confirmationUrl
        );

        model.addAttribute("message", "registered");
        return "login";
    }

    @GetMapping("/confirm")
    public String confirmAccount(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        VerificationToken verificationToken = tokenRepository.findByToken(token).orElse(null);

        if (verificationToken == null) {
            redirectAttributes.addFlashAttribute("error", "invalidToken");
            return "redirect:/login";
        }

        if (verificationToken.isExpired()) {
            redirectAttributes.addFlashAttribute("error", "expiredToken");
            redirectAttributes.addFlashAttribute("email", verificationToken.getUser().getEmail());
            return "redirect:/login";
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userService.updateUser(user);

        tokenRepository.delete(verificationToken);

        redirectAttributes.addFlashAttribute("message", "confirmed");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login"; // this maps to login.html in templates/
    }

    @GetMapping("/resend-confirmation")
    public String resendConfirmationEmail(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        Optional<User> optionalUser = userService.findByEmail(email);
        if (optionalUser.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "No account found with this email.");
            return "redirect:/login";
        }

        User user = optionalUser.get();
        if (user.isEnabled()) {
            redirectAttributes.addFlashAttribute("message", "Account is already confirmed.");
            return "redirect:/login";
        }

        // generate new token
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        // send email
        String appUrl = "http://localhost:8080"; // or build dynamically
        String confirmationUrl = appUrl + "/confirm?token=" + token;
        emailService.sendEmail(
                user.getEmail(),
                "Email Confirmation",
                "Your previous link expired. Please click this link to activate your account: " + confirmationUrl
        );

        redirectAttributes.addFlashAttribute("message", "resent"); // ✅ updated to match your login.html
        return "redirect:/login";
    }

    @GetMapping("/session-invalid")
    public String sessionInvalid(Model model) {
        model.addAttribute("sessionExpired", true);
        return "login"; // Thymeleaf template
    }


}
