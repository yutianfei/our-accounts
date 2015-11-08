package com.wangsy.ouraccounts.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * 网络请求/响应参数
 * <p/>
 * Created by wangsy on 15/11/7.
 */
public class HttpParams {

    /**
     * 检查更新
     * 响应结果：{ versionCode : int, versionName : "", downloadUrl:"" }
     */

    /**
     * 登录参数
     * <p/>
     * 响应结果：0:用户名不存在，1:密码错误，2:登录成功
     */
    public static Map<String, String> loginParams(String username, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        return params;
    }

    /**
     * 注册参数
     * <p/>
     * 响应结果：0:用户名存在，1:注册成功
     */
    public static Map<String, String> registerParams(String username, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        return params;
    }

    /**
     * 修改密码参数
     * <p/>
     * 响应结果：0:旧密码错误，1:修改成功
     */
    public static Map<String, String> changePasswordParams(String username, String oldPassword, String newPassword) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("oldPassword", oldPassword);
        params.put("newPassword", newPassword);
        return params;
    }

    /**
     * 信息反馈参数
     * <p/>
     * 响应结果：0:反馈失败，1:反馈成功
     */
    public static Map<String, String> feedbackParams(String feedbackContent, String feedbackAddress) {
        Map<String, String> params = new HashMap<>();
        params.put("feedbackContent", feedbackContent);
        params.put("feedbackAddress", feedbackAddress);
        return params;
    }
}
