package com.call_bridging.calling;

import android.webkit.URLUtil;

public class OpenTokConfig {
    // *** Fill the following variables using your own Project info from the OpenTok dashboard  ***
    // ***                      https://dashboard.tokbox.com/projects                           ***

    // Replace with your OpenTok API key
//    "ab9a016f18b33c3a0778a7a8cd6af843b653f6eb"  secret
//    API KEY: 46709902
//    SECRET: 780a967af05286b38beb759859533401cb0efb29

    public static final String API_KEY = "46709902";
    // Replace with a generated Session ID
    public static final String SESSION_ID = "2_MX40NjcwOTkwMn5-MTU4ODMxMTg1NzA5NX5XYWt0NkhHZnhyeGh3Wks0OENmUi90QmJ-fg";
    // Replace with a generated token (from the dashboard or using an OpenTok server SDK)
    public static final String TOKEN = "T1==cGFydG5lcl9pZD00NjcwOTkwMiZzaWc9OTZmNzc2YjhjOGMwMGJjOWUzM2UzM2UyYzQ4OWVkY2I1Yzg5YmI5ODpzZXNzaW9uX2lkPTJfTVg0ME5qY3dPVGt3TW41LU1UVTRPRE14TVRnMU56QTVOWDVYWVd0ME5raEhabmh5ZUdoM1drczBPRU5tVWk5MFFtSi1mZyZjcmVhdGVfdGltZT0xNTg4MzExOTE2Jm5vbmNlPTAuMzcyNTI2MDE2NzUxOTk2NiZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTkwOTAzOTE1JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    /*                           ***** OPTIONAL *****
     If you have set up a server to provide session information replace the null value
     in CHAT_SERVER_URL with it.

     For example: "https://yoursubdomain.com"
    */
    public static final String CHAT_SERVER_URL = null;
    public static final String SESSION_INFO_ENDPOINT = CHAT_SERVER_URL + "/session";


    // *** The code below is to validate this configuration file. You do not need to modify it  ***

    public static String webServerConfigErrorMessage;
    public static String hardCodedConfigErrorMessage;

    public static boolean areHardCodedConfigsValid() {
        if (OpenTokConfig.API_KEY != null && !OpenTokConfig.API_KEY.isEmpty()
                && OpenTokConfig.SESSION_ID != null && !OpenTokConfig.SESSION_ID.isEmpty()
                && OpenTokConfig.TOKEN != null && !OpenTokConfig.TOKEN.isEmpty()) {
            return true;
        } else {
            hardCodedConfigErrorMessage = "API KEY, SESSION ID and TOKEN in OpenTokConfig.java cannot be null or empty.";
            return false;
        }
    }

    public static boolean isWebServerConfigUrlValid() {
        if (OpenTokConfig.CHAT_SERVER_URL == null || OpenTokConfig.CHAT_SERVER_URL.isEmpty()) {
            webServerConfigErrorMessage = "CHAT_SERVER_URL in OpenTokConfig.java must not be null or empty";
            return false;
        } else if (!(URLUtil.isHttpsUrl(OpenTokConfig.CHAT_SERVER_URL) || URLUtil.isHttpUrl(OpenTokConfig.CHAT_SERVER_URL))) {
            webServerConfigErrorMessage = "CHAT_SERVER_URL in OpenTokConfig.java must be specified as either http or https";
            return false;
        } else if (!URLUtil.isValidUrl(OpenTokConfig.CHAT_SERVER_URL)) {
            webServerConfigErrorMessage = "CHAT_SERVER_URL in OpenTokConfig.java is not a valid URL";
            return false;
        } else {
            return true;
        }
    }
}
