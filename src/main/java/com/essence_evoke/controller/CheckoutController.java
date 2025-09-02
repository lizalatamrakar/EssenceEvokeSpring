package com.essence_evoke.controller;

import com.essence_evoke.model.*;
import com.essence_evoke.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class CheckoutController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private CheckoutItemService checkoutItemService;
    @Autowired
    private PurchaseHistoryService purchaseHistoryService;

    @Autowired
    private PurchaseHistoryLineItemService purchaseHistoryLineItemService;

    @PostMapping("/cart/add/{productId}")
    public String addToCart(
            @PathVariable Long productId,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request
    ) {
        // get the logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        Product product = productService.getProductById(productId);
        if (product == null) {
            return "redirect:/"; // product not found
        }

        // Add product to user's cart
        if (user.getCart() == null) {
            user.setCart(new HashSet<>());
        }
        user.getCart().add(product);
        userService.updateUser(user);

        // flash message
        redirectAttributes.addFlashAttribute("cartMessage", product.getName() + " added to cart!");

        // redirect back to where the request came from
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @GetMapping("/cart")
    public String checkout(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();

        User user = userService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("cartProducts", user.getCart());
        return "cart";
    }

    @PostMapping("/cart/remove/{productId}")
    public String removeFromCart(
            @PathVariable Long productId,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request
    ) {
        // get logged-in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName(); // assuming email is username

        User user = userService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        Product product = productService.getProductById(productId);
        if (product == null) {
            return "redirect:/"; // product not found
        }

        // Remove product from cart
        if (user.getCart() != null) {
            user.getCart().remove(product);
            userService.updateUser(user);
        }

        // flash message
        redirectAttributes.addFlashAttribute("cartMessage", product.getName() + " removed from cart!");

        // redirect back to where the request came from
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @GetMapping("/checkout")
    public String viewCheckoutPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findByEmail(email).orElse(null);

        if (user != null) {
            List<CheckoutItem> activeItems = checkoutItemService.findByUserAndStatus(user, "ACTIVE");
            model.addAttribute("checkoutItems", activeItems);

            double grandTotal = activeItems.stream()
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum();
            model.addAttribute("grandTotal", grandTotal);
        }

        return "checkout";
    }



    @PostMapping("/checkout/create")
    public String createCheckout(
            @RequestParam("productIds") List<Long> productIds,
            @RequestParam Map<String, String> allParams,
            RedirectAttributes redirectAttributes
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();

        User user = userService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        checkoutItemService.changeOldItemsStatus(user, "INACTIVE");

        List<CheckoutItem> newItems = new ArrayList<>();
        for (Long productId : productIds) {
            Product product = productService.getProductById(productId);
            if (product == null) continue;

            String qtyStr = allParams.get("quantities_" + productId);
            int qty = (qtyStr != null) ? Integer.parseInt(qtyStr) : 1;

            CheckoutItem item = new CheckoutItem();
            item.setUser(user);
            item.setProduct(product);
            item.setQuantity(qty);
            item.setPrice(product.getPrice());
            item.setStatus("ACTIVE");
            newItems.add(item);
        }

        checkoutItemService.saveAll(newItems);

        redirectAttributes.addFlashAttribute("cartMessage", "Checkout items created!");
        return "redirect:/checkout";
    }

    @PostMapping("/checkout/remove/{checkoutItemId}")
    public String removeCheckoutItem(
            @PathVariable Long checkoutItemId,
            RedirectAttributes redirectAttributes
    ) {
        Optional<CheckoutItem> itemOpt = checkoutItemService.findById(checkoutItemId);
        if (itemOpt.isPresent()) {
            checkoutItemService.delete(itemOpt.get());
            redirectAttributes.addFlashAttribute("cartMessage", "Item removed from checkout!");
        } else {
            redirectAttributes.addFlashAttribute("cartMessage", "Item not found!");
        }

        return "redirect:/checkout";
    }

    @GetMapping("/khalti")
    public String khalti() {
        return "khalti"; // this maps to login.html in templates/
    }

    @GetMapping("/esewa")
    public String esewa() {
        return "esewa"; // this maps to login.html in templates/
    }

    @PostMapping("/checkout/confirm")
    public String confirmPaymentMethod(
            @RequestParam("paymentMethod") String paymentMethod,
            RedirectAttributes redirectAttributes
    ) {
        if ("Esewa".equalsIgnoreCase(paymentMethod)) {
            return "redirect:/esewa";
        } else if ("Khalti".equalsIgnoreCase(paymentMethod)) {
            return "redirect:/khalti";
        }
        redirectAttributes.addFlashAttribute("cartMessage", "Invalid payment method selected!");
        return "redirect:/checkout";
    }

    @PostMapping("/payment/process")
    public String processPayment(
            @RequestParam("id") String paymentId,
            @RequestParam("pin") String pin,
            @RequestParam("paymentMethod") String paymentMethod,
            RedirectAttributes redirectAttributes
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        User user = userService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        // Fetch active checkout items
        List<CheckoutItem> items = checkoutItemService.findByUserAndStatus(user, "ACTIVE");
        if (items.isEmpty()) {
            redirectAttributes.addFlashAttribute("cartMessage", "No items to checkout!");
            return "redirect:/checkout";
        }

        // Calculate total
        float total = (float) items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        // Create PurchaseHistory
        PurchaseHistory history = new PurchaseHistory();
        history.setUser(user);
        history.setTotalAmount(total);
        history.setPaymentMethod(paymentMethod);
        history.setTransactionId(UUID.randomUUID().toString());
        history.setStatus("COMPLETED");

        // Save history first
        PurchaseHistory savedHistory = purchaseHistoryService.save(history);

        // Save line items
        for (CheckoutItem item : items) {
            PurchaseHistoryLineItem lineItem = new PurchaseHistoryLineItem();
            lineItem.setPurchaseHistory(savedHistory);
            lineItem.setProduct(item.getProduct());
            lineItem.setQuantity(item.getQuantity());
            lineItem.setPriceAtPurchase(item.getPrice());

            purchaseHistoryLineItemService.save(lineItem);
        }

        // Mark checkout items inactive
        checkoutItemService.changeOldItemsStatus(user, "COMPLETED");

        redirectAttributes.addFlashAttribute("cartMessage", "Payment successful via " + paymentMethod + "!");
        return "redirect:/success"; // later you can build success.html
    }

    @GetMapping("/success")
    public String successPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();

        User user = userService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        // Fetch latest purchase for this user
        List<PurchaseHistory> purchases = purchaseHistoryService.getByUser(user);
        if (!purchases.isEmpty()) {
            PurchaseHistory latest = purchases.get(purchases.size() - 1); // last one
            model.addAttribute("purchaseHistory", latest);
        }

        return "success";
    }
}
