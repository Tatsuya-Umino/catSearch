package controllers;

import play.mvc.*;
import static play.data.Form.*;
import views.html.*;
import views.html.template.*;
import java.util.*;

import org.apache.commons.mail.EmailException;
import org.mindrot.jbcrypt.BCrypt;

import play.Play;
import play.api.mvc.Call;
import play.cache.Cache;
import services.mock.*;
import dto.PagingDto;
import forms.*;
import play.data.*;
import play.data.validation.ValidationError;
import models.*;
import scala.Tuple2;
import views.html.sp.*;
import services.*;
import com.avaje.ebean.Query;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.*;
import java.io.File;

public class Application extends Controller {
	static AppService appS = new AppService();

	static UserAgentService userAgentS = new UserAgentService();

	public static Result index() {
		List<Cat> cats = Cat.find.all();
		if(cats.size() == 0) {
			AppServiceMock mock = new AppServiceMock();
			mock.savePref();
			mock.saveCats();
		}
		PagingDto pd = new PagingDto();
		pd = appS.findCatsWithPages(1, 10);
		if(userAgentS.accessedBySp()) {
			return ok(sp_index.render("index"));
		} else {
			return ok(index.render("index", pd.cats));
		}
	}

	public static Result search(){
		String type = Form.form().bindFromRequest().get("type");
		Form<Search> form = Form.form(Search.class);
		List<Tuple2<String, String>> opts = getPref();
		List<Cat> cats = new ArrayList<Cat>();
		if(type.equals("search")){
			Query<Cat> query = Cat.find.where("flg = '0'");
			cats = query.findList();
			if(userAgentS.accessedBySp()) {
				return ok(sp_searchSearch.render("Ssearch", form, opts, cats, new Call("POST", "/result/1?type=search")));
			} else {
				return ok(searchSearch.render("Ssearch", form, opts, cats, new Call("POST", "/result/1?type=search")));
			}
		}else{
			Query<Cat> query = Cat.find.where("flg = '1'");
			cats = query.findList();
			if(userAgentS.accessedBySp()) {
				return ok(sp_protectSearch.render("Psearch", form, opts, cats, new Call("POST", "/result/1?type=protect")));
			} else {
				return ok(protectSearch.render("Psearch", form, opts, cats, new Call("POST", "/result/1?type=protect")));
			}
		}
	}

	public static Result result(Integer page){
		String type = Form.form().bindFromRequest().get("type");
		Form<Search> form = Form.form(Search.class).bindFromRequest();
		Search search = new Search();
		Integer pageMax = 1;
		List<Cat> cats = new ArrayList<Cat>();
		String searchName = "";
		String keyword = "";
		Integer searchNum = 0;
		if(!form.hasErrors()) {
			search = form.get();
			searchName = Pref.getPrefName(search.tdfkId);
			if(!search.skgnName.equals("例 : 猫山区")) searchName += search.skgnName;
			if(!search.csName.equals("例 : 猫谷町")) searchName += search.csName;
			if(!search.keyword.equals("") && search.keyword != null && !search.keyword.equals("例 : 赤い首輪")) keyword = "キーワード:"+search.keyword;
		} else {
			search = (Search)Cache.get("search");
			if(search != null) {
				searchName = Pref.getPrefName(search.tdfkId);
				if(!search.skgnName.equals("例 : 猫山区")) searchName += search.skgnName;
				if(!search.csName.equals("例 : 猫谷町")) searchName += search.csName;
				if(!search.keyword.equals("") && search.keyword != null && !search.keyword.equals("例 : 赤い首輪")) keyword = "キーワード:"+search.keyword;
			}
		}
		String prevPageUrl = "";
		String nextPageUrl = "";
		if(type.equals("search")){
			prevPageUrl = "/result/"+(page - 1)+"?type=search";
			nextPageUrl = "/result/"+(page + 1)+"?type=search";
			search.protect = 0;
			cats = Cat.getFindSearch(search, page);
			pageMax = Cat.getPageMax(search);
			for(int i = 1;i <= pageMax;i++) {
				searchNum += Cat.getFindSearch(search, i).size();
			}
			Cache.set("search", search, 1 * 60 * 60);
			//探されている場合の処理
			if(userAgentS.accessedBySp()) {
				return ok(sp_searchResult.render("result", cats, searchName, keyword, page, pageMax, prevPageUrl, nextPageUrl, searchNum));
			} else {
				return ok(searchResult.render("result", cats, searchName, keyword, page, pageMax, prevPageUrl, nextPageUrl, searchNum));
			}
		}else{
			prevPageUrl = "/result/"+(page - 1)+"?type=protect";
			nextPageUrl = "/result/"+(page + 1)+"?type=protect";
			search.protect = 1;
			cats = Cat.getFindSearch(search, page);
			pageMax = Cat.getPageMax(search);
			for(int i = 1;i <= pageMax;i++) {
				searchNum += Cat.getFindSearch(search, i).size();
			}
			Cache.set("search", search, 1 * 60 * 60);
			//保護されている場合の処理
			if(userAgentS.accessedBySp()) {
				return ok(sp_protectResult.render("result", cats, searchName, keyword, page, pageMax, prevPageUrl, nextPageUrl, searchNum));
			} else {
				return ok(protectResult.render("result", cats, searchName, keyword, page, pageMax, prevPageUrl, nextPageUrl, searchNum));
			}
		}
	}

