package com.sg.controller;

import com.baomidou.mybatisplus.core.incrementer.ImadcnIdentifierGenerator;
import com.sg.domain.ResponseResult;
import com.sg.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    UploadService uploadService;

    @PostMapping
    public ResponseResult uploadImg(@RequestParam("img") MultipartFile file){ //这个参数是区分大小写的
        try {
            return uploadService.uploadImg(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传上传失败");
        }
    }
}
