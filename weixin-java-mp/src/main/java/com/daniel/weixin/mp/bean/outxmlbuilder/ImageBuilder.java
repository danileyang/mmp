package com.daniel.weixin.mp.bean.outxmlbuilder;

/**
 * 图片消息builder
 * @author chanjarster/danielyang
 */
public final class ImageBuilder extends com.daniel.weixin.mp.bean.outxmlbuilder.BaseBuilder<ImageBuilder, com.daniel.weixin.mp.bean.WxMpXmlOutImageMessage> {

  private String mediaId;

  public ImageBuilder mediaId(String media_id) {
    this.mediaId = media_id;
    return this;
  }

  public com.daniel.weixin.mp.bean.WxMpXmlOutImageMessage build() {
    com.daniel.weixin.mp.bean.WxMpXmlOutImageMessage m = new com.daniel.weixin.mp.bean.WxMpXmlOutImageMessage();
    setCommon(m);
    m.setMediaId(this.mediaId);
    return m;
  }
  
}
