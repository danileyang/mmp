package com.daniel.weixin.mp.bean.outxmlbuilder;

/**
 * 视频消息builder
 * @author chanjarster/danielyang
 *
 */
public final class VideoBuilder extends com.daniel.weixin.mp.bean.outxmlbuilder.BaseBuilder<VideoBuilder, com.daniel.weixin.mp.bean.WxMpXmlOutVideoMessage> {

  private String mediaId;
  private String title;
  private String description;

  public VideoBuilder title(String title) {
    this.title = title;
    return this;
  }
  public VideoBuilder description(String description) {
    this.description = description;
    return this;
  }
  public VideoBuilder mediaId(String mediaId) {
    this.mediaId = mediaId;
    return this;
  }
  
  public com.daniel.weixin.mp.bean.WxMpXmlOutVideoMessage build() {
    com.daniel.weixin.mp.bean.WxMpXmlOutVideoMessage m = new com.daniel.weixin.mp.bean.WxMpXmlOutVideoMessage();
    setCommon(m);
    m.setTitle(title);
    m.setDescription(description);
    m.setMediaId(mediaId);
    return m;
  }
  
}
