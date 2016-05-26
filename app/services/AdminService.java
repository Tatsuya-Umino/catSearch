package services;

import models.AdminUser;
import models.Cat;
import models.Comment;
import models.Contributor;
import play.Logger;
import play.Play;
import play.mvc.Http.MultipartFormData.FilePart;
import services.mock.AppServiceMock;

import java.io.File;
import java.sql.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;

import dto.AdminUsersPagingDto;
import dto.PagingDto;
import forms.AdminListQuery;

public class AdminService {
	/**
	 *
	 * @param userName
	 * @param password
	 */
	public void register(String userName, String password) {
		AdminUser adminUser = new AdminUser();
		adminUser.adminUserName = userName;
		adminUser.adminUserPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		adminUser.registerdDate = new Date(System.currentTimeMillis());
		adminUser.save();
	}

	/**
	 *
	 * @param userName
	 * @return
	 */
	public boolean notExists(String userName) {
		AdminUser adminUser = AdminUser.find.where().eq("adminUserName", userName).findUnique();
		if(adminUser == null) {
			return true;
		}	else	{
			return false;
		}
	}

	/**
	 *
	 * @param cat
	 */
	public void store(Cat cat){
		cat.save();
	}

	/**
	 *
	 * @param comment
	 */
	public void storeComment(Comment comment) {
		comment.save();
	}

	/**
	 *
	 * @param comment
	 */
	public void storeContributor(Contributor contributor) {
		contributor.save();
	}

	/**
	 *
	 * @param userName
	 * @param password
	 * @return
	 */
	public boolean login(String userName, String password) {
		AdminUser adminUser = AdminUser.find.where().eq("adminUserName", userName).findUnique();
		if(adminUser == null) {
			return false;
		}
		if (BCrypt.checkpw(password, adminUser.adminUserPassword)) {
			adminUser.lastLoginDate = new Date(System.currentTimeMillis());
			adminUser.save();
			return true;
		} else {
			return false;
		}
	}

	/**
	 *
	 * @param page
	 * @return
	 */
	public PagingDto findCatsWithPages(int page, int itemPerPage, AdminListQuery query) {
		ExpressionList<Cat> el;
		if(query.sort.equals("desc")) {
			el = Cat.find.orderBy().desc("createTs").where();
		}	else	{
			el = Cat.find.orderBy().asc("createTs").where();
		}
		if(StringUtils.isNotEmpty(query.catName)) {
			el = el.where().like("catName", "%" + query.catName + "%");
		}
		if(StringUtils.isNotEmpty(query.contributorName)) {
			el = el.where().like("contributor.contributorName", "%" + query.contributorName + "%");
		}
		if(StringUtils.isNotEmpty(query.commentTitle)) {
			el = el.where().like("comments.commentTitle", "%" + query.commentTitle + "%");
		}
		if(StringUtils.isNotEmpty(query.commentMessage)) {
			el = el.where().like("comments.commentMessage", "%" + query.commentMessage + "%");
		}
		if(query.prefId != null && StringUtils.isNotEmpty(String.valueOf(query.prefId))) {
			el = el.where().eq("pref.prefId", query.prefId);
		}
		Page<Cat> catPage = el.findPagingList(itemPerPage).getPage(page - 1);
		PagingDto pagingDto = new PagingDto();
		pagingDto.cats = catPage.getList();
		pagingDto.currentPage = page;
		pagingDto.totalPageCount = catPage.getTotalPageCount();
		return pagingDto;
	}

	/**
	 *
	 * @param catId
	 */
	private void deleteCatById(Long catId) {
		Cat cat = Cat.find.byId(catId);
		if(cat != null) {
			cat.delete();
		}
	}

	/**
	 *
	 * @param contributorId
	 */
	private void deleteContributorById(Long contributorId) {
		Contributor contributor = Contributor.find.byId(contributorId);
		if(contributor != null) {
			contributor.delete();
		}
	}

	/**
	 *
	 * @param commnetId
	 */
	private void deleteCommentById(Long commnetId) {
		Comment comment = Comment.find.byId(commnetId);
		if(comment != null) {
			comment.delete();
		}
	}

	/**
	 *
	 * @param userId
	 */
	private void deleteAdminUserById(Long userId) {
		AdminUser adminUser = AdminUser.find.byId(userId);
		if(adminUser != null) {
			adminUser.delete();
		}
	}

	/**
	 *
	 * @param tableName
	 * @param id
	 */
	public void delete(String tableName,Long id) {
		if(tableName.equals("Cat")) {
			this.deleteCatById(id);
		}	else if(tableName.equals("Contributor")) {
			this.deleteContributorById(id);
		}	else if(tableName.equals("Comment")) {
			this.deleteCommentById(id);
		}	else if(tableName.equals("AdminUser")) {
			this.deleteAdminUserById(id);
		}
	}

	/**
	 *
	 * @param page
	 * @param itemPerPage
	 * @return
	 */
	public AdminUsersPagingDto findAdminUsersWithPages(int pageIndex,int itemPerPage) {
		Page<AdminUser> page = AdminUser.find.order().desc("createTs").findPagingList(itemPerPage).getPage(pageIndex - 1);
		AdminUsersPagingDto dto = new AdminUsersPagingDto();
		dto.currentPage = pageIndex;
		dto.totalPageCount = page.getTotalPageCount();
		dto.adminUsers = page.getList();
		return dto;
	}

	/**
	 *
	 * @param catId
	 * @return
	 */
	public Cat findCatById(Long catId) {
		Cat cat = Cat.find.byId(catId);
		return cat;
	}

	/**
	 *
	 * @param commentId
	 * @return
	 */
	public Comment findCommentById(Long commentId) {
		Comment comment = Comment.find.byId(commentId);
		return comment;
	}

	/**
	 *
	 * @param contributorId
	 * @return
	 */
	public Contributor findContributorById(Long contributorId) {
		Contributor contributor = Contributor.find.byId(contributorId);
		return contributor;
	}

	/**
	 *
	 * @param pic
	 * @return
	 */
	public String uploadImage(FilePart pic){
		if (pic != null) {
		     String fileName = pic.getFilename();
		     String contentType = pic.getContentType();
		     File file = pic.getFile();
		     if (contentType.equals("image/jpeg") ||
		    		 contentType.equals("image/jpg") ||
		    		 contentType.equals("image/png")) {
		    	 String fullPath = Play.application().path().getPath() + "/public/images/";
		    	 file.renameTo(new File(fullPath, fileName));
		    	 return "/assets/images/" + fileName;
		     }else{
		    	 return "jpgかpngでアップロードしてください。";
		     }
		} else {
		      return null;
		}
	}
}