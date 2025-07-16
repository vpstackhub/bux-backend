package com.bux.model;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable=false, unique=true)
  private String username;   

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;
  
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<Expense> expenses;
  
  public String getUsername() {
      return username;
  }
  public void setUsername(String username) {
      this.username = username;
  }

  public User() {}

  public User(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
	    this.id = id;
	}

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<Expense> getExpenses() {
    return expenses;
  }

  public void setExpenses(List<Expense> expenses) {
    this.expenses = expenses;
    
  }
  @Override
  public String toString() {
	return "User [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password + ", expenses="
			+ expenses + "]";
  }
}

