package com.daniel.weixin.mp.api;

import com.daniel.weixin.common.model.response.WxAccessToken;

/**
 * 微信客户端配置存储
 *
 * @author chanjarster/danielyang
 */
public interface WxMpConfigStorage {

    public String getAccessToken();

    public String getAccessToken(String mpTag);

    public boolean isAccessTokenExpired();

    public boolean isAccessTokenExpired(String mpTag);

    /**
     * 强制将access token过期掉
     */
    public void expireAccessToken();

    /**
     * 强制将access token过期掉
     */
    public void expireAccessToken(String mpTag);

    /**
     * 应该是线程安全的
     *
     * @param accessToken
     */
    public void updateAccessToken(WxAccessToken accessToken);

    /**
     * 应该是线程安全的
     *
     * @param accessToken
     */
    public void updateAccessToken(WxAccessToken accessToken,String mpTag);

    /**
     * 应该是线程安全的
     *
     * @param accessToken
     * @param expiresIn
     */
    public void updateAccessToken(String accessToken, int expiresIn);

    public void updateAccessToken(String accessToken, int expiresIn,String mpTag);

    public String getJsapiTicket();

    public String getJsapiTicket(String mpTag);

    public boolean isJsapiTicketExpired();

    public boolean isJsapiTicketExpired(String mpTag);

    /**
     * 强制将jsapi ticket过期掉
     */
    public void expireJsapiTicket();

    /**
     * 强制将jsapi ticket过期掉
     */
    public void expireJsapiTicket(String mpTag);

    /**
     * 应该是线程安全的
     *
     * @param jsapiTicket
     */
    public void updateJsapiTicket(String jsapiTicket, int expiresInSeconds);

    /**
     * 应该是线程安全的
     *
     * @param jsapiTicket
     */
    public void updateJsapiTicket(String jsapiTicket, int expiresInSeconds,String mpTag);

    public String getAppId();

    public String getAppId(String mpTag);

    public void setAppId(String mpTag,boolean multi);

    public String getSecret();

    public void setSecret(String mpTag,boolean multi);

    public String getSecret(String mpTag);

    public String getToken();

    public void setToken(String mpTag,boolean multi);

    public String getToken(String mpTag);

    public String getAesKey();

    public void setAesKey(String mpTag,boolean multi);

    public String getAesKey(String mpTag);


    public long getExpiresTime();

    public void setExpiresTime(String mpTag,boolean multi);

    public long getExpiresTime(String mpTag);

    public String getOauth2redirectUri();

    public String getOauth2redirectUri(String mpTag);

    public String getHttp_proxy_host();

    public String getHttp_proxy_host(String mpTag);

    public int getHttp_proxy_port();

    public int getHttp_proxy_port(String mpTag);

    public String getHttp_proxy_username();

    public String getHttp_proxy_username(String mpTag);

    public String getHttp_proxy_password();

    public String getHttp_proxy_password(String mpTag);

}
