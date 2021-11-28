package ch.droduit.quickchat.helper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * <b>Helper class for managing HTTP cookies.</b>
 *
 * @see HttpServletRequest
 */
public class CookieHelper {

    public static String extractCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName))
                return cookie.getValue();
        }
        return null;
    }

    public static Cookie createCookie(String name, String value, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(60 * 60 * 24 * 365);
        cookie.setPath(path);
        return cookie;
    }

}
