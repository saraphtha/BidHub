package BidHub.controller;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import BidHub.exception.BadRequestException;
import BidHub.exception.NotFoundException;
import BidHub.exception.UnauthorizedRequestException;
import BidHub.assembler.UserModelAssembler;
import BidHub.dto.ChangePassword;
import BidHub.dto.EditUser;
import BidHub.dto.ForgotPassword;
import BidHub.dto.SignIn;
import BidHub.dto.SignUp;
import BidHub.entity.Role;
import BidHub.entity.User;
import BidHub.repository.UserRepository;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class UserController {

	private final UserRepository userRepository;
	private final UserModelAssembler userAssembler;

	private boolean checkUsernameAvailable(String username) throws Exception {
		Optional<User> userFound = userRepository.findByUsername(username);
		if (userFound.isPresent()) {
			throw new BadRequestException("Username is already exists.");
		}
		return true;
	}

	private boolean checkUsernameValid(String username) throws Exception {
		if (!Pattern.matches("^(?=\\S+$).{5,}$", username)) {
			throw new BadRequestException("Username must be at least 5 charcters long, and not contain any spaces.");
		}
		return true;
	}

	UserController(UserRepository userRepository, UserModelAssembler userAssembler) {
		this.userRepository = userRepository;
		this.userAssembler = userAssembler;
	}

	@GetMapping("/listUsers")
	public CollectionModel<EntityModel<User>> allUsers() {
		List<EntityModel<User>> users = userRepository.findAll().stream().map(userAssembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(users, linkTo(methodOn(UserController.class).allUsers()).withSelfRel());
	}

	@GetMapping("/listUsers/{listUserId}")
	public EntityModel<User> oneUser(@PathVariable Long listUserId) {

		User user = userRepository.findById(listUserId).orElseThrow(() -> new NotFoundException("User not found."));

		return userAssembler.toModel(user);
	}

	@GetMapping("/welcome")
	public String welcome() {
		return "welcome";
	}

	@PostMapping("/signIn")
	public EntityModel<User> signIn(@RequestBody SignIn userSignIn) {

		User user = userRepository.findByUsername(userSignIn.getUsername())
				.orElseThrow(() -> new NotFoundException("User not found."));

		if (!user.getPassword().equals(userSignIn.getPassword())) {
			throw new BadRequestException("Password is incorrect.");
		}

		return userAssembler.toModel(user);
	}

	@PutMapping("/signIn/forgotPassword")
	ResponseEntity<?> userForgotPassword(@RequestBody ForgotPassword forgotPassword) throws Exception {

		User user = userRepository.findByUsername(forgotPassword.getUsername())
				.orElseThrow(() -> new NotFoundException("User not found."));

		if (!Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$",
				forgotPassword.getNewPassword())) {
			throw new BadRequestException(
					"Password must be at least 8 charcters long, at least 1 upper case and 1 lower case letter, at least 1 number, and at least 1 special character.");
		}
		if (!forgotPassword.getNewPassword().equals(forgotPassword.getConfirmNewPassword())) {
			throw new BadRequestException("Password and Confirm Password do not match.");
		}
		user.setPassword(forgotPassword.getNewPassword());

		EntityModel<User> entityModel = userAssembler.toModel(userRepository.save(user));

		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	@PostMapping("/signUp")
	ResponseEntity<?> signUp(@RequestBody SignUp newUser) throws Exception {

		if (newUser.getFirstName() == null || newUser.getLastName() == null || newUser.getEmail() == null
				|| newUser.getPhone() == null || newUser.getAddress() == null || newUser.getPostalCode() == null
				|| newUser.getCity() == null || newUser.getCountry() == null || newUser.getUsername() == null
				|| newUser.getPassword() == null || newUser.getConfirmPassword() == null) {
			throw new BadRequestException("All fields must be filled in, 'Unit' is the only exception.");
		}
		if (checkUsernameAvailable(newUser.getUsername()) && checkUsernameValid(newUser.getUsername())) {
			if (!Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+=])(?=\\S+$).{8,}$",
					newUser.getPassword())) {
				throw new BadRequestException(
						"Password must be at least 8 charcters long, at least 1 upper case and 1 lower case letter, at least 1 number, and at least 1 special character.");
			}
			if (!newUser.getPassword().equals(newUser.getConfirmPassword())) {
				throw new BadRequestException("Password and Confirm Password do not match.");
			}
		}

		User user = new User();
		user.setName(newUser.getName());
		user.setEmail(newUser.getEmail());
		user.setAddress(newUser.getAddress());
		user.setUnit(newUser.getUnit());
		user.setPostalCode(newUser.getPostalCode());
		user.setCity(newUser.getCity());
		user.setCountry(newUser.getCountry());
		user.setPhone(newUser.getPhone());
		user.setUsername(newUser.getUsername());
		user.setPassword(newUser.getPassword());
		user.setRole(Role.ROLE_USER);

		EntityModel<User> entityModel = userAssembler.toModel(userRepository.save(user));

		return ResponseEntity //
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}

	@GetMapping("/account/{userId}/listUsers")
	public CollectionModel<EntityModel<User>> adminAcessAllUsers(@PathVariable Long userId) {

		if (userRepository.findById(userId).get().getRole().equals(Role.ROLE_USER)) {
			throw new UnauthorizedRequestException("You are not authorized to access the page.");
		}

		List<EntityModel<User>> users = userRepository.findAll().stream().map(userAssembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(users, linkTo(methodOn(UserController.class).allUsers()).withSelfRel());
	}

	@GetMapping("/account/{userId}/listUsers/{listUserId}")
	public EntityModel<User> adminAcessOneUser(@PathVariable Long userId, @PathVariable Long listUserId) {

		if (userRepository.findById(userId).get().getRole().equals(Role.ROLE_USER)) {
			throw new UnauthorizedRequestException("You are not authorized to access the page.");
		}

		User user = userRepository.findById(listUserId).orElseThrow(() -> new NotFoundException("User not found."));

		return userAssembler.toModel(user);
	}

	/*
	 * Username cannot be changed, and password change will be done through change
	 * passowrd method.
	 */
	@PutMapping("/account/{userId}/editUser")
	ResponseEntity<?> editUser(@RequestBody EditUser editUser, @PathVariable Long userId) throws Exception {

		User updatedUser = userRepository.findById(userId).get();

		if (editUser.getFirstName() != null) {
			updatedUser.setFirstName(editUser.getFirstName());
		}
		if (editUser.getLastName() != null) {
			updatedUser.setLastName(editUser.getLastName());
		}
		if (editUser.getEmail() != null) {
			updatedUser.setEmail(editUser.getEmail());
		}
		if (editUser.getPhone() != null) {
			updatedUser.setPhone(editUser.getPhone());
		}
		if (editUser.getAddress() != null) {
			updatedUser.setAddress(editUser.getAddress());
		}
		if (editUser.getUnit() != null) {
			updatedUser.setUnit(editUser.getUnit());
		}
		if (editUser.getPostalCode() != null) {
			updatedUser.setPostalCode(editUser.getPostalCode());
		}
		if (editUser.getCity() != null) {
			updatedUser.setCity(editUser.getCity());
		}
		if (editUser.getCountry() != null) {
			updatedUser.setCountry(editUser.getCountry());
		}

		userRepository.save(updatedUser);
		EntityModel<User> entityModel = userAssembler.toModel(updatedUser);

		return ResponseEntity //
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}

	@PutMapping("/account/{userId}/changePassword")
	ResponseEntity<?> editUserPassword(@RequestBody ChangePassword changePassword, @PathVariable Long userId)
			throws Exception {

		User updatedUser = userRepository.findById(userId).get();

		if (!updatedUser.getPassword().equals(changePassword.getCurrentPassword())) {
			throw new BadRequestException("Current Password is incorrect.");
		}

		if (!Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$",
				changePassword.getNewPassword())) {
			throw new BadRequestException(
					"New password must be at least 8 charcters long, at least 1 upper case and 1 lower case letter, at least 1 number, and at least 1 special character.");
		}

		if (updatedUser.getPassword().equals(changePassword.getNewPassword())) {
			throw new BadRequestException("New password must be different from previous.");
		}

		if (!changePassword.getNewPassword().equals(changePassword.getConfirmNewPassword())) {
			throw new BadRequestException("New Password and Confirm New Password fields do not match.");
		}

		updatedUser.setPassword(changePassword.getNewPassword());
		userRepository.save(updatedUser);

		EntityModel<User> entityModel = userAssembler.toModel(updatedUser);

		return ResponseEntity //
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}

	@DeleteMapping("/account/{userId}/delete")
	ResponseEntity<?> deleteUser(@PathVariable Long userId) {

		userRepository.deleteById(userId);

		return ResponseEntity.noContent().build();
	}
}