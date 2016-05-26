package models;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.avaje.ebean.Query;

import javax.persistence.JoinColumn;

import play.data.validation.Constraints.*;
import play.db.ebean.Model.Finder;

import services.PrefService;
import models.Pref;

@Entity
@Table(name="Cats")
public class Cat extends BaseModel {
	private final static int LIMIT = 6;
	@Id
	@Required
	public Long catId;

	@Required
	public String catName;

	@Required
	public String cityName;

	public String address;

	public String lastAddress;

	/**
	 * マイクロチップ識別番号
	 */
	public Long microNum;

	/**
	 * 保護した猫の一番の特徴詳
	 */
	public String standoutFeature;

	/**
	 * 保護した猫の体毛色
	 */
	public String bodyColor;

	/**
	 * 保護した猫の眼の色
	 */
	public String eyesColor;

	/**
	 * 保護した猫の身体の大きさ
	 */
	public String bodySize;

	/**
	 * 迷子になった日時
	 */
	public String missingDate;

	/**
	 * 閲覧者へ向けてのメッセージ
	 */
	public String message;

	/**
	 * その他の特徴・備考
	 */
	public String remarks;

	public Long latitude;

	public Long longitude;

	public String catImage;

	/**
	 * ネコが探されているか保護されているか
	 */
	public Integer flg;

	@OneToMany(cascade = CascadeType.ALL)
	public List<Comment> comments;

	@ManyToOne
	//@JoinColumn(name="prefId")
	public Pref pref;

	public String token;

	@ManyToOne(cascade=CascadeType.ALL)
	public Contributor contributor;

	public static Finder<Long, Cat> find =
			new Finder<Long, Cat>(Long.class, Cat.class);

	public String getMissingDate() {
		String date = this.missingDate;
		date = date.replaceAll("年", ".");
		date = date.replaceAll("月", "/");
		String ans[] = date.split("日");
		return ans[0];
	}

	// 検索条件にあったList<Cat>を取得
	public static List<Cat> getFindSearch(Search search,Integer page) {
		Integer pageNum = (page - 1 < 0) ? 0 : page - 1;
		boolean city = true;
		boolean town = true;
		boolean keyword = true;

		if(search.skgnName.equals("") || search.skgnName == null || search.skgnName.equals("例 : 猫山区")) city = false;
		if(search.csName.equals("") || search.csName == null || search.csName.equals("例 : 猫谷町")) town = false;
		if(search.keyword.equals("") || search.keyword == null || search.keyword.equals("例 : 赤い首輪")) keyword = false;

		List<Cat> cats = new ArrayList<Cat>();
		PrefService prefService = new PrefService();
		Long prefId = prefService.getPrefId(Pref.getPrefName(search.tdfkId));

		Query<Cat> query;
		String where = "pref.prefId = '"+ prefId +"'";
		if(search.protect != null) where += " AND flg = '"+ search.protect +"'";
		if(city) {
			where += " AND cityName LIKE '%"+ search.skgnName +"%'";
			where += " AND address LIKE '%"+ search.skgnName +"%'";
		}
		if(town) where += " AND address LIKE '%"+ search.csName +"%'";
		if(keyword) {
			where += " AND bodyColor LIKE '%"+ search.keyword +"%'";
			where += " AND eyesColor LIKE '%"+ search.keyword +"%'";
			where += " AND standoutFeature LIKE '%"+ search.keyword +"%'";
			where += " AND remarks LIKE '%"+ search.keyword +"%'";
		}

		query = Cat.find.where(where);
		cats = query.findPagingList(LIMIT).getPage(pageNum).getList();
		return cats;
	}

	// 検索条件にあったpageMaxを取得
	public static Integer getPageMax(Search search) {
		boolean city = true;
		boolean town = true;
		boolean keyword = true;

		if(search.skgnName.equals("") || search.skgnName == null || search.skgnName.equals("例 : 猫山区")) city = false;
		if(search.csName.equals("") || search.csName == null || search.csName.equals("例 : 猫谷町")) town = false;
		if(search.keyword.equals("") || search.keyword == null || search.keyword.equals("例 : 赤い首輪")) keyword = false;

		List<Cat> cats = new ArrayList<Cat>();
		PrefService prefService = new PrefService();
		Long prefId = prefService.getPrefId(Pref.getPrefName(search.tdfkId));

		String where = "pref.prefId = '"+ prefId +"'";
		if(search.protect != null) where += " AND flg = '"+ search.protect +"'";
		if(city) {
			where += " AND cityName LIKE '%"+ search.skgnName +"%'";
			where += " AND address LIKE '%"+ search.skgnName +"%'";
		}
		if(town) where += " AND address LIKE '%"+ search.csName +"%'";
		if(keyword) {
			where += " AND bodyColor LIKE '%"+ search.keyword +"%'";
			where += " AND eyesColor LIKE '%"+ search.keyword +"%'";
			where += " AND standoutFeature LIKE '%"+ search.keyword +"%'";
			where += " AND remarks LIKE '%"+ search.keyword +"%'";
		}

		return Cat.find.where(where).findPagingList(LIMIT).getTotalPageCount();
	}
}
