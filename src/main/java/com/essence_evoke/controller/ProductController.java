package com.essence_evoke.controller;

import com.essence_evoke.model.Product;
import com.essence_evoke.model.User;
import com.essence_evoke.service.ProductService;
import com.essence_evoke.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	@Autowired
	private UserService userService;
	
	// display list of products
	@GetMapping("/admin/products")
	public String viewProductsPage(Model model) {
		model.addAttribute("listProducts", productService.getAllProducts());
		return "products";
	}

	@GetMapping({"/", "/home"})
	public String viewHomePage(Model model) {
		model.addAttribute("productList", productService.getAllProducts());
		return "home";
	}

	@GetMapping("/productdisplay/{id}")
	public String viewProductDisplay(@PathVariable("id") long id, Model model) {
		Product selectedProduct = productService.getProductById(id);
		if (selectedProduct == null) {
			return "redirect:/"; // or a 404 page
		}

		model.addAttribute("product", selectedProduct);

		// fetch all other products except the selected one
		List<Product> topProducts = productService.getTopPickProducts();
		model.addAttribute("topProducts", topProducts);

		return "productdisplay";
	}

	@PostMapping("/wishlist/add/{productId}")
	public String addToWishlist(@PathVariable Long productId) {
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

		// Add product to wishlist
		if (user.getWishlist() == null) {
			user.setWishlist(new HashSet<>());
		}
		user.getWishlist().add(product);
		userService.updateUser(user);

		return "redirect:/productdisplay/" + productId; // redirect back to product page
	}

	@GetMapping("/wishlist")
	public String viewWishList(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = auth.getName();

		User user = userService.findByEmail(userEmail).orElse(null);
		if (user == null) {
			return "redirect:/login";
		}

		model.addAttribute("wishlistProducts", user.getWishlist());
		return "wishlist";
	}

	@PostMapping("/wishlist/remove/{productId}")
	public String removeFromWishlist(@PathVariable Long productId) {
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

		// Remove product from wishlist
		if (user.getWishlist() != null) {
			user.getWishlist().remove(product);
			userService.updateUser(user);
		}

		return "redirect:/wishlist"; // redirect back to wishlist page
	}

	// GET: Show Add Product form
	@GetMapping("/admin/products/add")
	public String showAddProductForm(Model model) {
		model.addAttribute("product", new Product());
		return "add_product"; // this will be the Thymeleaf HTML file for the form
	}

	@PostMapping("/admin/add/products")
	public String saveProduct(@ModelAttribute("product") Product product,
							  @RequestParam("imageFile") MultipartFile imageFile) {
		if (!imageFile.isEmpty()) {
			try {
				// Folder inside /static/uploads/products
				String uploadDir = "uploads/products/";

				// Create folder if not exists
				File directory = new File(uploadDir);
				if (!directory.exists()) {
					directory.mkdirs();
				}

				// Get the original filename
				String fileName = System.currentTimeMillis() +
						"_" + StringUtils.cleanPath(imageFile.getOriginalFilename());

				// Save the file
				Path filePath = Paths.get(uploadDir, fileName);
				Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

				// Store the filename in DB
				product.setImage("/uploads/products/" + fileName);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		productService.saveProduct(product);
		return "redirect:/admin/products";
	}

	// Show Edit Product Form
	@GetMapping("/admin/products/edit/{id}")
	public String showEditProductForm(@PathVariable Long id, Model model) {
		Product product = productService.getProductById(id);
		if (product == null) {
			return "redirect:/admin/products"; // fallback if not found
		}
		model.addAttribute("product", product);
		return "edit_product"; // Thymeleaf template for editing
	}

	// Save Edited Product
	@PostMapping("/admin/products/update/{id}")
	public String updateProduct(@PathVariable Long id,
								@ModelAttribute("product") Product product,
								@RequestParam("imageFile") MultipartFile imageFile) {
		Product existingProduct = productService.getProductById(id);

		if (existingProduct == null) {
			return "redirect:/admin/products"; // not found
		}

		// Update fields
		existingProduct.setName(product.getName());
		existingProduct.setDescription(product.getDescription());
		existingProduct.setPrice(product.getPrice());
		existingProduct.setTopPick(product.isTopPick());
		existingProduct.setNewArrival(product.isNewArrival());
		existingProduct.setStock(product.getStock());

		if (!imageFile.isEmpty()) {
			try {
				String uploadDir = "uploads/products/";

				File directory = new File(uploadDir);
				if (!directory.exists()) {
					directory.mkdirs();
				}


				String fileName = System.currentTimeMillis() +
						"_" + StringUtils.cleanPath(imageFile.getOriginalFilename());
				Path filePath = Paths.get(uploadDir, fileName);
				Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

				existingProduct.setImage("/uploads/products/" + fileName); // overwrite old image
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		productService.saveProduct(existingProduct);
		return "redirect:/admin/products";
	}

	// Delete Product
	@GetMapping("/admin/products/delete/{id}")
	public String deleteProduct(@PathVariable Long id) {
		productService.deleteProductById(id);
		return "redirect:/admin/products";
	}

}
