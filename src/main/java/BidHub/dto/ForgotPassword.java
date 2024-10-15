package BidHub.dto;

public class ForgotPassword {

	private String username;
	private String newPassword;
	private String confirmNewPassword;

	public String getUsername() {
		return this.username;
	}

	public String getConfirmNewPassword() {
		return this.confirmNewPassword;
	}

	public String getNewPassword() {
		return this.newPassword;
	}
}
