package utils;

import java.util.List;

import play.libs.F.Option;

public class OptionUtil {
	public static <A> Option<List<A>> apply(List<A> value) {
        if(value != null && value.size() != 0) {
            return Option.Some(value);
        } else {
            return Option.None();
        }
    }

	public static <A> Option<A> apply(A value) {
        if(value != null) {
            return Option.Some(value);
        } else {
            return Option.None();
        }
    }
}
