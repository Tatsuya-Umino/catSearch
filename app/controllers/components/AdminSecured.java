package controllers.components;

import play.mvc.Http.Context;
import services.SessionNames;
import play.mvc.Result;
import play.mvc.Security;

/**
 * セキュリティ認証クラス
 * @author masataka.okudera
 *
 */
public class AdminSecured extends Security.Authenticator {
    /**
     * ユーザ名取得
     * Authenticatorの仕組みとして、デフォルトusernameがあるかどうかで判断してログイン状態か判断している。
     * usernameは、何らかのString型が返ることでユーザー名を持ったアカウントがログインしているとみなしている。
     */
	@Override
    public String getUsername(Context ctx) {
        return ctx.session().get(SessionNames.ADMIN_USER_NAME);
    }

	/**
	 * 非認証時のアクション
	 * ログイン済でなかった場合に戻されるリダイレクト先を指定している
	 * どのURLからリクエストされているかをセッションに入れておくことで
	 * ログインOK時、当初アクセスする予定だったページにリダイレクトできる
	 */
    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(controllers.routes.AdminController.login());
    }
}