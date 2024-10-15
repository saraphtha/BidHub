package BidHub.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import BidHub.controller.PaymentController;
import BidHub.entity.Payment;

@Component
public class PaymentModelAssembler implements RepresentationModelAssembler<Payment, EntityModel<Payment>> {

	@Override
	public EntityModel<Payment> toModel(Payment payment) {

		EntityModel<Payment> paymentModel = EntityModel.of(payment,
				linkTo(methodOn(PaymentController.class).one(payment.getId())).withSelfRel(),
				linkTo(methodOn(PaymentController.class).all()).withRel("payments"));


		return paymentModel;
	}
}