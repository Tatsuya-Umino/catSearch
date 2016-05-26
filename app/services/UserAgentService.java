package services;

import eu.bitwalker.useragentutils.*;

import play.mvc.Http.Request;

public class UserAgentService {
	public boolean accessedBySp() {
		Request req = play.mvc.Http.Context.current().request();
		String userAgentStr = req.getHeader("User-Agent");
		OperatingSystem os = OperatingSystem.parseUserAgentString(userAgentStr);
		if(os.getDeviceType().equals(DeviceType.COMPUTER)) {
				return false;
		}	else	{
			return true;
		}
	}
}
