package forms;

import java.io.File;

import models.Comment;
import play.Play;

public class EditComment {
	public Long commentId;

	public String commentTitle;

	public String commentName;

	public String mailAddress;

	public String commentMessage;

	public boolean deletePic;

	public String commentImage;

	/**
	 *
	 * @param comment
	 * @return
	 */
	public Comment updateComment(Comment comment) {
		comment.commentTitle = this.commentTitle;
		comment.commentName = this.commentName;
		comment.mailAddress = this.mailAddress;
		comment.commentMessage = this.commentMessage;
		if(this.deletePic) {
			new File(Play.application().path().getPath() + comment.commentImage).delete();
			comment.commentImage = null;
		}
		return comment;
	}

	/**
	 *
	 * @param comment
	 */
	public EditComment(Comment comment) {
		this.commentId = comment.commentId;
		this.commentTitle = comment.commentTitle;
		this.commentName = comment.commentName;
		this.mailAddress = comment.mailAddress;
		this.commentMessage = comment.commentMessage;
		this.commentImage = comment.commentImage;
	}

	/**
	 *
	 */
	public EditComment() {}
}