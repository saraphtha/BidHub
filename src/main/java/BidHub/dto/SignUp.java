package BidHub.dto;

public class SignUp {

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
	private String confirmPassword;

	public SignUp() {
	}

	public SignUp(String firstName, String lastName, String email, String phone, String address, String unit,
			String postalCode, String city, String country, String username, String password) {
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
	}

	public String getName() {
		return this.firstName + " " + this.lastName;
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

	public String getConfirmPassword() {
		return this.confirmPassword;
	}
}