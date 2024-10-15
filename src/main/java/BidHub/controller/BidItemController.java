package BidHub.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import BidHub.assembler.BidItemModelAssembler;
import BidHub.dto.AddForwardBidItemPrice;
import BidHub.entity.BidItem;
import BidHub.entity.Status;
import BidHub.exception.BadRequestException;
import BidHub.exception.NotFoundException;
import BidHub.repository.BidItemRepository;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class BidItemController {

	private final BidItemRepository bidItemRepository;
	private final BidItemModelAssembler bidItemAssembler;

	BidItemController(BidItemRepository bidItemRepository, BidItemModelAssembler bidItemAssembler) {
		this.bidItemRepository = bidItemRepository;
		this.bidItemAssembler = bidItemAssembler;
	}

	@GetMapping("/auctionItems")
	public CollectionModel<EntityModel<BidItem>> all() {

		List<EntityModel<BidItem>> bidItems = bidItemRepository.findAll().stream() //
				.map(bidItemAssembler::toModel) //
				.collect(Collectors.toList());

		return CollectionModel.of(bidItems, //
				linkTo(methodOn(BidItemController.class).all()).withSelfRel());
	}

	@GetMapping("/auctionItems/{bidItemId}")
	public EntityModel<BidItem> one(@PathVariable Long bidItemId) {

		BidItem bidItem = bidItemRepository.findById(bidItemId) //
				.orElseThrow(() -> new NotFoundException("Item not found."));

		return bidItemAssembler.toModel(bidItem);
	}
	
	@GetMapping("/account/{userId}/auctionItems")
	public CollectionModel<EntityModel<BidItem>> allAuctionItems() {

		List<EntityModel<BidItem>> bidItems = bidItemRepository.findAll().stream() //
				.map(bidItemAssembler::toModel) //
				.collect(Collectors.toList());

		return CollectionModel.of(bidItems, //
				linkTo(methodOn(BidItemController.class).all()).withSelfRel());
	}

	@GetMapping("/account/{userId}/auctionItems/{bidItemId}")
	public EntityModel<BidItem> oneAuctionItem(@PathVariable Long bidItemId) {

		BidItem bidItem = bidItemRepository.findById(bidItemId) //
				.orElseThrow(() -> new NotFoundException("Item not found."));

		return bidItemAssembler.toModel(bidItem);
	}

	// This is all the auction items that have been won
	@GetMapping("/account/{userId}/allAuctionItemsWon")
	public CollectionModel<EntityModel<BidItem>> allWonBidItems() {

		List<EntityModel<BidItem>> bidItems = bidItemRepository.findAll().stream()
				.filter(item -> item.getStatus().equals(Status.SOLD)) //
				.map(bidItemAssembler::toModel).collect(Collectors.toList());

		return CollectionModel.of(bidItems, //
				linkTo(methodOn(BidItemController.class).all()).withSelfRel());
	}

	// User here is the one selling the item
	@GetMapping("/account/{userId}/sellingItems")
	public CollectionModel<EntityModel<BidItem>> userAllBidItems(@PathVariable Long userId) {

		List<EntityModel<BidItem>> bidItems = bidItemRepository.findAll().stream()
				.filter(item -> item.getSellerUserId() == userId).map(bidItemAssembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(bidItems, //
				linkTo(methodOn(BidItemController.class).all()).withSelfRel());
	}

	// User here is the one selling the item
	@GetMapping("/account/{userId}/sellingItems/{bidItemId}")
	public EntityModel<BidItem> userOneBidItem(@PathVariable Long bidItemId) {

		BidItem bidItem = bidItemRepository.findById(bidItemId) //
				.orElseThrow(() -> new NotFoundException("Item not found."));

		return bidItemAssembler.toModel(bidItem);
	}

	// User here is the one selling the item
	@PostMapping("/account/{userId}/sellingItems")
	ResponseEntity<EntityModel<BidItem>> newBidItem(@RequestBody BidItem bidItem, @PathVariable Long userId) {

		if (bidItem.getItemName() == null || bidItem.getDescription() == null) {
			throw new BadRequestException("Item name and description must be filled in.");
		}
		if (!bidItem.isDutch() && !bidItem.isForward()) {
			throw new BadRequestException(
					"Item must be part of one type of bidding: Dutch bidding or Forward bidding.");
		}
		if (bidItem.isDutch() && bidItem.isForward()) {
			throw new BadRequestException(
					"Item must be part of only one type of bidding: Dutch bidding or Forward bidding.");
		}
		if (bidItem.isDutch() && (bidItem.getDecrementAmount() <= 0 || bidItem.getLowestBidPrice() <= 0)) {
			throw new BadRequestException(
					"For this Dutch auction item, the decrement value and lowest bid price must be set to a value greater than 0.");
		}
		if (bidItem.isForward() && (bidItem.getDecrementAmount() > 0 || bidItem.getLowestBidPrice() > 0)) {
			throw new BadRequestException(
					"For Forward auction items, the decrement value and lowest bid price do not apply.");
		}
		
		ZonedDateTime time = ZonedDateTime.ofInstant(Instant.parse(bidItem.getEndTime()), ZoneId.systemDefault());
		if (ZonedDateTime.now().isAfter(time)) {
			if (bidItem.isDutch()) {
				throw new BadRequestException(
						"End time cannot be set to before today. End time is for how long this Dutch auction item will be up after the lowest bid price is hit much be set.");
			} else {
				throw new BadRequestException(
						"End time cannot be set to before today. End time must be set to after the current time for this Forward auction item.");
			}
		}

		if (bidItem.getBidPrice() <= 0) {
			throw new BadRequestException("Bid price must be more than 0.");
		}
		if (bidItem.getShipmentCost() <= 0 && bidItem.getShipmentDays() <= 0) {
			throw new BadRequestException("Cost of shipment and the days till shipment must be entered.");
		}

		bidItem.setSellerUserId(userId);
		bidItem.setStatus(Status.IN_PROGRESS);
		BidItem newBidItem = bidItemRepository.save(bidItem);

		return ResponseEntity //
				.created(linkTo(methodOn(BidItemController.class).one(newBidItem.getId())).toUri()) //
				.body(bidItemAssembler.toModel(newBidItem));
	}

	// User here can be the one selling the DUTCH bid item
	@PutMapping("/account/{userId}/sellingItems/{bidItemId}/decrement")
	ResponseEntity<?> decrementDutchBidItemPrice(@PathVariable Long userId, @PathVariable Long bidItemId) {

		BidItem updatingBidItem = bidItemRepository.findById(bidItemId).get();

		if (updatingBidItem.getStatus().equals(Status.SOLD)) {
			throw new BadRequestException("Item is unavailable to make changes.");
		}
		if (updatingBidItem.getSellerUserId() != userId) {
			throw new BadRequestException("You are not the seller of this item, you cannot make edits.");
		}
		if (!updatingBidItem.isDutch()) {
			throw new BadRequestException("This is not a Dutch bid item, so it cannot be edited.");
		}
		if (updatingBidItem.getBidPrice() <= updatingBidItem.getLowestBidPrice()) {
			throw new BadRequestException("Lowest price has been reached for this item, after "
					+ ZonedDateTime.ofInstant(Instant.parse(updatingBidItem.getEndTime()), ZoneId.systemDefault())
					+ " the item will be removed from the auction.");
		}

		updatingBidItem.setBidPrice(updatingBidItem.getBidPrice() - updatingBidItem.getDecrementAmount());
		bidItemRepository.save(updatingBidItem);

		EntityModel<BidItem> entityModel = bidItemAssembler.toModel(updatingBidItem);

		return ResponseEntity //
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}

	// User here can be the one buying the FORWARD bid item
	@PutMapping("/account/{userId}/auctionItem/{bidItemId}")
	ResponseEntity<?> addForwardBidItemPrice(@RequestBody AddForwardBidItemPrice addBidItemPrice,
			@PathVariable Long bidItemId, @PathVariable Long userId) {

		BidItem updatingBidItem = bidItemRepository.findById(bidItemId).get();

		if (updatingBidItem.getStatus().equals(Status.SOLD)) {
			throw new BadRequestException("Item is unavailable to make changes.");
		}
		if (!updatingBidItem.isForward()) {
			throw new BadRequestException("This is not a Forward auction item, so it cannot be edited.");
		}
		if (updatingBidItem.getSellerUserId().equals(userId)) {
			throw new BadRequestException("You are the seller of this item, you cannot add a bid to it.");
		}
		if (updatingBidItem.getCurrentBidder() != null && updatingBidItem.getCurrentBidder().equals(userId)) {
			throw new BadRequestException(
					"You are the highest bidder at the moment for this item, you cannot add another bid.");
		}
		if (addBidItemPrice.getBidPrice() <= updatingBidItem.getBidPrice()) {
			throw new BadRequestException("Bid price must be more than current price of this Forward item bid.");
		}

		updatingBidItem.setBidPrice(addBidItemPrice.getBidPrice());
		updatingBidItem.setCurrentBidder(userId);
		bidItemRepository.save(updatingBidItem);

		EntityModel<BidItem> entityModel = bidItemAssembler.toModel(updatingBidItem);

		return ResponseEntity //
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}

	// User here is the one buying the item
	@PutMapping("/account/{userId}/auctionItem/{bidItemId}/buyNow")
	public ResponseEntity<?> sold(@PathVariable Long bidItemId, @PathVariable Long userId) {

		BidItem bidItem = bidItemRepository.findById(bidItemId).get();

		if (bidItem.isDutch() && bidItem.getStatus().equals(Status.IN_PROGRESS)) {
			bidItem.setStatus(Status.SOLD);
			bidItem.setBuyerUserId(userId);
			return ResponseEntity.ok(bidItemAssembler.toModel(bidItemRepository.save(bidItem)));
		}
		else if (bidItem.isForward() && bidItem.getStatus().equals(Status.IN_PROGRESS)) {
			if (bidItem.getCurrentBidder() != null && bidItem.getCurrentBidder().equals(userId)) {
				bidItem.setStatus(Status.SOLD);
				bidItem.setBuyerUserId(userId);
				return ResponseEntity.ok(bidItemAssembler.toModel(bidItemRepository.save(bidItem)));
			} else {
				throw new BadRequestException(
						"You are not the highest bidder at the moment for this item, you cannot buy the item.");
			}
		}

		return ResponseEntity //
				.status(HttpStatus.METHOD_NOT_ALLOWED) //
				.header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
				.body(Problem.create() //
						.withTitle("Method not allowed") //
						.withDetail("You can't complete an bidItem that is in the " + bidItem.getStatus() + " status"));
	}

	// User here is the one selling the item - getting items they have sold
	@GetMapping("/account/{userId}/sellingItems/sold")
	public CollectionModel<EntityModel<BidItem>> userAllSoldBidItems(@PathVariable Long userId) {

		List<EntityModel<BidItem>> bidItems = bidItemRepository.findAll().stream()
				.filter(item -> item.getSellerUserId() == userId && item.getStatus().equals(Status.SOLD)) //
				.map(bidItemAssembler::toModel).collect(Collectors.toList());

		return CollectionModel.of(bidItems, //
				linkTo(methodOn(BidItemController.class).all()).withSelfRel());
	}

	// User here is the one buying the item - items that they have won
	@GetMapping("/account/{userId}/auctionItems/won")
	public CollectionModel<EntityModel<BidItem>> userAllBoughtBidItems(@PathVariable Long userId) {

		List<EntityModel<BidItem>> bidItems = bidItemRepository.findAll().stream()
				.filter(item -> item.getBuyerUserId() == userId && item.getStatus().equals(Status.SOLD)) //
				.map(bidItemAssembler::toModel).collect(Collectors.toList());

		return CollectionModel.of(bidItems, //
				linkTo(methodOn(BidItemController.class).all()).withSelfRel());
	}

	// User here is the one selling the item - cancel an item in the bid
	@DeleteMapping("/account/{userId}/sellingItems/{bidItemId}/cancel")
	public ResponseEntity<?> cancel(@PathVariable Long bidItemId) {

		BidItem bidItem = bidItemRepository.findById(bidItemId).get();

		if (bidItem.getStatus() == Status.SOLD) {
			throw new BadRequestException("Item is unavailable to make changes.");
		}
		bidItemRepository.deleteById(bidItemId);
//			ResponseEntity.ok("Auction item has been removed from auction");
		return ResponseEntity.noContent().build();
	}

	// delete item for which the time is up
	@DeleteMapping("/auctionItems/{bidItemId}/delete")
	public void delete(@PathVariable Long bidItemId) {

		BidItem bidItem = bidItemRepository.findById(bidItemId).get();

		ZonedDateTime time = ZonedDateTime.ofInstant(Instant.parse(bidItem.getEndTime()), ZoneId.systemDefault());
		if (ZonedDateTime.now().isAfter(time)) {
			bidItemRepository.deleteById(bidItemId);
		}
	}
}