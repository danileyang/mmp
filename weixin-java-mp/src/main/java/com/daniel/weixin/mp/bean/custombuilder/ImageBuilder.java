package com.daniel.weixin.mp.bean.custombuilder;

import com.daniel.weixin.common.api.WxConsts;

/**
 * 获得消息builder
 * <pre>
 * 用法: WxMpCustomMessage m = WxMpCustomMessage.IMAGE().mediaId(...).toUser(...).build();
 * </pre>
 * @author chanjarster/danielyang
 *
 */
public final class ImageBuilder extends com.daniel.weixin.mp.bean.custombuilder.BaseBuilder<ImageBuilder> {
  private String mediaId;

  public ImageBuilder() {
    this.msgType = WxConsts.CUSTOM_MSG_IMAGE;
  }

  public ImageBuilder mediaId(String media_id) {
    this.mediaId = media_id;
    return this;
  }

  public com.daniel.weixin.mp.bean.WxMpCustomMessage build() {
    com.daniel.weixin.mp.bean.WxMpCustomMessage m = super.build();
    m.setMediaId(this.mediaId);
    return m;
  }
}
