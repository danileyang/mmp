package com.daniel.weixin.mp.api;

import com.daniel.weixin.common.exception.WxErrorException;
import com.daniel.weixin.common.model.response.WxJsapiSignature;
import com.daniel.weixin.common.model.response.WxMediaUploadResult;
import com.daniel.weixin.common.model.response.WxMenu;
import com.daniel.weixin.common.util.http.RequestExecutor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 微信API的Service
 */
public interface WxMpService {

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * <pre>
     * 验证推送过来的消息的正确性
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=验证消息真实性
     * </pre>
     *
     * @param timestamp
     * @param nonce
     * @param signature
     * @return
     */
    public boolean checkSignature(String timestamp, String nonce, String signature);

    /**
     * <pre>
     * 验证推送过来的消息的正确性
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=验证消息真实性
     * </pre>
     *
     * @param timestamp
     * @param nonce
     * @param signature
     * @param mpTag
     * @return
     */
    public boolean checkSignature(String timestamp, String nonce, String signature,String mpTag);

    /**
     * 获取access_token, 不强制刷新access_token
     *
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     * @see #getAccessToken(boolean)
     */
    public String getAccessToken() throws WxErrorException;

    /**

     * @param mpTag
     * @return
     * @throws WxErrorException
     */
    public String getAccessToken(String mpTag) throws WxErrorException;

    /**
     * <pre>
     * 获取access_token，本方法线程安全
     * 且在多线程同时刷新时只刷新一次，避免超出2000次/日的调用次数上限
     *
     * 另：本service的所有方法都会在access_token过期是调用此方法
     *
     * 程序员在非必要情况下尽量不要主动调用此方法
     *
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=获取access_token
     * </pre>
     *
     * @param forceRefresh 强制刷新
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public String getAccessToken(boolean forceRefresh) throws WxErrorException;

    /**
     *
     * @param forceRefresh
     * @param mpTag
     * @return
     * @throws WxErrorException
     */
    public String getAccessToken(boolean forceRefresh,String mpTag) throws WxErrorException;

    /**
     * 获得jsapi_ticket,不强制刷新jsapi_ticket
     *
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     * @see #getJsapiTicket(boolean)
     */
    public String getJsapiTicket() throws WxErrorException;

    /**
     *
     * @param mpTag
     * @return
     * @throws WxErrorException
     */
    public String getJsapiTicket(String mpTag) throws WxErrorException;

    /**
     * <pre>
     * 获得jsapi_ticket
     * 获得时会检查jsapiToken是否过期，如果过期了，那么就刷新一下，否则就什么都不干
     *
     * 详情请见：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html#.E9.99.84.E5.BD.951-JS-SDK.E4.BD.BF.E7.94.A8.E6.9D.83.E9.99.90.E7.AD.BE.E5.90.8D.E7.AE.97.E6.B3.95
     * </pre>
     *
     * @param forceRefresh 强制刷新
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public String getJsapiTicket(boolean forceRefresh) throws WxErrorException;

    /**
     *
     * @param forceRefresh
     * @param mpTag
     * @return
     * @throws WxErrorException
     */
    public String getJsapiTicket(boolean forceRefresh,String mpTag) throws WxErrorException;

    /**
     * <pre>
     * 创建调用jsapi时所需要的签名
     *
     * 详情请见：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html#.E9.99.84.E5.BD.951-JS-SDK.E4.BD.BF.E7.94.A8.E6.9D.83.E9.99.90.E7.AD.BE.E5.90.8D.E7.AE.97.E6.B3.95
     * </pre>
     *
     * @param url url
     * @return
     */
    public WxJsapiSignature createJsapiSignature(String url) throws WxErrorException;

    /**
     * <pre>
     * 上传多媒体文件
     *
     * 上传的多媒体文件有格式和大小限制，如下：
     *   图片（image）: 1M，支持JPG格式
     *   语音（voice）：2M，播放长度不超过60s，支持AMR\MP3格式
     *   视频（video）：10MB，支持MP4格式
     *   缩略图（thumb）：64KB，支持JPG格式
     *
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=上传下载多媒体文件
     * </pre>
     *
     * @param mediaType   媒体类型, 请看{@link com.daniel.weixin.common.util.WxConsts}
     * @param fileType    文件类型，请看{@link com.daniel.weixin.common.util.WxConsts}
     * @param inputStream 输入流
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public WxMediaUploadResult mediaUpload(String mediaType, String fileType, InputStream inputStream) throws WxErrorException, IOException;

    /**
     * @param mediaType
     * @param file
     * @throws com.daniel.weixin.common.exception.WxErrorException
     * @see #mediaUpload(String, String, java.io.InputStream)
     */
    public WxMediaUploadResult mediaUpload(String mediaType, File file) throws WxErrorException;

