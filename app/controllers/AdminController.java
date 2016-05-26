package controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import controllers.components.AdminSecured;
import forms.AdminUserForm;
import forms.EditCat;
import forms.EditComment;
import forms.EditContributor;
import models.Cat;
import models.Comment;
import models.Contributor;
import forms.AdminListQuery;
import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.With;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import services.AdminService;
import services.PrefService;
import services.SessionNames;
import services.UrlService;
import views.html.index;
import views.html.admin.*;
import dto.AdminUsersPagingDto;
import dto.PagingDto;
import controllers.components.*;

public class AdminController extends Controller {
	/** */
	private static AdminService adminService = new AdminService();

	private static PrefService prefService = new PrefService();

	private static UrlService urlService = new UrlService();

	@With(BasicAuth.class)
	public static Result login() {
		if(session().get(SessionNames.ADMIN_USER_NAME) != null) {
			return redirect(routes.AdminController.list());
		}
        return ok(login.render(Form.form(AdminUserForm.class)));
    }

	/**
	 *
	 * @return
	 */
	@With(BasicAuth.class)
	public static Result doLogin() {
		if(session().get(SessionNames.ADMIN_USER_NAME) != null) {
			return redirect(routes.AdminController.list());
		}
		Form<AdminUserForm> adminUserForm = Form.form(AdminUserForm.class).bindFromRequest();
		if(!adminUserForm.hasErrors()) {
			if(adminService.login(adminUserForm.get().adminUserName,
					adminUserForm.get().adminUserPassword)) {
				session(SessionNames.ADMIN_USER_NAME,adminUserForm.get().adminUserName);
				return redirect(routes.AdminController.list());
			}	else	{
				//adminUserForredirectm.errors().get("adminUserName").add(new ValidationError("adminUserName","hoge"));
				//adminUserForm.errors().get("adminUserPassword").add(new ValidationError("adminUserPassword","hoge"));
			}
		}
		return ok(login.render(adminUserForm));
    }

	/**
	 *
	 * @return
	 */
	@With(BasicAuth.class)
	public static Result register() {
        return ok(register.render(Form.form(AdminUserForm.class)));
    }

	/**
	 *
	 * @return
	 */
	@With(BasicAuth.class)
	public static Result doRegister() {
		Form<AdminUserForm> adminUserForm = Form.form(AdminUserForm.class).bindFromRequest();
		if(!adminUserForm.hasErrors()) {
			if(adminService.notExists(adminUserForm.get().adminUserName)) {
				adminService.register(
						adminUserForm.get().adminUserName,
						adminUserForm.get().adminUserPassword);
				return redirect(routes.AdminController.login());
			}
		}
		return ok(register.render(Form.form(AdminUserForm.class)));
    }

	/**
	 * @return
	 */
	@With(BasicAuth.class)
	@Security.Authenticated(AdminSecured.class)
	public static Result list() {
		Form<AdminListQuery> adminListQueryForm = Form.form(AdminListQuery.class).bindFromRequest();
		int page = 1;
		if(adminListQueryForm.get().page != null) {
			page = adminListQueryForm.get().page;
		}
		final int itemPerPage = 10;
		PagingDto pagingDto = adminService.findCatsWithPages(page, itemPerPage , adminListQueryForm.get());
		String queryString = urlService.buildQuery(adminListQueryForm.get().toLinkedHashMap());
		return ok(
				list.render(Form.form(AdminListQuery.class),
				pagingDto,
				prefService.loadPrefsAsTuple(),
				queryString
				));
	}

	/**
	 *
	 * @return
	 */
	@With(BasicAuth.class)
	@Security.Authenticated(AdminSecured.class)
	public static Result edit(Long catId) {
		Cat cat = adminService.findCatById(catId);
		if(cat == null) {
			return redirect(routes.AdminController.list());
		}	else	{
			Form<EditCat> editCatForm = Form.form(EditCat.class).fill(new EditCat(cat));
			return ok(edit.render(editCatForm));
		}
	}

