package forms;

import play.data.validation.Constraints.*;

public class AdminUserForm {
	@Required
	public String adminUserName;

	@Required
	public String adminUserPassword;
}
