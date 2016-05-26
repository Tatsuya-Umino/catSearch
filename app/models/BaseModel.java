package models;

import java.sql.Date;
import javax.persistence.MappedSuperclass;

import play.data.validation.Constraints.*;
import play.db.ebean.Model;

@MappedSuperclass
public class BaseModel extends Model {
	@Required
	public Date createTs;
	@Required
	public Date updateTs;
	@Required
	public Long versions = 0L;

	@Override
	public void save() {
		this.createTs = new Date(System.currentTimeMillis());
		this.updateTs = this.createTs;
		this.versions++;
		super.save();
	}
}