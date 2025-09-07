package com.essence_evoke.controller;

import com.essence_evoke.dto.PurchaseHistoryDTO;
import com.essence_evoke.model.PurchaseHistory;
import com.essence_evoke.model.User;
import com.essence_evoke.service.PurchaseHistoryService;
import com.essence_evoke.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PurchaseHistoryController {
    @Autowired
    private PurchaseHistoryService purchaseHistoryService;
    @Autowired
    private UserService userService;

    // 1. Admin: show all purchase histories
    @GetMapping("/purchase-history")
    public String getAllHistories(Model model) {
        List<PurchaseHistoryDTO> histories = purchaseHistoryService.getAllHistories();
        model.addAttribute("histories", histories);
        return "purchase_history_list"; // points to src/main/resources/templates/purchase-history/list.html
    }

    // 2. Logged-in user: show my purchase histories
    @GetMapping("/purchase-history/me")
    public String getMyHistories(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // assuming Spring Security stores email
        User user = userService.findByEmail(email).orElseThrow();
        List<PurchaseHistoryDTO> histories = purchaseHistoryService.getHistoriesByUser(user);

        model.addAttribute("histories", histories);
        return "my_purchase_history"; // points to src/main/resources/templates/purchase-history/my-history.html
    }

    @PostMapping("/update-purchase-history-status")
    public String updateOrderStatus(
            @RequestParam("purchaseHistoryId") Long purchaseHistoryId,
            @RequestParam("status") String status,
            RedirectAttributes redirectAttributes) {

        PurchaseHistory purchaseHistory = purchaseHistoryService.getPurchaseHistoryById(purchaseHistoryId);

        purchaseHistory.setStatus(status);
        purchaseHistoryService.save(purchaseHistory);
        redirectAttributes.addFlashAttribute("success", "Order status updated successfully!");
        return "redirect:/purchase-history";
    }
}
