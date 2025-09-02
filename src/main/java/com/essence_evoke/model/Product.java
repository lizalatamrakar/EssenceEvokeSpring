package com.essence_evoke.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")

public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column (name = "name")
	private String name;
	
	@Column (name = "description")
	private String description;
	
	@Column (name = "price")
	private int price;
	
	@Column (name = "image")
	private String image;
	
	@Column (name = "is_top_pick")
	private boolean isTopPick;
	
	@Column (name = "is_new_arrival")
	private boolean isNewArrival;

	@Column(name = "stock", nullable = false, columnDefinition = "int default 0")
	private int stock;

	// Bidirectional mapping
	@ManyToMany(mappedBy = "wishlist")
	private Set<User> users = new HashSet<>();

	@ManyToMany(mappedBy = "cart")
	private Set<User> userCarts = new HashSet<>();


	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public boolean isTopPick() {
		return isTopPick;
	}
	public void setTopPick(boolean isTopPick) {
		this.isTopPick = isTopPick;
	}
	public boolean isNewArrival() {
		return isNewArrival;
	}
	public void setNewArrival(boolean isNewArrival) {
		this.isNewArrival = isNewArrival;
	}

	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	
}
