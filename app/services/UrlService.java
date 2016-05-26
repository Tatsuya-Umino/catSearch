package services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class UrlService {
	/**
	 *
	 * @param params
	 * @return
	 */
	public String buildQuery(Map<String,String> params) {
		String queryString = "";
		for(String name : params.keySet()) {
			if(!queryString.equals("")) {
				queryString += "&";
			}
			try {
				queryString += name + "=" + URLEncoder.encode(params.get(name),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "";
			}
		}
		return queryString;
	}
}
