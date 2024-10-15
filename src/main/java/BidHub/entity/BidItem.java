package BidHub.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * This is the entity class for the bid (auction) items.
 * 
 * Users gives all the private values seen below, except Status. Status is set
 * through the controllers when creating, or buying the item.
 */

@Entity
public class BidItem {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	private Long sellerUserId;
	private Long buyerUserId;
	private Long currentBidder;

	private String itemName;
	private String description;
	private int bidPrice;
	private String endTime;

	private boolean dutch;
	private boolean forward;

	private int decrementAmount;
	private int lowestBidPrice;

	private double shipmentCost;
	private int shipmentDays;
	private double expeditedShipmentCost;

	private Status status;

	public BidItem() {
	}

	public BidItem(Long sellerUserId, String itemName, String description, boolean dutch, boolean forward,
			String endTime, int bidPrice, int decrementAmount, int lowestBidPrice, double shipmentCost,
			double expeditedShipmentCost, int shipmentDays, Status status) {
		this.sellerUserId = sellerUserId;
		this.itemName = itemName;
		this.description = description;
		this.dutch = dutch;
		this.forward = forward;
		this.endTime = endTime;
		this.bidPrice = bidPrice;
		this.status = status;
		this.decrementAmount = decrementAmount;
		this.lowestBidPrice = lowestBidPrice;
		this.shipmentCost = shipmentCost;
		this.shipmentDays = shipmentDays;
		this.expeditedShipmentCost = expeditedShipmentCost;
	}

	public Long getId() {
		return this.id;
	}

	public Long getSellerUserId() {
		return this.sellerUserId;
	}

	public Long getBuyerUserId() {
		return this.buyerUserId;
	}

	public String getItemName() {
		return this.itemName;
	}

	public String getDescription() {
		return this.description;
	}

	public int getBidPrice() {
		return this.bidPrice;
	}

	public Status getStatus() {
		return this.status;
	}

	public boolean isDutch() {
		return this.dutch;
	}

	public boolean isForward() {
		return this.forward;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public int getDecrementAmount() {
		return this.decrementAmount;
	}

	public int getLowestBidPrice() {
		return this.lowestBidPrice;
	}

	public double getShipmentCost() {
		return this.shipmentCost;
	}

	public double getExpeditedShipmentCost() {
		return this.expeditedShipmentCost;
	}

	public int getShipmentDays() {
		return this.shipmentDays;
	}

	public Long getCurrentBidder() {
		return this.currentBidder;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setSellerUserId(Long sellerUserId) {
		this.sellerUserId = sellerUserId;
	}

	public void setBuyerUserId(Long buyerUserId) {
		this.buyerUserId = buyerUserId;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setBidPrice(int bidPrice) {
		this.bidPrice = bidPrice;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setDutch(boolean dutch) {
		this.dutch = dutch;
	}

	public void setForward(boolean forward) {
		this.forward = forward;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setDecrementAmount(int decrementAmount) {
		this.decrementAmount = decrementAmount;
	}

	public void setLowestBidPrice(int lowestBidPrice) {
		this.lowestBidPrice = lowestBidPrice;
	}

	public void setShipmentCost(double shipmentCost) {
		this.shipmentCost = shipmentCost;
	}

	public void setExpeditedShipmentCost(double expeditedShipmentCost) {
		this.expeditedShipmentCost = expeditedShipmentCost;
	}

	public void setShipmentDays(int shipmentDays) {
		this.shipmentDays = shipmentDays;
	}

	public void setCurrentBidder(Long currentBidder) {
		this.currentBidder = currentBidder;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (!(o instanceof BidItem))
			return false;
		BidItem bidItem = (BidItem) o;
		return Objects.equals(this.id, bidItem.id) && Objects.equals(this.sellerUserId, bidItem.sellerUserId)
				&& Objects.equals(this.buyerUserId, bidItem.buyerUserId)
				&& Objects.equals(this.currentBidder, bidItem.currentBidder)
				&& Objects.equals(this.itemName, bidItem.itemName)
				&& Objects.equals(this.description, bidItem.description) && Objects.equals(this.dutch, bidItem.dutch)
				&& Objects.equals(this.forward, bidItem.forward) && Objects.equals(this.endTime, bidItem.endTime)
				&& Objects.equals(this.bidPrice, bidItem.bidPrice)
				&& Objects.equals(this.lowestBidPrice, bidItem.lowestBidPrice)
				&& Objects.equals(this.decrementAmount, bidItem.decrementAmount)
				&& Objects.equals(this.shipmentCost, bidItem.shipmentCost)
				&& Objects.equals(this.expeditedShipmentCost, bidItem.expeditedShipmentCost)
				&& Objects.equals(this.shipmentDays, bidItem.shipmentDays) && this.status == bidItem.status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.sellerUserId, this.buyerUserId, this.currentBidder, this.itemName,
				this.description, this.dutch, this.forward, this.endTime, this.bidPrice, this.decrementAmount,
				this.lowestBidPrice, this.shipmentCost, this.expeditedShipmentCost, this.shipmentDays, this.status);
	}

	@Override
	public String toString() {
		return "Sell{" + "id=" + this.id + ", sellerUserId=" + this.sellerUserId + ", buyerUserId=" + this.buyerUserId
				+ ", currentBidder=" + this.currentBidder + ", itemName=" + this.itemName + ", description='"
				+ this.description + '\'' + ", dutch=" + this.dutch + ", forward=" + this.forward + ", endTime="
				+ this.endTime + ", bidPrice=" + this.bidPrice + ", decrementAmount=" + this.decrementAmount
				+ ", lowestBidPrice=" + this.lowestBidPrice + ", shipmentCost=" + this.shipmentCost + ", shipmentDays="
				+ this.shipmentDays + ", expeditedShipmentCost=" + this.expeditedShipmentCost + ", status="
				+ this.status + '}';
	}
}