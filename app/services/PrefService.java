package services;

import java.util.ArrayList;
import java.util.List;

import com.avaje.ebean.Query;

import scala.Tuple2;

import models.Pref;


public class PrefService {
	/**
	 * @return
	 */
	public List<Tuple2<String, String>> loadPrefsAsTuple() {
		List<Tuple2<String, String>> opts = new ArrayList<Tuple2<String, String>>();
		opts.add(new Tuple2("1" , "東京都"));
		opts.add(new Tuple2("2" , "埼玉県"));
		return opts;
	}

	/**
	* @param prefName
	* @return
	*/
	public Long getPrefId(String prefName) {
		Query<Pref> query = Pref.find.where("prefName = '" + prefName + "'");
		//Query<Pref> query = Pref.find.where("prefName = '東京都'");
		Pref pref = query.findUnique();
		return pref.prefId;
	}
}
