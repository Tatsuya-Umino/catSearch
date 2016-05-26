package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import play.data.validation.Constraints.*;
import play.db.ebean.Model.Finder;

@Entity
@Table(name="Comments")
public class Comment extends BaseModel {
	@Id
	@Required
	public Long commentId;


	@JsonBackReference
	@ManyToOne(cascade=CascadeType.ALL)
	public Cat cat;

	public String commentTitle;

	public String commentName;

	public String mailAddress;

	public String commentMessage;

	public String commentImage;

	public static Finder<Long, Comment> find =
			new Finder<Long, Comment>(Long.class, Comment.class);
}