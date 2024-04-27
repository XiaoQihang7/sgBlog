package com.sg.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PathUtils {
    public static String generateFilePath(String fileName){
        //根据日期来做目录，存放文件
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String datePath = simpleDateFormat.format(new Date());
        //获取一个uuid作为文件名，防止重名
        String uuid = UUID.randomUUID().toString().replace("-", "");
        //获取文件类型
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        //拼接文件名返回
        return datePath + uuid + fileType;
    }
}
