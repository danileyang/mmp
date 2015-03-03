package com.daniel.weixin.mp.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.daniel.weixin.common.util.xml.XStreamCDataConverter;

import java.io.Serializable;

@XStreamAlias("xml")
public abstract class WxMpXmlOutMessage implements Serializable {

  @XStreamAlias("ToUserName")
  @XStreamConverter(value=XStreamCDataConverter.class)
  protected String toUserName;
  
  @XStreamAlias("FromUserName")
  @XStreamConverter(value=XStreamCDataConverter.class)
  protected String fromUserName;
  
  @XStreamAlias("CreateTime")
  protected Long createTime;
  
  @XStreamAlias("MsgType")
  @XStreamConverter(value=XStreamCDataConverter.class)
  protected String msgType;

  public String getToUserName() {
    return toUserName;
  }

  public void setToUserName(String toUserName) {
    this.toUserName = toUserName;
  }

  public String getFromUserName() {
    return fromUserName;
  }

  public void setFromUserName(String fromUserName) {
    this.fromUserName = fromUserName;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }

  public String getMsgType() {
    return msgType;
  }

  public void setMsgType(String msgType) {
    this.msgType = msgType;
  }
  
  public String toXml() {
    return com.daniel.weixin.mp.util.xml.XStreamTransformer.toXml((Class) this.getClass(), this);
  }

  /**
   * 转换成加密的xml格式
   * @return
   */
  public String toEncryptedXml(com.daniel.weixin.mp.api.WxMpConfigStorage wxMpConfigStorage) {
    String plainXml = toXml();
    com.daniel.weixin.mp.util.crypto.WxMpCryptUtil pc = new com.daniel.weixin.mp.util.crypto.WxMpCryptUtil(wxMpConfigStorage);
    return pc.encrypt(plainXml);
  }

  /**
   * 获得文本消息builder
   * @return
   */
  public static com.daniel.weixin.mp.bean.outxmlbuilder.TextBuilder TEXT() {
    return new com.daniel.weixin.mp.bean.outxmlbuilder.TextBuilder();
  }

  /**
   * 获得图片消息builder
   * @return
   */
  public static com.daniel.weixin.mp.bean.outxmlbuilder.ImageBuilder IMAGE() {
    return new com.daniel.weixin.mp.bean.outxmlbuilder.ImageBuilder();
  }

  /**
   * 获得语音消息builder
   * @return
   */
  public static com.daniel.weixin.mp.bean.outxmlbuilder.VoiceBuilder VOICE() {
    return new com.daniel.weixin.mp.bean.outxmlbuilder.VoiceBuilder();
  }
  
  /**
   * 获得视频消息builder
   * @return
   */
  public static com.daniel.weixin.mp.bean.outxmlbuilder.VideoBuilder VIDEO() {
    return new com.daniel.weixin.mp.bean.outxmlbuilder.VideoBuilder();
  }
  
  /**
   * 获得音乐消息builder
   * @return
   */
  public static com.daniel.weixin.mp.bean.outxmlbuilder.MusicBuilder MUSIC() {
    return new com.daniel.weixin.mp.bean.outxmlbuilder.MusicBuilder();
  }
  
  /**
   * 获得图文消息builder
   * @return
   */
  public static com.daniel.weixin.mp.bean.outxmlbuilder.NewsBuilder NEWS() {
    return new com.daniel.weixin.mp.bean.outxmlbuilder.NewsBuilder();
  }
}
