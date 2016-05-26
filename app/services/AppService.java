package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.avaje.ebean.Page;

import dto.PagingDto;
import models.*;
import play.data.Form;
import play.data.validation.ValidationError;
import forms.*;
import com.avaje.ebean.Query;

import static play.data.Form.form;

import java.sql.*;

public class AppService {
	/**
	 *
	 * @return
	 */
	public List<HowToArrive> loadHowToArrives() {
		List<HowToArrive> howToArrives = HowToArrive.find.all();
		return howToArrives;
	}

	/**
	 *
	 * @return
	 */
	public List<Pref> loadPrefs() {
		List<Pref> prefs = Pref.find.all();
		return prefs;
	}

	/**
	 *
	 * @param page
	 * @return
	 */
	public PagingDto findCatsWithPages(int page,int itemPerPage) {
		Page<Cat> catsPage = Cat.find.orderBy("createTs desc")
				.findPagingList(itemPerPage).getPage(page);
		PagingDto pagingDto = new PagingDto();
		pagingDto.totalPageCount = catsPage.getTotalPageCount();
		pagingDto.cats = catsPage.getList();
		return pagingDto;
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

	public void catSave(Cat cat){
		cat.save();
	}

	public void commentSave(Comment com){
		com.save();
	}

	/**
	 *
	 * @param catId
	 */
	public void deleteCat(Long catId) {
		System.out.println("in deleteCat");
		Cat cat = Cat.find.byId(catId);
		if(cat != null) {
			cat.delete();
			System.out.println("delete");
		}
	}

	/**
	 *
	 * @param type
	 * @param prefName
	 * @return
	 */
	public List<Cat> findCatsByFlgAndPrefName(String type,String prefName) {
		Integer flg = null;
		if(type.equals("search")) {
			flg = 0;
		}	else	{
			flg = 1;
		}
		List<Cat> cats = Cat.find.where().eq("pref.prefName", prefName)
				.where().eq("flg", flg).findList();
		return cats;
	}

	public Cat findCatbyToken(String token){
		Cat cat = Cat.find.where("token = '" + token + "'").findUnique();
		return cat;
	}

	public Cat setCat(RequiredInfo req, CatInfo catInfo, ContributorInfo contInfo, String type){
		Cat cat = new Cat();
		Pref pref = Pref.find.byId(req.prefId + 1);
		//テスト用


		Contributor cont = new Contributor();
		cont.contributorName = contInfo.contributorName;
		cont.contributorNickName = contInfo.contributorNickName;
		cont.birthYear = contInfo.birthYear;
		cont.birthMonth = contInfo.birthMonth;
		cont.birthYear = contInfo.birthDay;
		cont.mailAddress = contInfo.mailAddress;
		cont.sex = contInfo.sex;
		cont.howToArriveId = contInfo.howToArriveId;
		cont.hashId = BCrypt.hashpw(cont.contributorName + cont.mailAddress + new java.util.Date(), BCrypt.gensalt());

		if(type.equals("search")){
			cat.flg = 0;
		}else{
			cat.flg = 1;
		}
		cat.catName = req.catName;
		//cat.prefId = req.prefName;
		cat.cityName = req.cityName;
		cat.address = req.address;
		cat.lastAddress = req.lastAddress;
		cat.microNum = req.microNum;
		cat.standoutFeature = req.standoutFeature;
		cat.bodyColor = catInfo.bodyColor;
		cat.eyesColor = catInfo.eyesColor;
		cat.bodySize = catInfo.bodySize;
		cat.missingDate = catInfo.protectionDate;
		cat.message = req.message;
		cat.remarks = catInfo.remarks;
		if(req.imagePath == null){
			cat.catImage = "/assets/images/units/nophoto.jpg";
		}else{
			cat.catImage = req.imagePath;
		}
		cat.pref = pref;
		cat.contributor = cont;
		return cat;
	}

	public Comment setComment(CommentInfo com, Cat cat){
		Comment comment = new Comment();
    	comment.cat = cat;
    	comment.commentTitle = com.title;
    	comment.commentName = com.commenterName;
    	comment.commentImage = com.imagePath;
    	comment.commentMessage = com.comment;
    	comment.createTs = new Date(new java.util.Date().getTime());
    	return comment;
	}

	public boolean updateCat(Cat cat){
		cat.update();
		return true;
	}

	public boolean checkYear(String year){
		return Integer.parseInt(year) > Calendar.getInstance().get(Calendar.YEAR) || Integer.parseInt(year) < 1900;
	}

	public boolean checkDay(String year, String month, String date){
		Calendar c = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);
		int days=c.getActualMaximum(Calendar.DAY_OF_MONTH);
		return Integer.parseInt(date) > days || Integer.parseInt(date) < 1;
	}

	public boolean checkMonth(String month){
		return Integer.parseInt(month) > 13 || Integer.parseInt(month) < 1;
	}

	public Form<ContributorInfo> checkErrors(String year, String month, String date, Form<ContributorInfo> form){
		List<ValidationError> errors = new ArrayList<ValidationError>();
		if(checkYear(year)){
			errors.add(new ValidationError("birthYear", "値を正しく入力してください。"));
			form.errors().put("birthYear", errors);
			if(checkMonth(month)){
				form.errors().put("birthMonth", errors);
				if(checkDay(year, month, date)){
					form.errors().put("birthDay", errors);
				}
			}
		}else if(checkMonth(month)){
			errors.add(new ValidationError("birthMonth", "値を正しく入力してください。"));
			form.errors().put("birthMonth", errors);
			if(checkDay(year, month, date)){
				form.errors().put("birthDay", errors);
			}
		}else if(checkDay(year, month, date)){
			errors.add(new ValidationError("birthDay", "値を正しく入力してください。"));
			form.errors().put("birthDay", errors);
		}
		return form;
	}
}