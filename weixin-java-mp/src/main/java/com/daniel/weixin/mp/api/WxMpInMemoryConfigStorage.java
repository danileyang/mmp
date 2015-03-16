package com.daniel.weixin.mp.api;

import com.daniel.weixin.common.model.response.WxAccessToken;

/**
 * 基于内存的微信配置provider，在实际生产环境中应该将这些配置持久化
 *
 * @author chanjarster/danielyang
 */
public class WxMpInMemoryConfigStorage implements com.daniel.weixin.mp.api.WxMpConfigStorage {

    protected volatile String appId;
    protected volatile String secret;
    protected volatile String token;
    protected volatile String accessToken;
    protected volatile String aesKey;
    protected volatile long expiresTime;

    protected volatile String oauth2redirectUri;

    protected volatile String http_proxy_host;
    protected volatile int http_proxy_port;
    protected volatile String http_proxy_username;
    protected volatile String http_proxy_password;

    protected volatile String jsapiTicket;
    protected volatile long jsapiTicketExpiresTime;

    public String getAccessToken() {
        return this.accessToken;
    }

    @Override
    public String getAccessToken(String mpTag) {
        return null;
    }

    public boolean isAccessTokenExpired() {
        return System.currentTimeMillis() > this.expiresTime;
    }

    @Override
    public boolean isAccessTokenExpired(String mpTag) {
        return false;
    }

    public synchronized void updateAccessToken(WxAccessToken accessToken) {
        updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
    }

    @Override
    public void updateAccessToken(WxAccessToken accessToken, String mpTag) {

    }

    public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
        this.accessToken = accessToken;
        this.expiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000l;
    }

    @Override
    public void updateAccessToken(String accessToken, int expiresIn, String mpTag) {
        //TODO:impl
    }

    public void expireAccessToken() {
        this.expiresTime = 0;
    }

    @Override
    public void expireAccessToken(String mpTag) {
        //TODO:impl
    }

    public String getJsapiTicket() {
        return jsapiTicket;
    }

    @Override
    public String getJsapiTicket(String mpTag) {
        return null;
    }

    public void setJsapiTicket(String jsapiTicket) {
        this.jsapiTicket = jsapiTicket;
    }

    public long getJsapiTicketExpiresTime() {
        return jsapiTicketExpiresTime;
    }

    public void setJsapiTicketExpiresTime(long jsapiTicketExpiresTime) {
        this.jsapiTicketExpiresTime = jsapiTicketExpiresTime;
    }

    public boolean isJsapiTicketExpired() {
        return System.currentTimeMillis() > this.jsapiTicketExpiresTime;
    }

    @Override
    public boolean isJsapiTicketExpired(String mpTag) {
        return false;
    }

    public synchronized void updateJsapiTicket(String jsapiTicket, int expiresInSeconds) {
        this.jsapiTicket = jsapiTicket;
        // 预留200秒的时间
        this.jsapiTicketExpiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000l;
    }

    @Override
    public void updateJsapiTicket(String jsapiTicket, int expiresInSeconds, String mpTag) {
        //TODO:impl
    }

    public void expireJsapiTicket() {
        this.jsapiTicketExpiresTime = 0;
    }

    @Override
    public void expireJsapiTicket(String mpTag) {
        //TODO:impl
    }

    public String getAppId() {
        return this.appId;
    }

    @Override
    public String getAppId(String mpTag) {
        return null;
    }

    @Override
    public void setAppId(String mpTag, boolean multi) {
        //TODO:impl
    }

    public String getSecret() {
        return this.secret;
    }

    @Override
    public void setSecret(String mpTag, boolean multi) {

    }

    @Override
    public String getSecret(String mpTag) {
        return null;
    }

    public String getToken() {
        return this.token;
    }

    @Override
    public void setToken(String mpTag, boolean multi) {

    }


    @Override
    public String getToken(String mpTag) {
        return null;
    }

    public long getExpiresTime() {
        return this.expiresTime;
    }

    @Override
    public void setExpiresTime(String mpTag, boolean multi) {

    }

    @Override
    public long getExpiresTime(String mpTag) {
        return 0;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAesKey() {
        return aesKey;
    }

    @Override
    public void setAesKey(String mpTag, boolean multi) {

    }

    @Override
    public String getAesKey(String mpTag) {
        return null;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }

    @Override
    public String getOauth2redirectUri() {
        return this.oauth2redirectUri;
    }

    @Override
    public String getOauth2redirectUri(String mpTag) {
        return null;
    }

    public void setOauth2redirectUri(String oauth2redirectUri) {
        this.oauth2redirectUri = oauth2redirectUri;
    }

    public String getHttp_proxy_host() {
        return http_proxy_host;
    }

    @Override
    public String getHttp_proxy_host(String mpTag) {
        return null;
    }

    public void setHttp_proxy_host(String http_proxy_host) {
        this.http_proxy_host = http_proxy_host;
    }

    public int getHttp_proxy_port() {
        return http_proxy_port;
    }

    @Override
    public int getHttp_proxy_port(String mpTag) {
        return 0;
    }

    public void setHttp_proxy_port(int http_proxy_port) {
        this.http_proxy_port = http_proxy_port;
    }

    public String getHttp_proxy_username() {
        return http_proxy_username;
    }

    @Override
    public String getHttp_proxy_username(String mpTag) {
        return null;
    }

    public void setHttp_proxy_username(String http_proxy_username) {
        this.http_proxy_username = http_proxy_username;
    }

    public String getHttp_proxy_password() {
        return http_proxy_password;
    }

    @Override
    public String getHttp_proxy_password(String mpTag) {
        return null;
    }

    public void setHttp_proxy_password(String http_proxy_password) {
        this.http_proxy_password = http_proxy_password;
    }

    @Override
    public String toString() {
        return "WxMpInMemoryConfigStorage{" +
                "appId='" + appId + '\'' +
                ", secret='" + secret + '\'' +
                ", token='" + token + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", aesKey='" + aesKey + '\'' +
                ", expiresTime=" + expiresTime +
                ", http_proxy_host='" + http_proxy_host + '\'' +
                ", http_proxy_port=" + http_proxy_port +
                ", http_proxy_username='" + http_proxy_username + '\'' +
                ", http_proxy_password='" + http_proxy_password + '\'' +
                ", jsapiTicket='" + jsapiTicket + '\'' +
                ", jsapiTicketExpiresTime='" + jsapiTicketExpiresTime + '\'' +
                '}';
    }
}
