package BidHub.dto;

public class PaymentInfo {
	
	private String paymentType;
	private String cardNumber;
	private String nameOnCard;
	private String CV;
	private String cardExpiry;
	private boolean expeditedShipment;

	public PaymentInfo() {
	}

	public PaymentInfo(String paymentType, String cardNumber, String nameOnCard, String CV, String cardExpiry,
			boolean expeditedShipment) {
		
		this.paymentType = paymentType;
		this.cardNumber = cardNumber;
		this.nameOnCard = nameOnCard;
		this.CV = CV;
		this.cardExpiry = cardExpiry;
		this.expeditedShipment = expeditedShipment;

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

	public boolean isExpeditedShipment() {
		return expeditedShipment;
	}
}