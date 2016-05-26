package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model.Finder;

@Entity
@Table(name="Contributors")
public class Contributor extends BaseModel {
	@Id
	@Required
	public Long contributorId;

	@Required
	public String contributorName;

	@Required
	public String contributorNickName;

	public String birthYear;

	public String birthMonth;

	public String birthDay;

	public String mailAddress;

	public Integer sex;

	public Integer howToArriveId;

	public String hashId;

	@JsonBackReference
	@OneToMany(cascade = CascadeType.ALL)
	public List<Cat> cats;

	public static Finder<Long, Contributor> find =
			new Finder<Long, Contributor>(Long.class, Contributor.class);
}