    /**
     * <pre>
     * 下载多媒体文件
     * 根据微信文档，视频文件下载不了，会返回null
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=上传下载多媒体文件
     * </pre>
     *
     * @return 保存到本地的临时文件
     * @throws com.daniel.weixin.common.exception.WxErrorException
     * @params media_id
     */
    public File mediaDownload(String media_id) throws WxErrorException;

    /**
     * <pre>
     * 发送客服消息
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=发送客服消息
     * </pre>
     *
     * @param message
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public void customMessageSend(com.daniel.weixin.mp.bean.WxMpCustomMessage message) throws WxErrorException;

    /**
     * <pre>
     * 上传群发用的图文消息，上传后才能群发图文消息
     *
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=高级群发接口
     * </pre>
     *
     * @param news
     * @throws com.daniel.weixin.common.exception.WxErrorException
     * @see #massGroupMessageSend(com.daniel.weixin.mp.bean.WxMpMassGroupMessage)
     * @see #massOpenIdsMessageSend(com.daniel.weixin.mp.bean.WxMpMassOpenIdsMessage)
     */
    public com.daniel.weixin.mp.bean.result.WxMpMassUploadResult massNewsUpload(com.daniel.weixin.mp.bean.WxMpMassNews news) throws WxErrorException;

    /**
     * <pre>
     * 上传群发用的视频，上传后才能群发视频消息
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=高级群发接口
     * </pre>
     *
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     * @see #massGroupMessageSend(com.daniel.weixin.mp.bean.WxMpMassGroupMessage)
     * @see #massOpenIdsMessageSend(com.daniel.weixin.mp.bean.WxMpMassOpenIdsMessage)
     */
    public com.daniel.weixin.mp.bean.result.WxMpMassUploadResult massVideoUpload(com.daniel.weixin.mp.bean.WxMpMassVideo video) throws WxErrorException;

    /**
     * <pre>
     * 分组群发消息
     * 如果发送图文消息，必须先使用 {@link #massNewsUpload(com.daniel.weixin.mp.bean.WxMpMassNews)} 获得media_id，然后再发送
     * 如果发送视频消息，必须先使用 {@link #massVideoUpload(com.daniel.weixin.mp.bean.WxMpMassVideo)} 获得media_id，然后再发送
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=高级群发接口
     * </pre>
     *
     * @param message
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public com.daniel.weixin.mp.bean.result.WxMpMassSendResult massGroupMessageSend(com.daniel.weixin.mp.bean.WxMpMassGroupMessage message) throws WxErrorException;

    /**
     * <pre>
     * 按openId列表群发消息
     * 如果发送图文消息，必须先使用 {@link #massNewsUpload(com.daniel.weixin.mp.bean.WxMpMassNews)} 获得media_id，然后再发送
     * 如果发送视频消息，必须先使用 {@link #massVideoUpload(com.daniel.weixin.mp.bean.WxMpMassVideo)} 获得media_id，然后再发送
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=高级群发接口
     * </pre>
     *
     * @param message
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public com.daniel.weixin.mp.bean.result.WxMpMassSendResult massOpenIdsMessageSend(com.daniel.weixin.mp.bean.WxMpMassOpenIdsMessage message) throws WxErrorException;

    /**
     * <pre>
     * 自定义菜单创建接口
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=自定义菜单创建接口
     * </pre>
     *
     * @param menu
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public void menuCreate(WxMenu menu) throws WxErrorException;

    /**
     * <pre>
     * 自定义菜单删除接口
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=自定义菜单删除接口
     * </pre>
     *
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public void menuDelete() throws WxErrorException;

    /**
     * <pre>
     * 自定义菜单查询接口
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=自定义菜单查询接口
     * </pre>
     *
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public WxMenu menuGet() throws WxErrorException;

    /**
     * <pre>
     * 分组管理接口 - 创建分组
     * 最多支持创建500个分组
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=分组管理接口
     * </pre>
     *
     * @param name 分组名字（30个字符以内）
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public com.daniel.weixin.mp.bean.WxMpGroup groupCreate(String name) throws WxErrorException;

    /**
     * <pre>
     * 分组管理接口 - 查询所有分组
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=分组管理接口
     * </pre>
     *
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public List<com.daniel.weixin.mp.bean.WxMpGroup> groupGet() throws WxErrorException;

    /**
     * <pre>
     * 分组管理接口 - 查询用户所在分组
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=分组管理接口
     * </pre>
     *
     * @param openid 微信用户的openid
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public long userGetGroup(String openid) throws WxErrorException;

    /**
     * <pre>
     * 分组管理接口 - 修改分组名
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=分组管理接口
     *
     * 如果id为0(未分组),1(黑名单),2(星标组)，或者不存在的id，微信会返回系统繁忙的错误
     * </pre>
     *
     * @param group 要更新的group，group的id,name必须设置
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public void groupUpdate(com.daniel.weixin.mp.bean.WxMpGroup group) throws WxErrorException;

    /**
     * <pre>
     * 分组管理接口 - 移动用户分组
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=分组管理接口
     *
     * 如果to_groupid为0(未分组),1(黑名单),2(星标组)，或者不存在的id，微信会返回系统繁忙的错误
     * </pre>
     *
     * @param openid     用户openid
     * @param to_groupid 移动到的分组id
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public void userUpdateGroup(String openid, long to_groupid) throws WxErrorException;

    /**
     * <pre>
     * 设置用户备注名接口
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=设置用户备注名接口
     * </pre>
     *
     * @param openid 用户openid
     * @param remark 备注名
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public void userUpdateRemark(String openid, String remark) throws WxErrorException;

    /**
     * <pre>
     * 获取用户基本信息
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=获取用户基本信息
     * </pre>
     *
     * @param openid 用户openid
     * @param lang   语言，zh_CN 简体(默认)，zh_TW 繁体，en 英语
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public com.daniel.weixin.mp.bean.result.WxMpUser userInfo(String openid, String lang) throws WxErrorException;

    /**
     * 获取用户基本信息
     * @param openid
     * @param lang
     * @param mpTag
     * @return
     * @throws WxErrorException
     */
    public com.daniel.weixin.mp.bean.result.WxMpUser userInfo(String openid, String lang,String mpTag) throws WxErrorException;

