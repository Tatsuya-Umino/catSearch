package controllers;

import java.util.List;
import java.util.Map;

import models.Cat;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.AppService;

public class ApiController extends Controller {

	private static AppService appService = new AppService();

	public static Result index() {
		Map<String, String[]> params = request().queryString();
		try {
			String type = params.get("type")[0];
			String prefName = params.get("area")[0];
			List<Cat> cats = appService.findCatsByFlgAndPrefName(type, prefName);
			return ok(Json.toJson(cats));
		} catch(Exception e) {
			e.printStackTrace();
			return badRequest("invalid parameters");
		}
	}
}