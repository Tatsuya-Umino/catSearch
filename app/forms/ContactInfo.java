package forms;

import play.data.validation.Constraints.Required;

public class ContactInfo {
	@Required
	public String contactName;

	@Required
	public String contactMail;

	@Required
	public String contactMessage;

}
