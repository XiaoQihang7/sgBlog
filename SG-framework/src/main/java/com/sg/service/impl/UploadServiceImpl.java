package com.sg.service.impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.sg.Exception.SystemException;
import com.sg.domain.ResponseResult;
import com.sg.domain.enums.AppHttpCodeEnum;
import com.sg.service.UploadService;
import com.sg.util.PathUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


@Service
@Data
@ConfigurationProperties(prefix = "oss")
public class UploadServiceImpl implements UploadService {

    private String accessKey;
    private String secretKey;
    private String bucket;

    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        //获取前端传来的文件
        String originalFilename = img.getOriginalFilename();
        //统一存放文件的格式
        if (!originalFilename.endsWith(".png")&&!originalFilename.endsWith(".jpg")){
           throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        //将这个文件创建新的文件名和存放目录
        String filePathName = PathUtils.generateFilePath(originalFilename);
        //上传至OSS，并返回一个图片所在的链接
        String url = uploadOss(img, filePathName);

        return ResponseResult.okResult(url);
    }

    private String uploadOss(MultipartFile imgFile,String fileName){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名,上传到OSS的文件名
        String key = fileName;
        try {
            //要上传的文件名
            InputStream inputStream = imgFile.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return "http://rxnxtx2ne.hn-bkt.clouddn.com/"+key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "www";
    }
}
