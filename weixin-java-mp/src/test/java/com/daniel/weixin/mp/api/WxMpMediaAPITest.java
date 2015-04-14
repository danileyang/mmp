package com.daniel.weixin.mp.api;

import org.testng.annotations.Guice;
import org.testng.annotations.Test;

/**
 * 测试多媒体文件上传下载
 * @author chanjarster/danielyang
 *
 */
@Test(groups="mediaAPI", dependsOnGroups="baseAPI")
@Guice(modules = ApiTestModule.class)
public class WxMpMediaAPITest {

//  @Inject
//  protected WxMpServiceImpl wxService;
//
//  private List<String> media_ids = new ArrayList<String>();
//
//  @Test(dataProvider="uploadMedia")
//  public void testUploadMedia(String mediaType, String fileType, String fileName) throws WxErrorException, IOException {
//    InputStream inputStream = ClassLoader.getSystemResourceAsStream(fileName);
//    WxMediaUploadResult res = wxService.mediaUpload(mediaType, fileType, inputStream);
//    Assert.assertNotNull(res.getType());
//    Assert.assertNotNull(res.getCreatedAt());
//    Assert.assertTrue(res.getMediaId() != null || res.getThumbMediaId() != null);
//
//    if (res.getMediaId() != null) {
//      media_ids.add(res.getMediaId());
//    }
//    if (res.getThumbMediaId() != null) {
//      media_ids.add(res.getThumbMediaId());
//    }
//  }
//
//  @DataProvider
//  public Object[][] uploadMedia() {
//    return new Object[][] {
//        new Object[] { WxConsts.MEDIA_IMAGE, WxConsts.FILE_JPG, "mm.jpeg" },
//        new Object[] { WxConsts.MEDIA_VOICE, WxConsts.FILE_MP3, "mm.mp3" },
//        new Object[] { WxConsts.MEDIA_VIDEO, WxConsts.FILE_MP4, "mm.mp4" },
//        new Object[] { WxConsts.MEDIA_THUMB, WxConsts.FILE_JPG, "mm.jpeg" }
//    };
//  }
//
//  @Test(dependsOnMethods = { "testUploadMedia" }, dataProvider="downloadMedia")
//  public void testDownloadMedia(String media_id) throws WxErrorException {
//    wxService.mediaDownload(media_id);
//  }
//
//  @DataProvider
//  public Object[][] downloadMedia() {
//    Object[][] params = new Object[this.media_ids.size()][];
//    for (int i = 0; i < this.media_ids.size(); i++) {
//      params[i] = new Object[] { this.media_ids.get(i) };
//    }
//    return params;
//  }
//
}