    /**
     * <pre>
     * 获取关注者列表
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=获取关注者列表
     * </pre>
     *
     * @param next_openid 可选，第一个拉取的OPENID，null为从头开始拉取
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public com.daniel.weixin.mp.bean.result.WxMpUserList userList(String next_openid) throws WxErrorException;

    /**
     * <pre>
     * 换取临时二维码ticket
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=生成带参数的二维码
     * </pre>
     *
     * @param scene_id       参数。
     * @param expire_seconds 过期秒数，默认60秒，最小60秒，最大1800秒
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public com.daniel.weixin.mp.bean.result.WxMpQrCodeTicket qrCodeCreateTmpTicket(int scene_id, Integer expire_seconds) throws WxErrorException;

    /**
     * <pre>
     * 换取永久二维码ticket
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=生成带参数的二维码
     * </pre>
     *
     * @param scene_id 参数。永久二维码时最大值为100000（目前参数只支持1--100000）
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public com.daniel.weixin.mp.bean.result.WxMpQrCodeTicket qrCodeCreateLastTicket(int scene_id) throws WxErrorException;

    /**
     * <pre>
     * 换取二维码图片文件，jpg格式
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=生成带参数的二维码
     * </pre>
     *
     * @param ticket 二维码ticket
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public File qrCodePicture(com.daniel.weixin.mp.bean.result.WxMpQrCodeTicket ticket) throws WxErrorException;

    /**
     * <pre>
     * 长链接转短链接接口
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=长链接转短链接接口
     * </pre>
     *
     * @param long_url
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public String shortUrl(String long_url) throws WxErrorException;

    /**
     * <pre>
     * 发送模板消息
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=模板消息接口
     * </pre>
     *
     * @param templateMessage
     * @return msgid
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public String templateSend(com.daniel.weixin.mp.bean.WxMpTemplateMessage templateMessage) throws WxErrorException;

    /**
     * <pre>
     * 语义查询接口
     * 详情请见：http://mp.weixin.qq.com/wiki/index.php?title=语义理解
     * </pre>
     *
     * @param semanticQuery
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    com.daniel.weixin.mp.bean.result.WxMpSemanticQueryResult semanticQuery(com.daniel.weixin.mp.bean.WxMpSemanticQuery semanticQuery) throws WxErrorException;

    /**
     * <pre>
     * 构造oauth2授权的url连接
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=网页授权获取用户基本信息
     * </pre>
     *
     * @param scope
     * @param state
     * @return code
     */
    public String oauth2buildAuthorizationUrl(String scope, String state);

    /**
     * <pre>
     * 用code换取oauth2的access token
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=网页授权获取用户基本信息
     * </pre>
     *
     * @param code
     * @return
     */
    public com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken oauth2getAccessToken(String code) throws WxErrorException;

