package models;

import play.data.validation.Constraints.Required;

public class Search {
	@Required	
	public Long		tdfkId;
	public String	skgnName;
	public String	csName;
	public String	keyword;
	public Integer	protect;
}