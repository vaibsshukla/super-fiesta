package com.call_bridging.calling;

import android.webkit.URLUtil;

public class OpenTokConfig {
    // *** Fill the following variables using your own Project info from the OpenTok dashboard  ***
    // ***                      https://dashboard.tokbox.com/projects                           ***

    // Replace with your OpenTok API key
//    "ab9a016f18b33c3a0778a7a8cd6af843b653f6eb"  secret
//    API KEY: 46709902
//    SECRET: 780a967af05286b38beb759859533401cb0efb29
    public final static String CHANNEL_ID = "BasicFirst";
    public static final String API_KEY = "46706982";
    // Replace with a generated Session ID
    public static String SESSION_ID = "1_MX40NjcwNjk4Mn5-MTU5NDAyNzAxMzU2Mn5WOXBReWFhT0ZTOXBPZWUwZmpSOXZlcXF-fg";
    // Replace with a generated token (from the dashboard or using an OpenTok server SDK)
    public static String TOKEN = "T1==cGFydG5lcl9pZD00NjcwNjk4MiZzaWc9MGU1ZjgzM2U4ODM1NDE2MjczOGE0YWMyZWRhZjhmYzE3YjA0ODI4YTpzZXNzaW9uX2lkPTFfTVg0ME5qY3dOams0TW41LU1UVTVOREF5TnpBeE16VTJNbjVXT1hCUmVXRmhUMFpUT1hCUFpXVXdabXBTT1habGNYRi1mZyZjcmVhdGVfdGltZT0xNTk0MDI3MDEzJnJvbGU9bW9kZXJhdG9yJm5vbmNlPTE1OTQwMjcwMTMuNjAyMzIwMTczNzkzODU=";
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

    public static class ConstantStrings {
        public static String IS_DECLINED = "is_declined";
        public static String VIDEO_CALL = "videocall";
        public static String TYPE = "type";
        public static String SESSION_ID = "session_id";
        public static String TOKEN = "token";
        public final static String DECLINE = "decline";
        public final static String STOP = "Stop";
        public final static String CANCEL = "cancel";
        public final static String STOP_DECLINE = "Stop_decline";
        public final static String IS_DROPPED = "is_dropped";
        public final static String ANSWER = "answer";
        public final static String NOT_ANSWERED = "not_answered";
        public final static String CALL_STATUS = "call_status";
        public final static String CALLING_EVENT = "calling_event";





    }
}
