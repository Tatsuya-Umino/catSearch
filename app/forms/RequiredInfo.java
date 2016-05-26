package forms;

import play.data.validation.Constraints.*;

public class RequiredInfo {
	public String catName;

	@Required
	public Long prefId;

	public String prefName;

	@Required
	public String cityName;

	public String address;

	public String lastAddress;

	@Required
	public String standoutFeature;

	public String imagePath;

	public Long microNum;

	@Required
	public String message;
}