	public static Result detail(Long id){
		String type = Form.form().bindFromRequest().get("type");
		Cat cat = appS.findCatById(id);
		Form<CommentInfo> form = form(CommentInfo.class);
		if(type.equals("search")){
			//探されている場合の処理
			if(userAgentS.accessedBySp()) {
				return ok(sp_searchDetail.render("detail", cat, form, new Call("POST", "/comment/" + id + "?type=search"), cat.comments));
			} else {
				return ok(searchDetail.render("detail", cat, form, new Call("POST", "/comment/" + id + "?type=search"), cat.comments));
			}
		}else{
			//保護されている場合の処理
			if(userAgentS.accessedBySp()) {
				return ok(sp_protectDetail.render("detail", cat, form, new Call("POST", "/comment/" + id + "?type=protect"), cat.comments));
			} else {
				return ok(protectDetail.render("detail", cat, form, new Call("POST", "/comment/" + id + "?type=protect"), cat.comments));
			}
		}
	}

	public static Result detailOwner(){
		String type = Form.form().bindFromRequest().get("type");
		String uid = Form.form().bindFromRequest().get("uid");
		String hash = Form.form().bindFromRequest().get("token");
		Cat cat = appS.findCatbyToken(hash);
		if(cat == null || !cat.contributor.hashId.equals(uid)){
			return redirect("/");
		}
		if(type.equals("search")){
			if(userAgentS.accessedBySp()) {
				return ok(sp_searchDetailOwner.render("owner", cat, cat.comments));
			} else {
				return ok(searchDetailOwner.render("owner", cat, cat.comments));
			}
		}else{
			if(userAgentS.accessedBySp()) {
				return ok(sp_protectDetailOwner.render("owner", cat, cat.comments));
			} else {
				return ok(protectDetailOwner.render("owner", cat, cat.comments));
			}
		}

	}

	public static Result post() throws Exception{
		String type = Form.form().bindFromRequest().get("type");
		RequiredInfo req = (RequiredInfo)Cache.get("req");
		Form<RequiredInfo> form;
		if(req != null){
			form = form(RequiredInfo.class).fill(req);
		}else{
			form = form(RequiredInfo.class);
		}
		List<Tuple2<String, String>> opts = getPref();
		if(type.equals("search")){
			//探されている場合の処理
			if(userAgentS.accessedBySp()) {
				return ok(sp_searchPost.render("post", form, opts, new Call("POST", "/post/second?type=search")));
			} else {
				return ok(searchPost.render("post", form, opts, new Call("POST", "/post/second?type=search")));
			}
		}else{
			//保護されている場合の処理
			if(userAgentS.accessedBySp()) {
				return ok(sp_protectPost.render("post", form, opts, new Call("POST", "/post/second?type=protect")));
			} else {
				return ok(protectPost.render("post", form, opts, new Call("POST", "/post/second?type=protect")));
			}
		}
	}

