package BidHub.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * This is the entity class for the users.
 * 
 * Users give their first and last name, email,, phone, address, unit
 * (optional), postal code, city, country, username, and password.
 * 
 * The is set to either ROLE_ADMIN or ROLE_USER. The database has one admin
 * created, and the UserController class creates users with ROLE_USER.
 */

@Entity
public class User {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String address;
	private String unit;
	private String postalCode;
	private String city;
	private String country;
	private String username;
	private String password;
	private Role role;

	public User() {
	}

	public User(String firstName, String lastName, String email, String phone, String address, String unit,
			String postalCode, String city, String country, String username, String password, Role role) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.unit = unit;
		this.postalCode = postalCode;
		this.city = city;
		this.country = country;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.firstName + " " + this.lastName;
	}

	public void setName(String name) {
		String[] parts = name.split(" ");
		this.firstName = parts[0];
		this.lastName = parts[1];
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getEmail() {
		return this.email;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return this.address;
	}

	public String getUnit() {
		return this.unit;
	}

	public String getPostalCode() {
		return this.postalCode;
	}

	public String getCity() {
		return this.city;
	}

	public String getCountry() {
		return this.country;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public Role getRole() {
		return this.role;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (!(o instanceof User))
			return false;
		User user = (User) o;
		return Objects.equals(this.id, user.id) && Objects.equals(this.firstName, user.firstName)
				&& Objects.equals(this.lastName, user.lastName) && Objects.equals(this.email, user.email)
				&& Objects.equals(this.phone, user.phone) && Objects.equals(this.address, user.address)
				&& Objects.equals(this.unit, user.unit) && Objects.equals(this.postalCode, user.postalCode)
				&& Objects.equals(this.city, user.city) && Objects.equals(this.country, user.country)
				&& Objects.equals(this.username, user.username) && Objects.equals(this.password, user.password)
				&& Objects.equals(this.role, user.role);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.firstName, this.lastName, this.email, this.phone, this.address, this.unit,
				this.postalCode, this.city, this.country, this.username, this.password, this.role);
	}

	@Override
	public String toString() {
		return "User{" + "id=" + this.id + ", firstName=" + this.firstName + ", lastName=" + this.lastName + ", email="
				+ this.email + ", phone=" + this.phone + ", address=" + this.address + ", unit=" + this.unit
				+ ", postalCode=" + this.postalCode + ", city=" + this.city + ", country=" + this.country
				+ ", username=" + this.username + ", password=" + this.password + ", role=" + this.role + '}';
	}
}