    public com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken oauth2getAccessToken(String code,String mpTag) throws WxErrorException;

    /**
     * <pre>
     * 刷新oauth2的access token
     * </pre>
     *
     * @param refreshToken
     * @return
     */
    public com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken oauth2refreshAccessToken(String refreshToken) throws WxErrorException;

    public com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken oauth2refreshAccessToken(String refreshToken,String mpTag) throws WxErrorException;

    /**
     * <pre>
     * 用oauth2获取用户信息, 当前面引导授权时的scope是snsapi_userinfo的时候才可以
     * </pre>
     *
     * @param oAuth2AccessToken
     * @param lang              zh_CN, zh_TW, en
     */
    public com.daniel.weixin.mp.bean.result.WxMpUser oauth2getUserInfo(com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken oAuth2AccessToken, String lang) throws WxErrorException;

    public com.daniel.weixin.mp.bean.result.WxMpUser oauth2getUserInfo(com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken oAuth2AccessToken, String lang,String mpTag) throws WxErrorException;

    /**
     * <pre>
     * 验证oauth2的access token是否有效
     * </pre>
     *
     * @param oAuth2AccessToken
     * @return
     */
    public boolean oauth2validateAccessToken(com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken oAuth2AccessToken);

    public boolean oauth2validateAccessToken(com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken oAuth2AccessToken,String mpTag);

    /**
     * <pre>
     * 获取微信服务器IP地址
     * http://mp.weixin.qq.com/wiki/0/2ad4b6bfd29f30f71d39616c2a0fcedc.html
     * </pre>
     *
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    String[] getCallbackIP() throws WxErrorException;

    /**
     * <pre>
     * 获取用户增减数据
     * http://mp.weixin.qq.com/wiki/3/ecfed6e1a0a03b5f35e5efac98e864b7.html
     * </pre>
     *
     * @param beginDate 最大时间跨度7天
     * @param endDate   endDate不能早于begingDate
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    List<com.daniel.weixin.mp.bean.result.WxMpUserSummary> getUserSummary(Date beginDate, Date endDate) throws WxErrorException;

    /**
     * <pre>
     * 获取累计用户数据
     * http://mp.weixin.qq.com/wiki/3/ecfed6e1a0a03b5f35e5efac98e864b7.html
     * </pre>
     *
     * @param beginDate 最大时间跨度7天
     * @param endDate   endDate不能早于begingDate
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    List<com.daniel.weixin.mp.bean.result.WxMpUserCumulate> getUserCumulate(Date beginDate, Date endDate) throws WxErrorException;

    /**
     * 当本Service没有实现某个API的时候，可以用这个，针对所有微信API中的GET请求
     *
     * @param url
     * @param queryParam
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    String get(String url, String queryParam) throws WxErrorException;

    /**
     * 当本Service没有实现某个API的时候，可以用这个，针对所有微信API中的POST请求
     *
     * @param url
     * @param postData
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    String post(String url, String postData) throws WxErrorException;

    /**
     * <pre>
     * Service没有实现某个API的时候，可以用这个，
     * 比{@link #get}和{@link #post}方法更灵活，可以自己构造RequestExecutor用来处理不同的参数和不同的返回类型。
     * 可以参考，{@link com.daniel.weixin.common.util.http.MediaUploadRequestExecutor}的实现方法
     * </pre>
     *
     * @param executor
     * @param uri
     * @param data
     * @param <T>
     * @param <E>
     * @return
     * @throws com.daniel.weixin.common.exception.WxErrorException
     */
    public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws WxErrorException;

    public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data,String mpTag) throws WxErrorException;

    /**
     * 注入 {@link com.daniel.weixin.mp.api.WxMpConfigStorage} 的实现
     *
     * @param wxConfigProvider
     */
    public void setWxMpConfigStorage(com.daniel.weixin.mp.api.WxMpConfigStorage wxConfigProvider);

    /**
     * <pre>
     * 设置当微信系统响应系统繁忙时，要等待多少 retrySleepMillis(ms) * 2^(重试次数 - 1) 再发起重试
     * 默认：1000ms
     * </pre>
     *
     * @param retrySleepMillis
     */
    void setRetrySleepMillis(int retrySleepMillis);

    /**
     * <pre>
     * 设置当微信系统响应系统繁忙时，最大重试次数
     * 默认：5次
     * </pre>
     *
     * @param maxRetryTimes
     */
    void setMaxRetryTimes(int maxRetryTimes);

}