	public static Result postSecond() throws Exception{
		MultipartFormData body = request().body().asMultipartFormData();
	    FilePart picture = body.getFile("imagePath");
	    String imgPath = uploadImage(picture);
		String type = Form.form().bindFromRequest().get("type");
		Form<RequiredInfo> createForm = form(RequiredInfo.class).bindFromRequest();
		if (!createForm.hasErrors()) {
			RequiredInfo req = new RequiredInfo();
			req = createForm.get();
			req.imagePath = imgPath;
			Cache.set("req", req, 60 * 5);
			Form<CatInfo> form;
			CatInfo catInfo = (CatInfo)Cache.get("catInfo");
			if(catInfo != null){
				form = form(CatInfo.class).fill(catInfo);
			}else{
				form = form(CatInfo.class);
			}
			if(type.equals("search")){
				//探されている場合の処理
				if(userAgentS.accessedBySp()) {
					return ok(sp_searchPost2.render("post2", form, new Call("POST", "/post/third?type=search")));
				} else {
					return ok(searchPost2.render("post2", form, new Call("POST", "/post/third?type=search")));
				}
			}else{
				//保護されている場合の処理
				if(userAgentS.accessedBySp()) {
					return ok(sp_protectPost2.render("post2", form, new Call("POST", "/post/third?type=protect")));
				} else {
					return ok(protectPost2.render("post2", form, new Call("POST", "/post/third?type=protect")));
				}
			}
		}else{
			List<Tuple2<String, String>> opts = getPref();
			if(type.equals("search")){
				//探されている場合の処理
				if(userAgentS.accessedBySp()) {
					return badRequest(sp_searchPost.render("post", createForm, opts, new Call("POST", "/post/second?type=search")));
				} else {
					return badRequest(searchPost.render("post", createForm, opts, new Call("POST", "/post/second?type=search")));
				}
			}else{
				//保護されている場合の処理
				if(userAgentS.accessedBySp()) {
					return badRequest(sp_protectPost.render("post", createForm, opts, new Call("POST", "/post/second?type=protect")));
				} else {
					return badRequest(protectPost.render("post", createForm, opts, new Call("POST", "/post/second?type=protect")));
				}
			}
		}
	}

	public static Result postThird() throws Exception{
		String type = Form.form().bindFromRequest().get("type");
		Form<CatInfo> createForm = form(CatInfo.class).bindFromRequest();
		if (!createForm.hasErrors()) {
			CatInfo catInfo = new CatInfo();
			catInfo = createForm.get();
			Cache.set("catInfo", catInfo, 60 * 5);
			Form<ContributorInfo> form;
			ContributorInfo cont = (ContributorInfo)Cache.get("cont");
			if(cont != null){
				form = form(ContributorInfo.class).fill(cont);
			}else{
				form = form(ContributorInfo.class);
			}
			if(type.equals("search")){
				//探されている場合の処理
				if(userAgentS.accessedBySp()) {
					return ok(sp_searchPost3.render("post3", form, new Call("POST", "/post/confirm?type=search")));
				} else {
					return ok(searchPost3.render("post3", form, new Call("POST", "/post/confirm?type=search")));
				}
			}else{
				//保護されている場合の処理
				if(userAgentS.accessedBySp()) {
					return ok(sp_protectPost3.render("post3", form, new Call("POST", "/post/confirm?type=protect")));
				} else {
					return ok(protectPost3.render("post3", form, new Call("POST", "/post/confirm?type=protect")));
				}
			}
		}else{
			if(type.equals("search")){
				//探されている場合の処理
				if(userAgentS.accessedBySp()) {
					return badRequest(sp_searchPost2.render("post2", createForm, new Call("POST", "/post/third?type=search")));
				} else {
					return badRequest(searchPost2.render("post2", createForm, new Call("POST", "/post/third?type=search")));
				}
			}else{
				//保護されている場合の処理
				if(userAgentS.accessedBySp()) {
					return badRequest(sp_protectPost2.render("post2", createForm, new Call("POST", "/post/third?type=protect")));
				} else {
					return badRequest(protectPost2.render("post2", createForm, new Call("POST", "/post/third?type=protect")));
				}
			}
		}
	}

