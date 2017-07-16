package com.oracle.schoolcircle.http.constants;

/**
 * 后台服务器接口
 * Created by 田帅 on 2017/1/3.
 */

public class URLConstants {
    /**
     * 基础url
     */
//    public static String BASE_URL = "http://192.168.1.105:8080/SchoolCircle/";
//    public static String BASE_URL = "http://172.20.10.7:8080/SchoolCircle/";
//    public static String BASE_URL = "http://172.28.79.18:8080/SchoolCircle/";
    public static String BASE_URL = "http://192.168.1.107:8080/SchoolCircle/";
    /**
     * 登陆接口
     */
    public static String LOGIN_URL = "LoginServlet";
    /**
     * 注册接口
     */
    public static String REGISTER_URL = "RegisterServlet";
    /**
     * 首页新闻接口
     */
    public static String NEWS_URL = "SNewsServlet";
    /**
     * 内容类型接口
     */
    public static String CONTENT_TYPE_URL = "SCOTypeServlet";
    /**
     * 内容接口
     */
    public static String CONTENT_URL = "SContentServlet";
    /**
     * 发布内容接口
     */
    public static String ADD_CONTENT_URL = "ContentsServlet";
    /**
     * 小纸条界面接口
     */
    public static String MESSAGE_CONTACT_URL = "SAllFriendChatLastServlet";
    /**
     * 聊天记录接口
     */
    public static String CHAT_URL = "GetChattomgRecordsServlet";
    /**
     * 发送聊天内容接口
     */
    public static String SEND_CHAT_URL = "MsgChatServlet";
    /**
     * 发送聊天语音接口
     */
    public static String SEND_VOICE_CHAT_URL = "MsgVoiceChatServlet";
    /**
     * 请求推荐好友接口
     */
    public static String FRIEND_URL = "AddFriendServlet";
    /**
     * 添加好友接口
     */
    public static String ADD_FRIEND_URL = "AddFriendServlet";
    /**
     * 请求用户信息接口
     */
    public static String USER_INFO_URL = "UserServlet";
    /**
     * 上传用户头像信息接口
     */
    public static String USER_UPDATE_IMG_URL = "UserImgServlet";
    /**
     * 上传用户姓名等信息接口
     */
    public static String USER_UPDATE_INFO_URL = "UserInfoServlet";
    /**
     * 上传内容接口
     */
    public static String CONTENT_SEND_URL = "ContentsServlet";
    /**
     * 评论接口
     */
    public static String CONTENT_COMMENT_URL = "ContentCommentServlet";
}
