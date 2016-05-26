package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;

@Entity
@Table(name="Knows")
public class HowToArrive extends BaseModel {
	@Id
	@Required
	public Long howToArriveId;

	@Required
	public String howToArriveName;

	public static Finder<Long, HowToArrive> find =
			new Finder<Long, HowToArrive>(Long.class, HowToArrive.class);
}
