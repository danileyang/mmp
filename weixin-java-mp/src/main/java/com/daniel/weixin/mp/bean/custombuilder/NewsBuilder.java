package com.daniel.weixin.mp.bean.custombuilder;

import com.daniel.weixin.common.util.WxConsts;

import java.util.ArrayList;
import java.util.List;

/**
 * 图文消息builder
 * <pre>
 * 用法:
 * WxMpCustomMessage m = WxMpCustomMessage.NEWS().addArticle(article).toUser(...).build();
 * </pre>
 * @author chanjarster/danielyang
 *
 */
public final class NewsBuilder extends com.daniel.weixin.mp.bean.custombuilder.BaseBuilder<NewsBuilder> {

  private List<com.daniel.weixin.mp.bean.WxMpCustomMessage.WxArticle> articles = new ArrayList<com.daniel.weixin.mp.bean.WxMpCustomMessage.WxArticle>();
  
  public NewsBuilder() {
    this.msgType = WxConsts.CUSTOM_MSG_NEWS;
  }

  public NewsBuilder addArticle(com.daniel.weixin.mp.bean.WxMpCustomMessage.WxArticle article) {
    this.articles.add(article);
    return this;
  }

  public com.daniel.weixin.mp.bean.WxMpCustomMessage build() {
    com.daniel.weixin.mp.bean.WxMpCustomMessage m = super.build();
    m.setArticles(this.articles);
    return m;
  }
}