	public static Result postConfirm(){
		String type = Form.form().bindFromRequest().get("type");
		Form<ContributorInfo> createForm = form(ContributorInfo.class).bindFromRequest();
		if (!createForm.hasErrors()) {
			ContributorInfo cont = new ContributorInfo();
			cont = createForm.get();
			Form<ContributorInfo> form = appS.checkErrors(cont.birthYear, cont.birthMonth, cont.birthDay, createForm);
			if(form.hasErrors()){
				if(type.equals("search")){
					//探されている場合の処理
					if(userAgentS.accessedBySp()) {
						return badRequest(sp_searchPost3.render("post3", form, new Call("POST", "/post/confirm?type=search")));
					} else {
						return badRequest(searchPost3.render("post3", form, new Call("POST", "/post/confirm?type=search")));
					}
				}else{
					//保護されている場合の処理
					if(userAgentS.accessedBySp()) {
						return badRequest(sp_searchPost3.render("post3", form, new Call("POST", "/post/confirm?type=protect")));
					} else {
						return badRequest(searchPost3.render("post3", form, new Call("POST", "/post/confirm?type=protect")));
					}
				}
			}
			Cache.set("cont", cont, 60 * 5);
			RequiredInfo req = (RequiredInfo)Cache.get("req");
			req.prefName = Pref.find.byId(req.prefId + 1).prefName;
			CatInfo catInfo = (CatInfo)Cache.get("catInfo");
			Cache.set("req", req, 60 * 5);
			Cache.set("catInfo", catInfo, 60 * 5);
			if(type.equals("search")){
				//探されている場合の処理
				if(userAgentS.accessedBySp()) {
					return ok(sp_searchPostConfirm.render("post4", req, catInfo, cont));
				} else {
					return ok(searchPostConfirm.render("post4", req, catInfo, cont));
				}
			}else{
				//保護されている場合の処理
				if(userAgentS.accessedBySp()) {
					return ok(sp_protectPostConfirm.render("post4", req, catInfo, cont));
				} else {
					return ok(protectPostConfirm.render("post4", req, catInfo, cont));
				}
			}
		}else{
			if(type.equals("search")){
				//探されている場合の処理
				if(userAgentS.accessedBySp()) {
					return badRequest(sp_searchPost3.render("post3", createForm, new Call("POST", "/post/confirm?type=search")));
				} else {
					return badRequest(searchPost3.render("post3", createForm, new Call("POST", "/post/confirm?type=search")));
				}
			}else{
				//保護されている場合の処理
				if(userAgentS.accessedBySp()) {
					return badRequest(sp_searchPost3.render("post3", createForm, new Call("POST", "/post/confirm?type=protect")));
				} else {
					return badRequest(searchPost3.render("post3", createForm, new Call("POST", "/post/confirm?type=protect")));
				}
			}
		}
	}

	public static Result postFinish(){
		String type = Form.form().bindFromRequest().get("type");
		RequiredInfo req = (RequiredInfo)Cache.get("req");
		CatInfo catInfo = (CatInfo)Cache.get("catInfo");
		ContributorInfo contInfo = (ContributorInfo)Cache.get("cont");
		AppService appS = new AppService();
		Cat cat = appS.setCat(req, catInfo, contInfo, type);
		postFinishInfo pfi = new postFinishInfo();
		String hash = BCrypt.hashpw(cat.catId + cat.contributor.mailAddress + new Date(), BCrypt.gensalt());
		pfi.url = "http://127.0.0.1:9000/detailOwner?uid=" + cat.contributor.hashId + "&token=" + hash + "&type=" + type;
		cat.token = hash;
		appS.catSave(cat);
		Form<postFinishInfo> form = form(postFinishInfo.class).fill(pfi);

		//投稿時メール配信処理、じゃまなのでコメントアウトしています。
		/*EmailService es = new EmailService();
		es.sendEmail("【ネコサーチ】投稿を受け付けました",
				contInfo.contributorName + "様\n\n"
				+ "ネコサーチへの投稿を受け付けました。\n"
				+ "コメント投稿者の方がメールアドレスを投稿していた場合、下記URLからページにアクセスすることで、記事投稿者であるあなたのみ、そのメールアドレスと閲覧できます。\n"
				+ "また、下記URLからページにアクセスすることで、記事の削除も行えます。\n\n"
				+ pfi.url + "\n\n"
				+ "※当メールは原則として再発行できませんので、大切に保管ください。\n"
				+ "※当メールに覚えのない方は、お手数ですがplay.neko.search.tp031@gmail.comまでご連絡ください。\n\n"
				+ "大切な家族の一員と無事に再開できるよう、心よりお祈りしております。\n"
				+ "【ネコサーチ】",
				contInfo.mailAddress, "play.neko.search.tp031@gmail.com");*/
		if(type.equals("search")){
			//探されている場合の処理
			if(userAgentS.accessedBySp()) {
				return ok(sp_searchPostFinish.render("post finish", form, pfi.url));
			} else {
				return ok(searchPostFinish.render("post finish", form, pfi.url));
			}
		}else{
			//保護されている場合の処理
			if(userAgentS.accessedBySp()) {
				return ok(sp_protectPostFinish.render("post finish", form, pfi.url));
			} else {
				return ok(protectPostFinish.render("post finish", form, pfi.url));
			}
		}
	}

