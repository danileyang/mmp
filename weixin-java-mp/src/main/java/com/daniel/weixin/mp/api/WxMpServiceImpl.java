package com.daniel.weixin.mp.api;

import com.daniel.weixin.common.bean.WxAccessToken;
import com.daniel.weixin.common.bean.WxJsapiSignature;
import com.daniel.weixin.common.bean.WxMenu;
import com.daniel.weixin.common.bean.result.WxError;
import com.daniel.weixin.common.bean.result.WxMediaUploadResult;
import com.daniel.weixin.common.exception.WxErrorException;
import com.daniel.weixin.common.session.StandardSessionManager;
import com.daniel.weixin.common.session.WxSessionManager;
import com.daniel.weixin.common.util.RandomUtils;
import com.daniel.weixin.common.util.StringUtils;
import com.daniel.weixin.common.util.crypto.SHA1;
import com.daniel.weixin.common.util.fs.FileUtils;
import com.daniel.weixin.common.util.http.*;
import com.daniel.weixin.common.util.json.GsonHelper;
import com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import com.daniel.weixin.mp.bean.result.WxMpSemanticQueryResult;
import com.daniel.weixin.mp.bean.result.WxMpUser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class WxMpServiceImpl implements com.daniel.weixin.mp.api.WxMpService {

    protected final Logger log = LoggerFactory.getLogger(WxMpServiceImpl.class);

    /**
     * 全局的是否正在刷新access token的锁
     */
    protected final Object globalAccessTokenRefreshLock = new Object();

    /**
     * 全局的是否正在刷新jsapi_ticket的锁
     *
     */
    protected final Object globalJsapiTicketRefreshLock = new Object();

    protected WxMpConfigStorage wxMpConfigStorage;

    protected CloseableHttpClient httpClient;

    protected HttpHost httpProxy;

    private int retrySleepMillis = 1000;

    private int maxRetryTimes = 5;

    protected WxSessionManager sessionManager = new StandardSessionManager();

    public boolean checkSignature(String timestamp, String nonce, String signature) {
        try {
            return SHA1.gen(wxMpConfigStorage.getToken(), timestamp, nonce).equals(signature);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean checkSignature(String timestamp, String nonce, String signature, String mpTag) {
        try {
            return SHA1.gen(wxMpConfigStorage.getToken(mpTag), timestamp, nonce).equals(signature);
        } catch (Exception e) {
            return false;
        }
    }

    public String getAccessToken() throws WxErrorException {
        return getAccessToken(false);
    }

    @Override
    public String getAccessToken(String mpTag) throws WxErrorException {
        return getAccessToken(false,mpTag);
    }

    public String getAccessToken(boolean forceRefresh) throws WxErrorException {
        if (forceRefresh) {
            wxMpConfigStorage.expireAccessToken();
        }
        if (wxMpConfigStorage.isAccessTokenExpired()) {
            synchronized (globalAccessTokenRefreshLock) {
                if (wxMpConfigStorage.isAccessTokenExpired()) {
                    String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"
                            + "&appid=" + wxMpConfigStorage.getAppId()
                            + "&secret=" + wxMpConfigStorage.getSecret();
                    try {
                        HttpGet httpGet = new HttpGet(url);
                        if (httpProxy != null) {
                            RequestConfig config = RequestConfig.custom().setProxy(httpProxy).build();
                            httpGet.setConfig(config);
                        }
                        CloseableHttpClient httpclient = getHttpclient();
                        CloseableHttpResponse response = httpclient.execute(httpGet);
                        String resultContent = new BasicResponseHandler().handleResponse(response);
                        WxError error = WxError.fromJson(resultContent);
                        if (error.getErrorCode() != 0) {
                            throw new WxErrorException(error);
                        }
                        WxAccessToken accessToken = WxAccessToken.fromJson(resultContent);
                        wxMpConfigStorage.updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
                    } catch (ClientProtocolException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return wxMpConfigStorage.getAccessToken();
    }

    @Override
    public String getAccessToken(boolean forceRefresh, String mpTag) throws WxErrorException {
        if (forceRefresh) {
            wxMpConfigStorage.expireAccessToken(mpTag);
        }
        if (wxMpConfigStorage.isAccessTokenExpired(mpTag)) {
            synchronized (globalAccessTokenRefreshLock) {
                if (wxMpConfigStorage.isAccessTokenExpired(mpTag)) {
                    String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"
                            + "&appid=" + wxMpConfigStorage.getAppId(mpTag)
                            + "&secret=" + wxMpConfigStorage.getSecret(mpTag);
                    try {
                        HttpGet httpGet = new HttpGet(url);
                        if (httpProxy != null) {
                            RequestConfig config = RequestConfig.custom().setProxy(httpProxy).build();
                            httpGet.setConfig(config);
                        }
                        CloseableHttpClient httpclient = getHttpclient();
                        CloseableHttpResponse response = httpclient.execute(httpGet);
                        String resultContent = new BasicResponseHandler().handleResponse(response);
                        WxError error = WxError.fromJson(resultContent);
                        if (error.getErrorCode() != 0) {
                            throw new WxErrorException(error);
                        }
                        WxAccessToken accessToken = WxAccessToken.fromJson(resultContent);
                        wxMpConfigStorage.updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn(),mpTag);
                    } catch (ClientProtocolException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return wxMpConfigStorage.getAccessToken(mpTag);
    }

    public String getJsapiTicket() throws WxErrorException {
        return getJsapiTicket(false);
    }

    @Override
    public String getJsapiTicket(String mpTag) throws WxErrorException {
        return getJsapiTicket(false,mpTag);
    }

    public String getJsapiTicket(boolean forceRefresh) throws WxErrorException {
        if (forceRefresh) {
            wxMpConfigStorage.expireJsapiTicket();
        }
        if (wxMpConfigStorage.isJsapiTicketExpired()) {
            synchronized (globalJsapiTicketRefreshLock) {
                if (wxMpConfigStorage.isJsapiTicketExpired()) {
                    String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi";
                    String responseContent = execute(new SimpleGetRequestExecutor(), url, null);
                    JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
                    JsonObject tmpJsonObject = tmpJsonElement.getAsJsonObject();
                    String jsapiTicket = tmpJsonObject.get("ticket").getAsString();
                    int expiresInSeconds = tmpJsonObject.get("expires_in").getAsInt();
                    wxMpConfigStorage.updateJsapiTicket(jsapiTicket, expiresInSeconds);
                }
            }
        }
        return wxMpConfigStorage.getJsapiTicket();
    }

    @Override
    public String getJsapiTicket(boolean forceRefresh, String mpTag) throws WxErrorException {
        if (forceRefresh) {
            wxMpConfigStorage.expireJsapiTicket(mpTag);
        }
        if (wxMpConfigStorage.isJsapiTicketExpired(mpTag)) {
            synchronized (globalJsapiTicketRefreshLock) {
                if (wxMpConfigStorage.isJsapiTicketExpired(mpTag)) {
                    String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi";

                    String responseContent = null;
                    if (StringUtils.isBlank(mpTag)){
                        responseContent = execute(new SimpleGetRequestExecutor(), url, null,mpTag);
                    }else{
                        responseContent = execute(new SimpleGetRequestExecutor(), url, null);
                    }
                    JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
                    JsonObject tmpJsonObject = tmpJsonElement.getAsJsonObject();
                    String jsapiTicket = tmpJsonObject.get("ticket").getAsString();
                    int expiresInSeconds = tmpJsonObject.get("expires_in").getAsInt();
                    wxMpConfigStorage.updateJsapiTicket(jsapiTicket, expiresInSeconds,mpTag);
                }
            }
        }
        return wxMpConfigStorage.getJsapiTicket(mpTag);
    }

    public WxJsapiSignature createJsapiSignature(String url) throws WxErrorException {
        long timestamp = System.currentTimeMillis() / 1000;
        String noncestr = RandomUtils.getRandomStr();
        String jsapiTicket = getJsapiTicket(false);
        try {
            String signature = SHA1.genWithAmple(
                    "jsapi_ticket=" + jsapiTicket,
                    "noncestr=" + noncestr,
                    "timestamp=" + timestamp,
                    "url=" + url
            );
            WxJsapiSignature jsapiSignature = new WxJsapiSignature();
            jsapiSignature.setTimestamp(timestamp);
            jsapiSignature.setNoncestr(noncestr);
            jsapiSignature.setUrl(url);
            jsapiSignature.setSignature(signature);
            return jsapiSignature;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void customMessageSend(com.daniel.weixin.mp.bean.WxMpCustomMessage message) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send";
        execute(new SimplePostRequestExecutor(), url, message.toJson());
    }

    public void menuCreate(WxMenu menu) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/menu/create";
        execute(new SimplePostRequestExecutor(), url, menu.toJson());
    }

    public void menuDelete() throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/menu/delete";
        execute(new SimpleGetRequestExecutor(), url, null);
    }

    public WxMenu menuGet() throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/menu/get";
        try {
            String resultContent = execute(new SimpleGetRequestExecutor(), url, null);
            return WxMenu.fromJson(resultContent);
        } catch (WxErrorException e) {
            // 46003 不存在的菜单数据
            if (e.getError().getErrorCode() == 46003) {
                return null;
            }
            throw e;
        }
    }

    public WxMediaUploadResult mediaUpload(String mediaType, String fileType, InputStream inputStream) throws WxErrorException, IOException {
        return mediaUpload(mediaType, FileUtils.createTmpFile(inputStream, UUID.randomUUID().toString(), fileType));
    }

    public WxMediaUploadResult mediaUpload(String mediaType, File file) throws WxErrorException {
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/upload?type=" + mediaType;
        return execute(new MediaUploadRequestExecutor(), url, file);
    }

    public File mediaDownload(String media_id) throws WxErrorException {
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/get";
        return execute(new MediaDownloadRequestExecutor(), url, "media_id=" + media_id);
    }

    public com.daniel.weixin.mp.bean.result.WxMpMassUploadResult massNewsUpload(com.daniel.weixin.mp.bean.WxMpMassNews news) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/media/uploadnews";
        String responseContent = execute(new SimplePostRequestExecutor(), url, news.toJson());
        return com.daniel.weixin.mp.bean.result.WxMpMassUploadResult.fromJson(responseContent);
    }

    public com.daniel.weixin.mp.bean.result.WxMpMassUploadResult massVideoUpload(com.daniel.weixin.mp.bean.WxMpMassVideo video) throws WxErrorException {
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/uploadvideo";
        String responseContent = execute(new SimplePostRequestExecutor(), url, video.toJson());
        return com.daniel.weixin.mp.bean.result.WxMpMassUploadResult.fromJson(responseContent);
    }

    public com.daniel.weixin.mp.bean.result.WxMpMassSendResult massGroupMessageSend(com.daniel.weixin.mp.bean.WxMpMassGroupMessage message) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall";
        String responseContent = execute(new SimplePostRequestExecutor(), url, message.toJson());
        return com.daniel.weixin.mp.bean.result.WxMpMassSendResult.fromJson(responseContent);
    }

    public com.daniel.weixin.mp.bean.result.WxMpMassSendResult massOpenIdsMessageSend(com.daniel.weixin.mp.bean.WxMpMassOpenIdsMessage message) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/message/mass/send";
        String responseContent = execute(new SimplePostRequestExecutor(), url, message.toJson());
        return com.daniel.weixin.mp.bean.result.WxMpMassSendResult.fromJson(responseContent);
    }

    public com.daniel.weixin.mp.bean.WxMpGroup groupCreate(String name) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/groups/create";
        JsonObject json = new JsonObject();
        JsonObject groupJson = new JsonObject();
        json.add("group", groupJson);
        groupJson.addProperty("name", name);

        String responseContent = execute(
                new SimplePostRequestExecutor(),
                url,
                json.toString());
        return com.daniel.weixin.mp.bean.WxMpGroup.fromJson(responseContent);
    }

    public List<com.daniel.weixin.mp.bean.WxMpGroup> groupGet() throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/groups/get";
        String responseContent = execute(new SimpleGetRequestExecutor(), url, null);
    /*
     * 操蛋的微信API，创建时返回的是 { group : { id : ..., name : ...} }
     * 查询时返回的是 { groups : [ { id : ..., name : ..., count : ... }, ... ] }
     */
        JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
        return com.daniel.weixin.mp.util.json.WxMpGsonBuilder.INSTANCE.create().fromJson(tmpJsonElement.getAsJsonObject().get("groups"), new TypeToken<List<com.daniel.weixin.mp.bean.WxMpGroup>>() {
        }.getType());
    }

    public long userGetGroup(String openid) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/groups/getid";
        JsonObject o = new JsonObject();
        o.addProperty("openid", openid);
        String responseContent = execute(new SimplePostRequestExecutor(), url, o.toString());
        JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
        return GsonHelper.getAsLong(tmpJsonElement.getAsJsonObject().get("groupid"));
    }

    public void groupUpdate(com.daniel.weixin.mp.bean.WxMpGroup group) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/groups/update";
        execute(new SimplePostRequestExecutor(), url, group.toJson());
    }

    public void userUpdateGroup(String openid, long to_groupid) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/groups/members/update";
        JsonObject json = new JsonObject();
        json.addProperty("openid", openid);
        json.addProperty("to_groupid", to_groupid);
        execute(new SimplePostRequestExecutor(), url, json.toString());
    }

    public void userUpdateRemark(String openid, String remark) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark";
        JsonObject json = new JsonObject();
        json.addProperty("openid", openid);
        json.addProperty("remark", remark);
        execute(new SimplePostRequestExecutor(), url, json.toString());
    }

    public com.daniel.weixin.mp.bean.result.WxMpUser userInfo(String openid, String lang) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info";
        lang = lang == null ? "zh_CN" : lang;
        String responseContent = execute(new SimpleGetRequestExecutor(), url, "openid=" + openid + "&lang=" + lang);
        return com.daniel.weixin.mp.bean.result.WxMpUser.fromJson(responseContent);
    }

    @Override
    public WxMpUser userInfo(String openid, String lang, String mpTag) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info";
        lang = lang == null ? "zh_CN" : lang;
        String responseContent = execute(new SimpleGetRequestExecutor(), url, "openid=" + openid + "&lang=" + lang,mpTag);
        return com.daniel.weixin.mp.bean.result.WxMpUser.fromJson(responseContent);
    }

    public com.daniel.weixin.mp.bean.result.WxMpUserList userList(String next_openid) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/user/get";
        String responseContent = execute(new SimpleGetRequestExecutor(), url, next_openid == null ? null : "next_openid=" + next_openid);
        return com.daniel.weixin.mp.bean.result.WxMpUserList.fromJson(responseContent);
    }

    public com.daniel.weixin.mp.bean.result.WxMpQrCodeTicket qrCodeCreateTmpTicket(int scene_id, Integer expire_seconds) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
        JsonObject json = new JsonObject();
        json.addProperty("action_name", "QR_SCENE");
        if (expire_seconds != null) {
            json.addProperty("expire_seconds", expire_seconds);
        }
        JsonObject actionInfo = new JsonObject();
        JsonObject scene = new JsonObject();
        scene.addProperty("scene_id", scene_id);
        actionInfo.add("scene", scene);
        json.add("action_info", actionInfo);
        String responseContent = execute(new SimplePostRequestExecutor(), url, json.toString());
        return com.daniel.weixin.mp.bean.result.WxMpQrCodeTicket.fromJson(responseContent);
    }

    public com.daniel.weixin.mp.bean.result.WxMpQrCodeTicket qrCodeCreateLastTicket(int scene_id) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
        JsonObject json = new JsonObject();
        json.addProperty("action_name", "QR_LIMIT_SCENE");
        JsonObject actionInfo = new JsonObject();
        JsonObject scene = new JsonObject();
        scene.addProperty("scene_id", scene_id);
        actionInfo.add("scene", scene);
        json.add("action_info", actionInfo);
        String responseContent = execute(new SimplePostRequestExecutor(), url, json.toString());
        return com.daniel.weixin.mp.bean.result.WxMpQrCodeTicket.fromJson(responseContent);
    }

    public File qrCodePicture(com.daniel.weixin.mp.bean.result.WxMpQrCodeTicket ticket) throws WxErrorException {
        String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode";
        return execute(new com.daniel.weixin.mp.util.http.QrCodeRequestExecutor(), url, ticket);
    }

    public String shortUrl(String long_url) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/shorturl";
        JsonObject o = new JsonObject();
        o.addProperty("action", "long2short");
        o.addProperty("long_url", long_url);
        String responseContent = execute(new SimplePostRequestExecutor(), url, o.toString());
        JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
        return tmpJsonElement.getAsJsonObject().get("short_url").getAsString();
    }

    public String templateSend(com.daniel.weixin.mp.bean.WxMpTemplateMessage templateMessage) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send";
        String responseContent = execute(new SimplePostRequestExecutor(), url, templateMessage.toJson());
        JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
        return tmpJsonElement.getAsJsonObject().get("msgid").getAsString();
    }

    public com.daniel.weixin.mp.bean.result.WxMpSemanticQueryResult semanticQuery(com.daniel.weixin.mp.bean.WxMpSemanticQuery semanticQuery) throws WxErrorException {
        String url = "https://api.weixin.qq.com/semantic/semproxy/search";
        String responseContent = execute(new SimplePostRequestExecutor(), url, semanticQuery.toJson());
        return WxMpSemanticQueryResult.fromJson(responseContent);
    }

    @Override
    public String oauth2buildAuthorizationUrl(String scope, String state) {
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?";
        url += "appid=" + wxMpConfigStorage.getAppId();
        url += "&redirect_uri=" + URIUtil.encodeURIComponent(wxMpConfigStorage.getOauth2redirectUri());
        url += "&response_type=code";
        url += "&scope=" + scope;
        if (state != null) {
            url += "&state=" + state;
        }
        url += "#wechat_redirect";
        return url;
    }

    @Override
    public com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken oauth2getAccessToken(String code) throws WxErrorException {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?";
        url += "appid=" + wxMpConfigStorage.getAppId();
        url += "&secret=" + wxMpConfigStorage.getSecret();
        url += "&code=" + code;
        url += "&grant_type=authorization_code";

        try {
            RequestExecutor<String, String> executor = new SimpleGetRequestExecutor();
            String responseText = executor.execute(getHttpclient(), httpProxy, url, null);
            return com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken.fromJson(responseText);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WxMpOAuth2AccessToken oauth2getAccessToken(String code, String mpTag) throws WxErrorException {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?";
        url += "appid=" + wxMpConfigStorage.getAppId(mpTag);
        url += "&secret=" + wxMpConfigStorage.getSecret(mpTag);
        url += "&code=" + code;
        url += "&grant_type=authorization_code";

        try {
            RequestExecutor<String, String> executor = new SimpleGetRequestExecutor();
            String responseText = executor.execute(getHttpclient(), httpProxy, url, null,mpTag);
            return com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken.fromJson(responseText);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken oauth2refreshAccessToken(String refreshToken) throws WxErrorException {
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?";
        url += "appid=" + wxMpConfigStorage.getAppId();
        url += "&grant_type=refresh_token";
        url += "&refresh_token=" + refreshToken;

        try {
            RequestExecutor<String, String> executor = new SimpleGetRequestExecutor();
            String responseText = executor.execute(getHttpclient(), httpProxy, url, null);
            return com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken.fromJson(responseText);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WxMpOAuth2AccessToken oauth2refreshAccessToken(String refreshToken, String mpTag) throws WxErrorException {
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?";
        url += "appid=" + wxMpConfigStorage.getAppId(mpTag);
        url += "&grant_type=refresh_token";
        url += "&refresh_token=" + refreshToken;

        try {
            RequestExecutor<String, String> executor = new SimpleGetRequestExecutor();
            String responseText = executor.execute(getHttpclient(), httpProxy, url, null,mpTag);
            return com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken.fromJson(responseText);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public com.daniel.weixin.mp.bean.result.WxMpUser oauth2getUserInfo(com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken oAuth2AccessToken, String lang) throws WxErrorException {
        String url = "https://api.weixin.qq.com/sns/userinfo?";
        url += "access_token=" + oAuth2AccessToken.getAccessToken();
        url += "&openid=" + oAuth2AccessToken.getOpenId();
        if (lang == null) {
            url += "&lang=zh_CN";
        } else {
            url += "&lang=" + lang;
        }

        try {
            RequestExecutor<String, String> executor = new SimpleGetRequestExecutor();
            String responseText = executor.execute(getHttpclient(), httpProxy, url, null);
            return com.daniel.weixin.mp.bean.result.WxMpUser.fromJson(responseText);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WxMpUser oauth2getUserInfo(WxMpOAuth2AccessToken oAuth2AccessToken, String lang, String mpTag) throws WxErrorException {
        String url = "https://api.weixin.qq.com/sns/userinfo?";
        url += "access_token=" + oAuth2AccessToken.getAccessToken();
        url += "&openid=" + oAuth2AccessToken.getOpenId();
        if (lang == null) {
            url += "&lang=zh_CN";
        } else {
            url += "&lang=" + lang;
        }

        try {
            RequestExecutor<String, String> executor = new SimpleGetRequestExecutor();
            String responseText = executor.execute(getHttpclient(), httpProxy, url, null,mpTag);
            return com.daniel.weixin.mp.bean.result.WxMpUser.fromJson(responseText);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean oauth2validateAccessToken(com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken oAuth2AccessToken) {
        String url = "https://api.weixin.qq.com/sns/auth?";
        url += "access_token=" + oAuth2AccessToken.getAccessToken();
        url += "&openid=" + oAuth2AccessToken.getOpenId();

        try {
            RequestExecutor<String, String> executor = new SimpleGetRequestExecutor();
            executor.execute(getHttpclient(), httpProxy, url, null);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (WxErrorException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean oauth2validateAccessToken(WxMpOAuth2AccessToken oAuth2AccessToken, String mpTag) {
        String url = "https://api.weixin.qq.com/sns/auth?";
        url += "access_token=" + oAuth2AccessToken.getAccessToken();
        url += "&openid=" + oAuth2AccessToken.getOpenId();

        try {
            RequestExecutor<String, String> executor = new SimpleGetRequestExecutor();
            executor.execute(getHttpclient(), httpProxy, url, null,mpTag);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (WxErrorException e) {
            return false;
        }
        return true;
    }

    @Override
    public String[] getCallbackIP() throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/getcallbackip";
        String responseContent = get(url, null);
        JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
        JsonArray ipList = tmpJsonElement.getAsJsonObject().get("ip_list").getAsJsonArray();
        String[] ipArray = new String[ipList.size()];
        for (int i = 0; i < ipList.size(); i++) {
            ipArray[i] = ipList.get(i).getAsString();
        }
        return ipArray;
    }


    @Override
    public List<com.daniel.weixin.mp.bean.result.WxMpUserSummary> getUserSummary(Date beginDate, Date endDate) throws WxErrorException {
        String url = "https://api.weixin.qq.com/datacube/getusersummary";
        JsonObject param = new JsonObject();
        param.addProperty("begin_date", SIMPLE_DATE_FORMAT.format(beginDate));
        param.addProperty("end_date", SIMPLE_DATE_FORMAT.format(endDate));
        String responseContent = post(url, param.toString());
        JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
        return com.daniel.weixin.mp.util.json.WxMpGsonBuilder.INSTANCE.create().fromJson(tmpJsonElement.getAsJsonObject().get("list"), new TypeToken<List<com.daniel.weixin.mp.bean.result.WxMpUserSummary>>() {
        }.getType());
    }

    @Override
    public List<com.daniel.weixin.mp.bean.result.WxMpUserCumulate> getUserCumulate(Date beginDate, Date endDate) throws WxErrorException {
        String url = "https://api.weixin.qq.com/datacube/getusercumulate";
        JsonObject param = new JsonObject();
        param.addProperty("begin_date", SIMPLE_DATE_FORMAT.format(beginDate));
        param.addProperty("end_date", SIMPLE_DATE_FORMAT.format(endDate));
        String responseContent = post(url, param.toString());
        JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
        return com.daniel.weixin.mp.util.json.WxMpGsonBuilder.INSTANCE.create().fromJson(tmpJsonElement.getAsJsonObject().get("list"), new TypeToken<List<com.daniel.weixin.mp.bean.result.WxMpUserCumulate>>() {
        }.getType());
    }

    public String get(String url, String queryParam) throws WxErrorException {
        return execute(new SimpleGetRequestExecutor(), url, queryParam);
    }

    public String post(String url, String postData) throws WxErrorException {
        return execute(new SimplePostRequestExecutor(), url, postData);
    }

    @Override
    public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws WxErrorException {
        int retryTimes = 0;
        do {
            try {
                return executeInternal(executor, uri, data);
            } catch (WxErrorException e) {
                WxError error = e.getError();
                /**
                 * -1 系统繁忙, 1000ms后重试
                 */
                if (error.getErrorCode() == -1) {
                    int sleepMillis = retrySleepMillis * (1 << retryTimes);
                    try {
                        log.debug("微信系统繁忙，{}ms 后重试(第{}次)", sleepMillis, retryTimes + 1);
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException e1) {
                        throw new RuntimeException(e1);
                    }
                } else {
                    throw e;
                }
            }
        } while (++retryTimes < maxRetryTimes);

        throw new RuntimeException("微信服务端异常，超出重试次数");
    }

    /**
     * 向微信端发送请求，在这里执行的策略是当发生access_token过期时才去刷新，然后重新执行请求，而不是全局定时请求
     *
     * @param executor
     * @param uri
     * @param data
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data,String mpTag) throws WxErrorException {
        int retryTimes = 0;
        do {
            try {
                return executeInternal(executor, uri, data,mpTag);
            } catch (WxErrorException e) {
                WxError error = e.getError();
                /**
                 * -1 系统繁忙, 1000ms后重试
                 */
                if (error.getErrorCode() == -1) {
                    int sleepMillis = retrySleepMillis * (1 << retryTimes);
                    try {
                        log.debug("微信系统繁忙，{}ms 后重试(第{}次)", sleepMillis, retryTimes + 1);
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException e1) {
                        throw new RuntimeException(e1);
                    }
                } else {
                    throw e;
                }
            }
        } while (++retryTimes < maxRetryTimes);

        throw new RuntimeException("微信服务端异常，超出重试次数");
    }

    protected <T, E> T executeInternal(RequestExecutor<T, E> executor, String uri, E data,String mpTag) throws WxErrorException {
        String accessToken = getAccessToken(false,mpTag);

        String uriWithAccessToken = uri;
        uriWithAccessToken += uri.indexOf('?') == -1 ? "?access_token=" + accessToken : "&access_token=" + accessToken;

        try {
            return executor.execute(getHttpclient(), httpProxy, uriWithAccessToken, data,mpTag);
        } catch (WxErrorException e) {
            WxError error = e.getError();
      /*
       * 发生以下情况时尝试刷新access_token
       * 40001 获取access_token时AppSecret错误，或者access_token无效
       * 42001 access_token超时
       */
            if (error.getErrorCode() == 42001 || error.getErrorCode() == 40001) {
                // 强制设置wxMpConfigStorage它的access token过期了，这样在下一次请求里就会刷新access token
                if (StringUtils.isNotBlank(mpTag)){
                    wxMpConfigStorage.expireAccessToken(mpTag);
                }else{
                    wxMpConfigStorage.expireAccessToken();
                }
                return execute(executor, uri, data,mpTag);
            }
            if (error.getErrorCode() != 0) {
                throw new WxErrorException(error);
            }
            return null;
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T, E> T executeInternal(RequestExecutor<T, E> executor, String uri, E data) throws WxErrorException {
        String accessToken = getAccessToken(false);

        String uriWithAccessToken = uri;
        uriWithAccessToken += uri.indexOf('?') == -1 ? "?access_token=" + accessToken : "&access_token=" + accessToken;

        try {
            return executor.execute(getHttpclient(), httpProxy, uriWithAccessToken, data);
        } catch (WxErrorException e) {
            WxError error = e.getError();
      /*
       * 发生以下情况时尝试刷新access_token
       * 40001 获取access_token时AppSecret错误，或者access_token无效
       * 42001 access_token超时
       */
            if (error.getErrorCode() == 42001 || error.getErrorCode() == 40001) {
                // 强制设置wxMpConfigStorage它的access token过期了，这样在下一次请求里就会刷新access token
                wxMpConfigStorage.expireAccessToken();
                return execute(executor, uri, data);
            }
            if (error.getErrorCode() != 0) {
                throw new WxErrorException(error);
            }
            return null;
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected CloseableHttpClient getHttpclient() {
        return httpClient;
    }

    public void setWxMpConfigStorage(WxMpConfigStorage wxConfigProvider) {
        this.wxMpConfigStorage = wxConfigProvider;

        String http_proxy_host = wxMpConfigStorage.getHttp_proxy_host();
        int http_proxy_port = wxMpConfigStorage.getHttp_proxy_port();
        String http_proxy_username = wxMpConfigStorage.getHttp_proxy_username();
        String http_proxy_password = wxMpConfigStorage.getHttp_proxy_password();

        if (StringUtils.isNotBlank(http_proxy_host)) {
            // 使用代理服务器
            if (StringUtils.isNotBlank(http_proxy_username)) {
                // 需要用户认证的代理服务器
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(
                        new AuthScope(http_proxy_host, http_proxy_port),
                        new UsernamePasswordCredentials(http_proxy_username, http_proxy_password));
                httpClient = HttpClients
                        .custom()
                        .setDefaultCredentialsProvider(credsProvider)
                        .build();
            } else {
                // 无需用户认证的代理服务器
                httpClient = HttpClients.createDefault();
            }
            httpProxy = new HttpHost(http_proxy_host, http_proxy_port);
        } else {
            httpClient = HttpClients.createDefault();
        }
    }

    @Override
    public void setRetrySleepMillis(int retrySleepMillis) {
        this.retrySleepMillis = retrySleepMillis;
    }


    @Override
    public void setMaxRetryTimes(int maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }

}
