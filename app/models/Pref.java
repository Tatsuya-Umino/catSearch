package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model.Finder;

import java.util.List;
import java.util.ArrayList;
import models.Cat;
import models.BaseModel;
import services.mock.AppServiceMock;

@Entity
@Table(name="Prefs")
public class Pref extends BaseModel{
	@Id
	@Required
	public Long prefId;

	@Required
	public String prefName;

	@JsonBackReference
	@OneToMany(cascade = CascadeType.ALL)
	public List<Cat> cats = new ArrayList<Cat>();

	public static Finder<Long, Pref> find =
			new Finder<Long, Pref>(Long.class, Pref.class);

	// idから都道府県名をもらう
	public static String getPrefName(Long id) {
		AppServiceMock mock = new AppServiceMock();
		List<Pref> prefs = mock.loadPrefs();
		Integer i = new Integer(id.toString());
		return prefs.get(i).prefName;
	}
}