	public static Result postComment(Long id){
		MultipartFormData body = request().body().asMultipartFormData();
	    FilePart picture = body.getFile("imagePath");
	    String imgPath = uploadImage(picture);
	    Form<CommentInfo> createForm = form(CommentInfo.class).bindFromRequest();
	    String type = Form.form().bindFromRequest().get("type");
    	Cat cat = appS.findCatById(id);
	    if(!createForm.hasErrors()){
	    	CommentInfo commentInfo = createForm.get();
	    	commentInfo.imagePath = imgPath;
	    	Comment comment = appS.setComment(commentInfo, cat);
	    	cat.comments.add(comment);
	    	appS.updateCat(cat);
	    	if(type.equals("search")){
	    		return redirect("/detail/" + id + "?type=search");
	    	}else{
	    		return redirect("/detail/" + id + "?type=protect");
	    	}
	    }else{
	    	List<Comment> comments = cat.comments;
	    	if(type.equals("search")){
	    		return badRequest(searchDetail.render("detail", cat, createForm, new Call("POST", "/comment/" + id + "?type=search"), comments));
	    	}else{
	    		return badRequest(protectDetail.render("detail", cat, createForm, new Call("POST", "/comment/" + id + "?type=protect"), comments));
	    	}
	    }
	}

	public static Result about() {
		if(userAgentS.accessedBySp()) {
			return ok(sp_about.render());
		} else {
			return ok(about.render());
		}
	}

	public static Result operation() {
		if(userAgentS.accessedBySp()) {
			return ok(sp_operation.render());
		} else {
			return ok(operation.render());
		}
	}

	public static Result links() {
		if(userAgentS.accessedBySp()) {
			return ok(sp_links.render());
		} else {
			return ok(links.render());
		}
	}

	public static Result shopping() {
		if(userAgentS.accessedBySp()) {
			return ok(sp_shopping.render());
		} else {
			return ok(shopping.render());
		}
	}

	public static Result blogparts() {
		return ok(blogparts.render());
	}

	public static Result apiInfo() {
		return ok(apiInfo.render());
	}

	public static Result kiyaku() {
		if(userAgentS.accessedBySp()) {
			return ok(sp_kiyaku.render());
		} else {
			return ok(kiyaku.render());
		}
	}

	public static Result privacy() {
		if(userAgentS.accessedBySp()) {
			return ok(sp_privacy.render());
		} else {
			return ok(privacy.render());
		}
	}

	public static Result corp() {
		if(userAgentS.accessedBySp()) {
			return ok(sp_corp.render());
		} else {
			return ok(corp.render());
		}
	}

	public static Result contact(){
		Form<ContactInfo> form = form(ContactInfo.class);
		if(userAgentS.accessedBySp()) {
			return ok(sp_contact.render("contact", form));
		} else {
			return ok(contact.render("contact", form));
		}
	}

	public static Result contactFinish() throws EmailException{
		Form<ContactInfo> createForm = form(ContactInfo.class).bindFromRequest();
		ContactInfo contact = new ContactInfo();
		contact = createForm.get();
		EmailService es = new EmailService();
		es.sendEmail("ネコサーチ問い合わせ", contact.contactMessage, "play.neko.search.tp031@gmail.com", contact.contactMail);
		return ok(contactFinish.render("contact"));
	}

	public static Result delete(Long id) {
		appS.deleteCat(id);
		return redirect("/");
	}

	//都道府県をTupleに入れる
	public static List<Tuple2<String, String>> getPref(){
		Form<RequiredInfo> form = form(RequiredInfo.class);
		List<Tuple2<String, String>> opts = new ArrayList<Tuple2<String, String>>();
		AppServiceMock mock = new AppServiceMock();
		List<Pref> prefs = mock.loadPrefs();
		Integer cnt = 0;
		for(Pref pref: prefs) {
			opts.add(new Tuple2(cnt.toString(), pref.prefName));
			cnt ++;
		}
		return opts;
	}

	public static String uploadImage(FilePart pic){
		if (pic != null) {
		     String fileName = pic.getFilename();
		     String contentType = pic.getContentType();
		     File file = pic.getFile();
		     if (contentType.equals("image/jpeg") || contentType.equals("image/jpg") || contentType.equals("image/png")) {
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
