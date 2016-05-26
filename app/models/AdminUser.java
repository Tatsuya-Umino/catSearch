package models;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model.Finder;

@Entity
@Table(name="AdminUsers")
public class AdminUser extends BaseModel {
	@Id
	@Required
	public Long adminUserId;

	@Required
	public String adminUserName;

	@Required
	public String adminUserPassword;

	public Date registerdDate;

	public Date lastLoginDate;

	public static Finder<Long, AdminUser> find =
			new Finder<Long, AdminUser>(Long.class, AdminUser.class);
}
