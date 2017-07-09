package com.example.security.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
/**
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_user")
public class User extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8227034948239645662L;

	//TODO Lock user after 3 times wrong password attempt.  Use enable / disable?
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	
	@Column(length=30, unique=true)
	private String username;
	
	
	@Column(length=30, unique=true)
	private String email;
	
		
	@Column(length=255)
	private String password;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "t_user_role",
	    joinColumns = {@JoinColumn(name = "user", referencedColumnName = "id")},
	    inverseJoinColumns = {@JoinColumn(name = "role", referencedColumnName ="code")})
	private Set<Role> roles;
	
	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST, mappedBy="users")
	private Set<Group> groups;
	
	@Column(length=255)
	private String productLines;
	
	@Column(length=100)
	private String businessMarkets;
	
	@Column(length=100, unique=true)
	private String employeeNumber;
	
	private boolean enable;
	@NotNull
	@Column(length = 50)
	private String firstName;
	
	@NotNull
	@Column(length = 50)
	private String lastName;
	
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	
	public String getProductLines() {
		return productLines;
	}
	public void setProductLines(String productLines) {
		this.productLines = productLines;
	}
	
	public String getBusinessMarkets() {
		return businessMarkets;
	}
	public void setBusinessMarkets(String businessMarkets) {
		this.businessMarkets = businessMarkets;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Set<Group> getGroups() {
		return groups;
	}
	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}
	
	
}
