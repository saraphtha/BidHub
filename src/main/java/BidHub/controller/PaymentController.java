package BidHub.controller;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import BidHub.assembler.PaymentModelAssembler;
import BidHub.dto.PaymentInfo;
import BidHub.entity.BidItem;
import BidHub.entity.Payment;
import BidHub.entity.User;
import BidHub.exception.BadRequestException;
import BidHub.exception.NotFoundException;
import BidHub.repository.BidItemRepository;
import BidHub.repository.PaymentRepository;
import BidHub.repository.UserRepository;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class PaymentController {

	private final PaymentRepository paymentRepository;
	private final PaymentModelAssembler paymentAssembler;

	private final UserRepository userRepository;
	private final BidItemRepository bidItemRepository;

	PaymentController(PaymentRepository paymentRepository, PaymentModelAssembler paymentAssembler,
			UserRepository userRepository, BidItemRepository bidItemRepository) {
		this.paymentRepository = paymentRepository;
		this.paymentAssembler = paymentAssembler;
		this.userRepository = userRepository;
		this.bidItemRepository = bidItemRepository;
	}
	
	@GetMapping("/receipts")
	public CollectionModel<EntityModel<Payment>> all() {

		List<EntityModel<Payment>> payment = paymentRepository.findAll().stream() //
				.map(paymentAssembler::toModel) //
				.collect(Collectors.toList());

		return CollectionModel.of(payment, //
				linkTo(methodOn(PaymentController.class).all()).withSelfRel());
	}

	@GetMapping("/receipts/{payId}")
	public EntityModel<Payment> one(@PathVariable Long payId) {

		Payment payment = paymentRepository.findById(payId) //
				.orElseThrow(() -> new NotFoundException("Item not found."));

		return paymentAssembler.toModel(payment);
	}

	@GetMapping("/account/{userId}/auctionItems/{bidItemId}/receipt/{payId}")
	public EntityModel<Payment> oneReceipt(@PathVariable Long payId) {

		Payment pay = paymentRepository.findById(payId).get();

		return paymentAssembler.toModel(pay);
	}

	@PostMapping("/account/{userId}/auctionItems/{bidItemId}/payNow")
	public ResponseEntity<?> payment(@RequestBody PaymentInfo payInfo, @PathVariable Long userId,
			@PathVariable Long bidItemId) {

		User user = userRepository.findById(userId).get();
		BidItem bidItem = bidItemRepository.findById(bidItemId).get();

		int todayMonth = ZonedDateTime.now().getMonth().getValue();
		int todayYear = ZonedDateTime.now().getYear() - 2000;
		int expiryMonth = Integer.parseInt(payInfo.getCardExpiry().substring(0, 2));
		int expiryYear = Integer.parseInt(payInfo.getCardExpiry().substring(3));

		if (expiryMonth <= todayMonth && expiryYear <= todayYear) {
			throw new BadRequestException("This card is expiried, please enter another card's information.");
		}
		if (bidItem.getBuyerUserId() != userId) {
			throw new BadRequestException("You are not the winner of this item.");
		}
		
		Payment pay = new Payment(payInfo.getPaymentType(), payInfo.getCardNumber(), payInfo.getNameOnCard(), payInfo.getCV(), payInfo.getCardExpiry(), payInfo.isExpeditedShipment());
		
		pay.setName(user.getName());
		pay.setEmail(user.getEmail());
		pay.setAddress(user.getAddress());
		pay.setUnit(user.getUnit());
		pay.setPostalCode(user.getPostalCode());
		pay.setCity(user.getCity());
		pay.setCountry(user.getCountry());
		pay.setPhone(user.getPhone());

		if (payInfo.isExpeditedShipment()) {
			pay.setTotalCost(bidItem.getBidPrice(), bidItem.getShipmentCost(), bidItem.getExpeditedShipmentCost());
		} else {
			pay.setTotalCost(bidItem.getBidPrice(), bidItem.getShipmentCost(), 0);
		}
		pay.setShipmentDays(bidItem.getShipmentDays());

		paymentRepository.save(pay);

		return ResponseEntity //
				.created(linkTo(methodOn(PaymentController.class).one(pay.getId())).toUri()) //
				.body(paymentAssembler.toModel(pay));
	}

	@DeleteMapping("/account/{userId}/bidItems/{bidItemId}/receipt/{payId}")
	public void deletePayemnt(@PathVariable Long payId) {
		paymentRepository.deleteById(payId);
	}
}