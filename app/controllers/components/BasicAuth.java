package controllers.components;

import play.*;
import play.libs.F.Promise;
import play.mvc.*;
import play.mvc.Http.*;

/**
 * Basic認証を実装したクラス
 * ベーシック認証をかけたいactionに
 * 「@With(BasicAuth.class)」
 * アノテーションをつけると、Basic認証機能をつけることができる。
 *
 * @author masataka.okudera
 *
 */
public class BasicAuth extends Action.Simple {

    private static final String AUTHORIZATION = "authorization";
    private static final String WWW_AUTHENTICATE = "WWW-Authenticate";
    private static final String REALM = "Basic realm=\"Enter your login ID and password, please.\"";

    private static final String adminUser = "admin";
    private static final String adminPassword = "password";

    public Promise<SimpleResult> call(Http.Context ctx) throws Throwable {
        String authHeader = ctx.request().getHeader(AUTHORIZATION);
        if (authHeader == null) {
            ctx.response().setHeader(WWW_AUTHENTICATE, REALM);
            return Promise.pure((SimpleResult) delegate.unauthorized());
        }
        String auth = authHeader.substring(6);
        byte[] decodedAuth = new sun.misc.BASE64Decoder().decodeBuffer(auth);
        String[] credString = new String(decodedAuth, "UTF-8").split(":");
        if (credString == null || credString.length != 2) {
        }
        String username = credString[0];
        String password = credString[1];
        if (username.equals(adminUser) && password.equals(adminPassword)) {
            return delegate.call(ctx);
        } else {
        	ctx.response().setHeader(WWW_AUTHENTICATE, REALM);
        	return Promise.pure((SimpleResult) delegate.unauthorized());
        }
    }
}