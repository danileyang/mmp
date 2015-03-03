package com.daniel.weixin.mp.bean.outxmlbuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 图文消息builder
 * @author chanjarster/danielyang
 */
public final class NewsBuilder extends com.daniel.weixin.mp.bean.outxmlbuilder.BaseBuilder<NewsBuilder, com.daniel.weixin.mp.bean.WxMpXmlOutNewsMessage> {

  protected final List<com.daniel.weixin.mp.bean.WxMpXmlOutNewsMessage.Item> articles = new ArrayList<com.daniel.weixin.mp.bean.WxMpXmlOutNewsMessage.Item>();
  
  public NewsBuilder addArticle(com.daniel.weixin.mp.bean.WxMpXmlOutNewsMessage.Item item) {
    this.articles.add(item);
    return this;
  }
  
  public com.daniel.weixin.mp.bean.WxMpXmlOutNewsMessage build() {
    com.daniel.weixin.mp.bean.WxMpXmlOutNewsMessage m = new com.daniel.weixin.mp.bean.WxMpXmlOutNewsMessage();
    for(com.daniel.weixin.mp.bean.WxMpXmlOutNewsMessage.Item item : articles) {
      m.addArticle(item);
    }
    setCommon(m);
    return m;
  }
  
}
