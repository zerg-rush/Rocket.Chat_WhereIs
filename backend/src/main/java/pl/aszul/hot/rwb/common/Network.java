package pl.aszul.hot.rwb.common;

import javax.servlet.http.HttpServletRequest;

public class Network {

    public static String getClientIP(final HttpServletRequest request) {
        var xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    public static String getClientDetails(HttpServletRequest request) {
        return "(IP: " + getClientIP(request) + ", User Agent: " + request.getHeader("User-Agent") + ")";
    }

    public static String getClientDetails(HttpServletRequest request, String username) {
        return "(username: " + username + ", IP: " + getClientIP(request) +
                ", User Agent: " + request.getHeader("User-Agent") + ")";
    }

}
