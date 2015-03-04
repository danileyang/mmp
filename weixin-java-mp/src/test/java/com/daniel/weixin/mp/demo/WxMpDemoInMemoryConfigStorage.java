package com.daniel.weixin.mp.demo;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.daniel.weixin.common.util.xml.XStreamInitializer;
import com.daniel.weixin.mp.api.WxMpInMemoryConfigStorage;

import java.io.InputStream;

/**
 * @author Daniel Qian
 */
@XStreamAlias("xml")
class WxMpDemoInMemoryConfigStorage extends WxMpInMemoryConfigStorage {

    @Override
    public String toString() {
        return "SimpleWxConfigProvider [appId=" + appId + ", secret=" + secret + ", accessToken=" + accessToken
                + ", expiresTime=" + expiresTime + ", token=" + token + ", aesKey=" + aesKey + "]";
    }


    public static WxMpDemoInMemoryConfigStorage fromXml(InputStream is) {
        XStream xstream = XStreamInitializer.getInstance();
        xstream.processAnnotations(WxMpDemoInMemoryConfigStorage.class);
        return (WxMpDemoInMemoryConfigStorage) xstream.fromXML(is);
    }

    @Override
    public void setAppId(String mpTag, boolean multi) {
        //TODO:impl
    }

    @Override
    public void setSecret(String mpTag, boolean multi) {
        //TODO:impl
    }

    @Override
    public void setToken(String mpTag, boolean multi) {
        //TODO:impl
    }

    @Override
    public void setAesKey(String mpTag, boolean multi) {
        //TODO:impl
    }

    @Override
    public void setExpiresTime(String mpTag, boolean multi) {
        //TODO:impl
    }
}
