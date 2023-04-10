package com.atguigu.vodtest;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import org.junit.Test;

import java.util.List;

public class TestVod {
    //根据视频id获取视频播放地址
    @Test
    public void getPlayUrl() throws Exception {
        //1.创建初始化对象
        DefaultAcsClient client =InitObject.initVodClient(
                                        "LTAI5tMUCkxmE6ouUc2dmbXm",
                                        "0Py10jHOPVkeFp6MiIm88c9QqyykUE");

        //2.创建获取视频地址的request和response
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();

        //3.向request对象里面设置视频id(必须是没有加密的视频id)
        request.setVideoId("7218f0acdf4f4a76950d509fb1d18f51");

        //4.调用初始化对象里面的方法,获取视频信息
        response = client.getAcsResponse(request);

        //5.从获取到的视频信息中取两个信息:播放地址和视频名称
        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.println("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
        //Base信息
        System.out.println("VideoBase.Title = " + response.getVideoBase().getTitle()+"\n");
    }

    //根据视频id获取视频凭证
    @Test
    public void getPlayAuth() throws Exception {
        //1.创建初始化对象
        DefaultAcsClient client =InitObject.initVodClient(
                "LTAI5tMUCkxmE6ouUc2dmbXm",
                "0Py10jHOPVkeFp6MiIm88c9QqyykUE");

        //2.创建获取视频凭证的request和response
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

        //3.向request对象里面设置视频id(加密视频id、没有加密视频的id都是可以的)
        request.setVideoId("efae65041e034f67b8e990ad20c8f2d6");

        //4.调用初始化对象里面的方法,获取视频信息
        response = client.getAcsResponse(request);

        //5.从获取到的视频信息中取视频凭证的信息
        System.out.println("playauth: " + response.getPlayAuth());
    }

    //本地视频上传
    @Test
    public void uploadVideo() {
        String accessKeyId = "LTAI5tMUCkxmE6ouUc2dmbXm";
        String accessKeySecret = "0Py10jHOPVkeFp6MiIm88c9QqyykUE";
        String title = "6 - What If I Want to Move Faster -- upload by sdk"; //上传后文件的名称
        String fileName = "C:/Users/mxy/Desktop/6 - What If I Want to Move Faster.mp4"; //本地文件的路径和名称

        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }
}
