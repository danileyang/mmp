package com.daniel.weixin.mp.util.xml;

import com.thoughtworks.xstream.XStream;
import com.daniel.weixin.common.util.xml.XStreamInitializer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class XStreamTransformer {

  protected static final Map<Class, XStream> CLASS_2_XSTREAM_INSTANCE = configXStreamInstance();

  /**
   * xml -> pojo
   *
   * @param clazz
   * @param xml
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> T fromXml(Class<T> clazz, String xml) {
    T object = (T) CLASS_2_XSTREAM_INSTANCE.get(clazz).fromXML(xml);
    return object;
  }

  @SuppressWarnings("unchecked")
  public static <T> T fromXml(Class<T> clazz, InputStream is) {
    T object = (T) CLASS_2_XSTREAM_INSTANCE.get(clazz).fromXML(is);
    return object;
  }

  /**
   * pojo -> xml
   *
   * @param clazz
   * @param object
   * @return
   */
  public static <T> String toXml(Class<T> clazz, T object) {
    return CLASS_2_XSTREAM_INSTANCE.get(clazz).toXML(object);
  }

  private static Map<Class, XStream> configXStreamInstance() {
    Map<Class, XStream> map = new HashMap<Class, XStream>();
    map.put(com.daniel.weixin.mp.bean.WxMpXmlMessage.class, config_WxMpXmlMessage());
    map.put(com.daniel.weixin.mp.bean.WxMpXmlOutMusicMessage.class, config_WxMpXmlOutMusicMessage());
    map.put(com.daniel.weixin.mp.bean.WxMpXmlOutNewsMessage.class, config_WxMpXmlOutNewsMessage());
    map.put(com.daniel.weixin.mp.bean.WxMpXmlOutTextMessage.class, config_WxMpXmlOutTextMessage());
    map.put(com.daniel.weixin.mp.bean.WxMpXmlOutImageMessage.class, config_WxMpXmlOutImageMessage());
    map.put(com.daniel.weixin.mp.bean.WxMpXmlOutVideoMessage.class, config_WxMpXmlOutVideoMessage());
    map.put(com.daniel.weixin.mp.bean.WxMpXmlOutVoiceMessage.class, config_WxMpXmlOutVoiceMessage());
    return map;
  }

  private static XStream config_WxMpXmlMessage() {
    XStream xstream = XStreamInitializer.getInstance();
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlMessage.class);
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlMessage.ScanCodeInfo.class);
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlMessage.SendPicsInfo.class);
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlMessage.SendPicsInfo.Item.class);
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlMessage.SendLocationInfo.class);

    xstream.aliasField("MsgID", com.daniel.weixin.mp.bean.WxMpXmlMessage.class, "msgId");
    return xstream;
  }

  private static XStream config_WxMpXmlOutImageMessage() {
    XStream xstream = XStreamInitializer.getInstance();
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlOutMessage.class);
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlOutImageMessage.class);
    return xstream;
  }

  private static XStream config_WxMpXmlOutNewsMessage() {
    XStream xstream = XStreamInitializer.getInstance();
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlOutMessage.class);
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlOutNewsMessage.class);
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlOutNewsMessage.Item.class);
    return xstream;
  }

  private static XStream config_WxMpXmlOutMusicMessage() {
    XStream xstream = XStreamInitializer.getInstance();
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlOutMessage.class);
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlOutMusicMessage.class);
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlOutMusicMessage.Music.class);
    return xstream;
  }

  private static XStream config_WxMpXmlOutTextMessage() {
    XStream xstream = XStreamInitializer.getInstance();
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlOutMessage.class);
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlOutTextMessage.class);
    return xstream;
  }

  private static XStream config_WxMpXmlOutVideoMessage() {
    XStream xstream = XStreamInitializer.getInstance();
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlOutMessage.class);
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlOutVideoMessage.class);
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlOutVideoMessage.Video.class);
    return xstream;
  }

  private static XStream config_WxMpXmlOutVoiceMessage() {
    XStream xstream = XStreamInitializer.getInstance();
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlOutMessage.class);
    xstream.processAnnotations(com.daniel.weixin.mp.bean.WxMpXmlOutVoiceMessage.class);
    return xstream;
  }

}
