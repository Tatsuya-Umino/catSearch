package services.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.avaje.ebean.Page;

import dto.PagingDto;
import models.Cat;
import models.Comment;
import models.Contributor;
import models.HowToArrive;
import models.Pref;
import services.AppService;
import services.PrefService;

public class AppServiceMock extends AppService {
	private List<Cat> cats = new ArrayList<Cat>();
	private List<Pref> prefs = new ArrayList<Pref>();
	private List<HowToArrive> howToArrives = new ArrayList<HowToArrive>();

	public AppServiceMock() {
		for(int i = 1; i <= 32; i++) {
			Cat cat = new Cat();
			cat.catId = (long) i;
			cat.pref = new Pref();
			cat.pref.prefId = 1L;
			cat.pref.prefName = "東京都";
			cat.cityName = "港区";
			cat.address = "1-15-12 ほげマンション エントランス周辺";
			cat.catName = "みけ No." + i;
			cat.flg = 0;
			cat.bodyColor = "黒とグレーのボーダー";
			cat.eyesColor = "薄い水色";
			cat.standoutFeature = "目が水色で白い毛。尻尾は短くこげ茶色のしま模様";
			cat.missingDate = "2016年5月17日22時くらい";
			cat.remarks = "鼻と肉球が黒く、オスで去勢手術済みです。臆病、慎重な性格です。";
			cat.message = "どんなに些細な事でもかまいません。もしかしたら…、あれ…？と思ったら連絡をいただけると助かります。大事な家族なので諦めたくありません。絶対に帰ってくると信じています。";
			cat.contributor = new Contributor();
			cat.contributor.contributorId = 1L;
			cat.contributor.contributorName = "山田";
			/*Comment c1 = new Comment();
			c1.commentTitle = "似たような猫を発見しました。";
			c1.commentName = "hoge";
			c1.commentMessage = "東京メトロ表参道駅周辺で似たような特徴の猫を発見しました。";
			cat.comments.add(c1);
			cat.comments.add(c1);*/
			this.cats.add(cat);
		}

		List<String> dummy = Arrays.asList(
				"東京都",
				"埼玉県"
				);
		for(int i = 1; i <= dummy.size(); i++) {
			Pref pref = new Pref();
			pref.prefId = (long) i;
			pref.prefName = dummy.get(i - 1);
			this.prefs.add(pref);
		}

		List<String> hahaha = Arrays.asList(
				"検索サイト",
				"ブログ",
				"バナー広告"
				);
		for(int i = 1; i <= hahaha.size(); i++) {
			HowToArrive hta = new HowToArrive();
			hta.howToArriveId = (long) i;
			hta.howToArriveName = hahaha.get(i - 1);
			this.howToArrives.add(hta);
		}
	}
	/**
	 *
	 */
	@Override
	public List<HowToArrive> loadHowToArrives() {
		return this.howToArrives;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public List<Pref> loadPrefs() {
		return this.prefs;
	}

	/**
	 *
	 */
	@Override
	public PagingDto findCatsWithPages(int page,int itemPerPage) {
		PagingDto pagingDto = new PagingDto();
		pagingDto.totalPageCount = this.cats.size() / itemPerPage;
		int startIndex = (page - 1) * itemPerPage;
		int endIndex = (startIndex + itemPerPage) - 1;
		for(int j = startIndex; j <= endIndex; j++) {
			if(j > this.cats.size() - 1) break;
			pagingDto.cats.add(this.cats.get(j));
		}
		return pagingDto;
	}

	@Override
	public Cat findCatById(Long catId) {
		if(catId < this.cats.size()) {
			return this.cats.get(Integer.parseInt(String.valueOf(catId)) - 1);
		}	else	{
			return null;
		}
	}

	@Override
	public List<Cat> findCatsByFlgAndPrefName(String type,String prefName) {
		return this.cats;
	}

	public List<Cat> getCats() {
		return this.cats;
	}

	public void saveCats() {
		for(int i = 1; i <= 32; i++) {
			Cat cat = new Cat();
			//cat.catId = (long) i;
			cat.cityName = "港区";
			cat.address = "1-15-12 ほげマンション エントランス周辺";
			cat.catName = "みけ No." + i;
			cat.flg = 0;
			cat.bodyColor = "黒とグレーのボーダー";
			cat.eyesColor = "薄い水色";
			cat.standoutFeature = "目が水色で白い毛。尻尾は短くこげ茶色のしま模様";
			cat.missingDate = "2016年5月17日22時くらい";
			cat.remarks = "鼻と肉球が黒く、オスで去勢手術済みです。臆病、慎重な性格です。";
			cat.message = "どんなに些細な事でもかまいません。もしかしたら…、あれ…？と思ったら連絡をいただけると助かります。大事な家族なので諦めたくありません。絶対に帰ってくると信じています。";
			cat.catImage = "/assets/images/units/sample.jpg";
			cat.flg = i % 2;
			cat.pref = new Pref();
			cat.pref.prefName = "東京都";
			PrefService prefService = new PrefService();
			cat.pref.prefId = prefService.getPrefId(cat.pref.prefName);
			//cat.prefId = cat.pref.prefId;
			cat.contributor = new Contributor();
			//cat.contributor.contributorId = 1L;
			cat.contributor.contributorName = "山田";
			/*Comment c1 = new Comment();
			c1.commentTitle = "似たような猫を発見しました。";
			c1.commentName = "hoge";
			c1.commentMessage = "東京メトロ表参道駅周辺で似たような特徴の猫を発見しました。";
			cat.comments.add(c1);*/

			cat.save();
		}

		Cat cat = new Cat();
		cat.cityName = "新宿区";
		cat.address = "新宿";
		cat.catName = "みけ No." + 33;
		cat.flg = 0;
		cat.bodyColor = "黒とボーダー";
		cat.eyesColor = "薄色";
		cat.standoutFeature = "目が白い毛のしま模様";
		cat.missingDate = "1111年1月1日22時くらい";
		cat.remarks = "鼻が黒く、オスで、臆病、慎重な性格です。";
		cat.message = "…、…？";
		cat.catImage = "/assets/images/units/sample.jpg";
		cat.flg = 33 % 2;
		cat.pref = new Pref();
		cat.pref.prefName = "東京都";
		PrefService prefService = new PrefService();
		cat.pref.prefId = prefService.getPrefId(cat.pref.prefName);
		cat.contributor = new Contributor();
		cat.contributor.contributorName = "アホだ";

		cat.save();
	}

	public void savePref() {
		for(Pref p: prefs) {
			Pref pref = new Pref();
			pref.prefName = p.prefName;
			pref.save();
		}
	}
}