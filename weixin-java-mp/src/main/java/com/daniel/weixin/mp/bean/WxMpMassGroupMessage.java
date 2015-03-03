package com.daniel.weixin.mp.bean;

import java.io.Serializable;

/**
 * 分组群发的消息
 * 
 * @author chanjarster/danielyang
 */
public class WxMpMassGroupMessage implements Serializable {
  
  private Long groupId;
  private String msgtype;
  private String content;
  private String mediaId;

  public WxMpMassGroupMessage() {
    super();
  }
  
  public String getMsgtype() {
    return msgtype;
  }

  /**
   * <pre>
   * 请使用
   * {@link com.daniel.weixin.common.api.WxConsts#MASS_MSG_IMAGE}
   * {@link com.daniel.weixin.common.api.WxConsts#MASS_MSG_NEWS}
   * {@link com.daniel.weixin.common.api.WxConsts#MASS_MSG_TEXT}
   * {@link com.daniel.weixin.common.api.WxConsts#MASS_MSG_VIDEO}
   * {@link com.daniel.weixin.common.api.WxConsts#MASS_MSG_VOICE}
   * 如果msgtype和media_id不匹配的话，会返回系统繁忙的错误
   * </pre>
   * @param msgtype
   */
  public void setMsgtype(String msgtype) {
    this.msgtype = msgtype;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getMediaId() {
    return mediaId;
  }

  public void setMediaId(String mediaId) {
    this.mediaId = mediaId;
  }

  public String toJson() {
    return com.daniel.weixin.mp.util.json.WxMpGsonBuilder.INSTANCE.create().toJson(this);
  }

  public Long getGroupId() {
    return groupId;
  }

  /**
   * 如果不设置则就意味着发给所有用户
   * @param groupId
   */
  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

}