	/**
	 *
	 * @return
	 */
	@With(BasicAuth.class)
	@Security.Authenticated(AdminSecured.class)
	public static Result doEdit() {
		Form<EditCat> editCatForm = Form.form(EditCat.class).bindFromRequest();
		if(request().body().asMultipartFormData().asFormUrlEncoded().containsKey("deletePic")) {
			editCatForm.get().deletePic = true;
	    }	else	{
	    	editCatForm.get().deletePic = false;
	    }
		MultipartFormData body = request().body().asMultipartFormData();
	    FilePart picture = body.getFile("catPicture");
	    String catImage = adminService.uploadImage(picture);
		if(!editCatForm.hasErrors()) {
			Cat cat = adminService.findCatById(editCatForm.get().catId);
			cat.catImage = catImage;
			cat = editCatForm.get().updateCat(cat);
			if(cat != null) {
				adminService.store(cat);
				return redirect(routes.AdminController.list());
			}
		}
		for(String key : editCatForm.errors().keySet()) {
			Logger.error(key + " : " + editCatForm.errors().get(key));
		}
		return ok(edit.render(editCatForm));
	}

	/**
	 *
	 * @return
	 */
	public static Result editComment(Long commentId) {
		Comment comment = adminService.findCommentById(commentId);
		if(comment == null) {
			return redirect(routes.AdminController.list());
		}	else	{
			Form<EditComment> editCommentForm = Form.form(EditComment.class)
					.fill(new EditComment(comment));
			return ok(editComment.render(editCommentForm));
		}
	}

	/**
	 *
	 * @return
	 */
	public static Result doEditComment() {
		Form<EditComment> commentForm = Form.form(EditComment.class).bindFromRequest();
		if(request().body().asMultipartFormData().asFormUrlEncoded().containsKey("deletePic")) {
			commentForm.get().deletePic = true;
	    }	else	{
	    	commentForm.get().deletePic = false;
	    }
		MultipartFormData body = request().body().asMultipartFormData();
	    FilePart picture = body.getFile("commentPicture");
	    String commentImage = adminService.uploadImage(picture);
		if(!commentForm.hasErrors()) {
			Comment comment = adminService.findCommentById(commentForm.get().commentId);
			comment.commentImage = commentImage;
			comment = commentForm.get().updateComment(comment);
			adminService.storeComment(comment);
			return redirect(routes.AdminController.list());
		}	else	{
			return ok(editComment.render(commentForm));
		}
	}

	/**
	 *
	 * @return
	 */
	public static Result editContributor(Long contributorId) {
		Contributor contributor = adminService.findContributorById(contributorId);
		if(contributor == null) {
			return redirect(routes.AdminController.list());
		}	else	{
			Form<EditContributor> contributorForm = Form.form(EditContributor.class)
					.fill(new EditContributor(contributor));
			return ok(editContributor.render(contributorForm));
		}
	}

	/**
	 *
	 * @return
	 */
	public static Result doEditContributor() {
		Form<EditContributor> editContributorForm = Form.form(EditContributor.class)
				.bindFromRequest();
		if(!editContributorForm.hasErrors()) {
			Contributor contributor = adminService
					.findContributorById(editContributorForm.get().contributorId);
			if(contributor != null) {
				adminService.storeContributor(editContributorForm.get()
						.updateContributor(contributor));
			}
			return redirect(routes.AdminController.list());
		}	else	{
			return ok(editContributor.render(editContributorForm));
		}
	}


	/**
	 *
	 * @return
	 */
	public static Result adminUsersList() {
		Integer page = 1;
		try {
			page = Integer.parseInt(request().queryString().get("page")[0]);
		} catch(Exception e) {
			e.printStackTrace();
		}
		int itemPerPage = 10;
		AdminUsersPagingDto dto = adminService.findAdminUsersWithPages(page, itemPerPage);
		return ok(users.render(dto));
	}

	/**
	 *
	 * @return
	 */
	public static Result deleteAdminUser() {
		try {
			Map<String,String[]> params = request().body().asFormUrlEncoded();
			Long id = Long.parseLong(params.get("userId")[0]);
			adminService.delete("AdminUser", id);
			return redirect(routes.AdminController.adminUsersList());
		} catch(Exception e) {
			return badRequest("削除に失敗しました。");
		}
	}

	/**
	 *
	 * @return
	 */
	@With(BasicAuth.class)
	@Security.Authenticated(AdminSecured.class)
	public static Result delete() {
		try {
			Map<String,String[]> params = request().body().asFormUrlEncoded();
			String tableName = params.get("table")[0];
			Long id = Long.parseLong(params.get("id")[0]);
			adminService.delete(tableName, id);
			return redirect(routes.AdminController.list());
		} catch(Exception e) {
			e.printStackTrace();
			return badRequest("削除に失敗しました。");
		}
	}

	/**
	 *
	 * @return
	 */
	@With(BasicAuth.class)
	@Security.Authenticated(AdminSecured.class)
	public static Result doLogout() {
		session().clear();
        return redirect(routes.AdminController.login());
	}
}