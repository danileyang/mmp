package com.daniel.weixin.cp.bean.outxmlbuilder;

import com.daniel.weixin.cp.bean.WxCpXmlOutNewsMessage;
import com.daniel.weixin.cp.bean.WxCpXmlOutNewsMessage.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * 图文消息builder
 * @author Daniel Qian
 */
public final class NewsBuilder extends BaseBuilder<NewsBuilder, WxCpXmlOutNewsMessage> {

  protected final List<Item> articles = new ArrayList<Item>();
  
  public NewsBuilder addArticle(Item item) {
    this.articles.add(item);
    return this;
  }
  
  public WxCpXmlOutNewsMessage build() {
    WxCpXmlOutNewsMessage m = new WxCpXmlOutNewsMessage();
    for(Item item : articles) {
      m.addArticle(item);
    }
    setCommon(m);
    return m;
  }
  
}
