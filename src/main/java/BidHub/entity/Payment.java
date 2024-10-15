package BidHub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * This is the entity class for the payments.
 * 
 * This will help create the final receipt the user sees for their purchase.
 */

@Entity
public class Payment {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	private String paymentType;
	private String cardNumber;
	private String nameOnCard;
	private String CV;
	private String cardExpiry;

	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String address;
	private String unit;
	private String postalCode;
	private String city;
	private String country;

	private int bidItemPrice;
	private double shipmentCost;
	private int shipmentDays;
	private boolean expeditedShipment;
	private double expeditedShipmentCost;

	public Payment() {
	}

	public Payment(String paymentType, String cardNumber, String nameOnCard, String CV, String cardExpiry,
			boolean expeditedShipment) {

		this.paymentType = paymentType;
		this.cardNumber = cardNumber;
		this.nameOnCard = nameOnCard;
		this.CV = CV;
		this.cardExpiry = cardExpiry;
		this.expeditedShipment = expeditedShipment;

	}

	public Long getId() {
		return this.id;
	}

	public String getPaymentType() {
		return this.paymentType;
	}

	public String getCardNumber() {
		return this.cardNumber;
	}

	public String getNameOnCard() {
		return this.nameOnCard;
	}

	public String getCV() {
		return this.CV;
	}

	public String getCardExpiry() {
		return cardExpiry;
	}

	public String getName() {
		return this.firstName + " " + this.lastName;
	}

	public void setName(String name) {
		String[] parts = name.split(" ");
		this.firstName = parts[0];
		this.lastName = parts[1];
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

	public int getBidItemPrice() {
		return this.bidItemPrice;
	}

	public double getShipmentCost() {
		return shipmentCost;
	}

	public double getTotalCost() {
		return this.bidItemPrice + this.shipmentCost + this.expeditedShipmentCost;
	}

	public double getTotalShipmentCost() {
		return this.shipmentCost + this.expeditedShipmentCost;
	}

	public void setTotalCost(int bidItemPrice, double shipmentCost, double expeditedShipmentCost) {
		this.bidItemPrice = bidItemPrice;
		this.shipmentCost = shipmentCost;
		this.expeditedShipmentCost = expeditedShipmentCost;
	}

	public int getShipmentDays() {
		return shipmentDays;
	}

	public boolean isExpeditedShipment() {
		return expeditedShipment;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public void setNameOnCard(String name) {
		this.nameOnCard = name;
	}

	public void setCV(String cv) {
		this.CV = cv;
	}

	public void setCardExpiry(String cardExpiry) {
		this.cardExpiry = cardExpiry;
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

	public void setShipmentDays(int shipmentDays) {
		this.shipmentDays = shipmentDays;
	}

	public void setExpeditedShipment(boolean expeditedShipment) {
		this.expeditedShipment = expeditedShipment;
	}
}
