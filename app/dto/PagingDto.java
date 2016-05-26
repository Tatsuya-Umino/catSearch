package dto;

import java.util.ArrayList;
import java.util.List;

import models.Cat;

public class PagingDto {
	public int totalPageCount;
	public int currentPage;
	public List<Cat> cats = new ArrayList<Cat>();
}
