package forms;

import java.io.File;

import models.Cat;
import play.Play;
import play.data.validation.Constraints.*;

public class EditCat {
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

	/**
	 * 画像パス
	 */
	public String catImage;

	/**
	 * 画像の削除フラグ
	 */
	public Boolean deletePic;

	/**
	 *
	 * @param cat
	 * @return
	 */
	public Cat updateCat(Cat cat) {
		cat.catName = this.catName;
		cat.cityName = this.cityName;
		cat.address = this.address;
		cat.lastAddress = this.lastAddress;
		cat.microNum = this.microNum;
		cat.standoutFeature = this.standoutFeature;
		cat.bodyColor = this.bodyColor;
		cat.eyesColor = this.eyesColor;
		cat.bodySize = this.bodySize;
		cat.missingDate = this.missingDate;
		cat.message = this.message;
		cat.remarks = this.remarks;
		if(this.deletePic) {
			new File(Play.application().path().getPath() + cat.catImage).delete();
			cat.catImage = null;
		}
		return cat;
	}

	/**
	 *
	 */
	public EditCat() {}

	/**
	 *
	 * @param cat
	 */
	public EditCat(Cat cat) {
		this.catId = cat.catId;
		this.catName = cat.catName;
		this.cityName = cat.cityName;
		this.address = cat.address;
		this.lastAddress = cat.lastAddress;
		this.microNum = cat.microNum;
		this.standoutFeature = cat.standoutFeature;
		this.bodyColor = cat.bodyColor;
		this.eyesColor = cat.eyesColor;
		this.bodySize = cat.bodySize;
		this.missingDate = cat.missingDate;
		this.message = cat.message;
		this.remarks = cat.remarks;
		this.catImage = cat.catImage;
		this.deletePic = false;
	}
}