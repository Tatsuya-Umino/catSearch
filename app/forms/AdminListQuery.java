package forms;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class AdminListQuery {
	/** */
	public String catName;

	/** */
	public Long prefId;

	/** */
	public String contributorName;

	/** */
	public String commentTitle;

	/** */
	public String commentMessage;

	/** */
	public String sort = "asc";

	public Integer page;

	/**
	 * 変数名と値をLinkedHashMapに変換する。
	 * @return
	 */
	public Map<String,String> toLinkedHashMap() {
		Map<String,String> map = new LinkedHashMap<String,String>();
		if(StringUtils.isNotEmpty(this.catName)) map.put("catName", this.catName);
		if(this.prefId != null && StringUtils.isNotEmpty(String.valueOf(this.prefId)))
			map.put("prefId", String.valueOf(this.prefId));
		if(StringUtils.isNotEmpty(this.contributorName))
			map.put("contributorName", this.contributorName);
		if(StringUtils.isNotEmpty(this.commentTitle))
			map.put("commentTitle", this.commentTitle);
		if(StringUtils.isNotEmpty(this.commentMessage))
			map.put("commentMessage", this.commentMessage);
		if(StringUtils.isNotEmpty(this.sort)) map.put("sort", this.sort);
		return map;
	}
}