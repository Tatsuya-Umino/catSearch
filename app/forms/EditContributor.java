package forms;

import models.Contributor;
import play.data.validation.Constraints.*;

public class EditContributor {
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

	public Contributor updateContributor(Contributor contributor) {
		contributor.contributorName = this.contributorName;
		contributor.contributorNickName = this.contributorNickName;
		contributor.birthYear = this.birthYear;
		contributor.birthMonth = this.birthMonth;
		contributor.birthDay = this.birthDay;
		contributor.mailAddress = this.mailAddress;
		contributor.sex = this.sex;
		return contributor;
	}

	/**
	 *
	 * @param contributor
	 */
	public EditContributor(Contributor contributor) {
		this.contributorId = contributor.contributorId;
		this.contributorName = contributor.contributorName;
		this.contributorNickName = contributor.contributorNickName;
		this.birthYear = contributor.birthYear;
		this.birthMonth = contributor.birthMonth;
		this.birthDay = contributor.birthDay;
		this.mailAddress = contributor.mailAddress;
		contributor.sex = this.sex;
	}

	/**
	 *
	 */
	public EditContributor() {}
}
