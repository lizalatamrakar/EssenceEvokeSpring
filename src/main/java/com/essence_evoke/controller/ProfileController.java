package com.essence_evoke.controller;

import com.essence_evoke.model.User;
import com.essence_evoke.model.UserProfile;
import com.essence_evoke.service.UserProfileService;
import com.essence_evoke.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

@Controller
public class ProfileController {

    private final UserService userService;
    private final UserProfileService userProfileService;

    public ProfileController(UserService userService,
                             UserProfileService userProfileService) {
        this.userService = userService;
        this.userProfileService = userProfileService;
    }

    @GetMapping("/profile")
    public String viewProfilePage(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = userProfileService.findByUser(user)
                .orElseGet(UserProfile::new);

        profile.setUser(user);

        // Add cache-busting timestamp
        String profilePicUrl = (profile.getProfilePicture() != null && !profile.getProfilePicture().isEmpty())
                ? profile.getProfilePicture() + "?v=" + System.currentTimeMillis()
                : "/image/profilepic.png";

        model.addAttribute("user", user);
        model.addAttribute("profile", profile);
        model.addAttribute("profilePicUrl", profilePicUrl); // ✅ important

        return "profile";
    }


    @PostMapping("/profile/save")
    public String saveProfile(@ModelAttribute("profile") UserProfile profile,
                              @RequestParam("profilePictureFile") MultipartFile profilePictureFile,
                              Principal principal) {

        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch existing profile if it exists
        UserProfile existingProfile = userProfileService.findByUser(user)
                .orElse(profile); // If not exists, use the new one from form

        // Update fields
        existingProfile.setFirstName(profile.getFirstName());
        existingProfile.setLastName(profile.getLastName());
        existingProfile.setGender(profile.getGender());
        existingProfile.setDateOfBirth(profile.getDateOfBirth());

        // Handle profile picture if uploaded
        if (!profilePictureFile.isEmpty()) {
            try {
                // Folder inside /static/uploads/profile-pictures
                String uploadDir = "uploads/profile-pictures/";

                // Create folder if it doesn't exist
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Delete old profile picture if it exists
                if (existingProfile.getProfilePicture() != null && !existingProfile.getProfilePicture().isEmpty()) {
                    String oldFilePath = uploadDir + existingProfile.getProfilePicture().substring(existingProfile.getProfilePicture().lastIndexOf("/") + 1);
                    File oldFile = new File(oldFilePath);
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }

                // Generate unique filename for new picture
                String fileName = user.getId() + "_" + System.currentTimeMillis() +
                        "_" + StringUtils.cleanPath(profilePictureFile.getOriginalFilename());

                // Save the file
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(profilePictureFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Save the path relative to static folder (for frontend access)
                existingProfile.setProfilePicture("/uploads/profile-pictures/" + fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        existingProfile.setUser(user); // ensure user is set

        // Save updated profile
        userProfileService.save(existingProfile);

        // ✅ Redirect ensures GET /profile loads fresh data
        return "redirect:/profile";
    }


}
