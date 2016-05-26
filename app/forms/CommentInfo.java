package forms;

import play.data.validation.Constraints.*;

public class CommentInfo {

	@Required
	public String commenterName;

	@Required
	public String title;

	public String imagePath;

	@Required
	@Email
	public String commenterMail;

	@Required
	public String comment;


}
