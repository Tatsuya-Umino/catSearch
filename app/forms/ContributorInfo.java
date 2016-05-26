package forms;

import play.data.validation.Constraints.*;
import java.util.*;

public class ContributorInfo{
	/**
	 *
	 */
	@Required
	public String contributorName;

	/**
	 *
	 */
	@Required
	public String contributorNickName;

	/**
	 *
	 */
	@Pattern(value="^[0-9]*$", message="数字で入力してください")
	public String birthYear;



	/**
	 *
	 */
	@Pattern(value="^[0-9]*$", message="数字で入力してください")
	public String birthMonth;

	/**
	 *
	 */
	@Pattern(value="^[0-9]*$", message="数字で入力してください")
	public String birthDay;

	@Required
	@Email
	public String mailAddress;

	@Required
	public Integer sex;

	/**
	 *
	 */
	@Required
	public Integer howToArriveId;

}