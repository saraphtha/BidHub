package BidHub.dto;

public class EditUser {

	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String address;
	private String unit;
	private String postalCode;
	private String city;
	private String country;

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
}