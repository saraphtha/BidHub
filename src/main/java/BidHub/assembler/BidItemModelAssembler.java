package BidHub.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import BidHub.controller.BidItemController;
import BidHub.entity.BidItem;
import BidHub.entity.Status;

@Component
public class BidItemModelAssembler implements RepresentationModelAssembler<BidItem, EntityModel<BidItem>> {

	@Override
	public EntityModel<BidItem> toModel(BidItem bidItem) {

		// Unconditional links to single-item resource and aggregate root

		EntityModel<BidItem> bidItemModel = EntityModel.of(bidItem,
				linkTo(methodOn(BidItemController.class).one(bidItem.getId())).withSelfRel(),
				linkTo(methodOn(BidItemController.class).all()).withRel("bidItems"));

		// Conditional links based on state of the bidItem

		if (bidItem.getStatus() == Status.IN_PROGRESS) {
			bidItemModel.add(linkTo(methodOn(BidItemController.class).sold(bidItem.getId(), bidItem.getBuyerUserId()))
					.withRel("sold"));
			bidItemModel.add(linkTo(methodOn(BidItemController.class).cancel(bidItem.getId())).withRel("cancel"));
		}

		return bidItemModel;
	}
}