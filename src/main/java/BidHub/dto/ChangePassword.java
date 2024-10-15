package BidHub.dto;

public class ChangePassword {

	private String currentPassword;
	private String newPassword;
	private String confirmNewPassword;

	public String getCurrentPassword() {
		return this.currentPassword;
	}

	public String getConfirmNewPassword() {
		return this.confirmNewPassword;
	}

	public String getNewPassword() {
		return this.newPassword;
	}
}
