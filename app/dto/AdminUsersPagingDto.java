package dto;

import java.util.ArrayList;
import java.util.List;

import models.AdminUser;

public class AdminUsersPagingDto {
	public int totalPageCount;
	public int currentPage;
	public List<AdminUser> adminUsers = new ArrayList<AdminUser>();
}
