package com.sg.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.sg.domain.ResponseResult;
import com.sg.domain.entity.Category;
import com.sg.domain.enums.AppHttpCodeEnum;
import com.sg.domain.vo.CategoryVo;
import com.sg.domain.vo.PageVo;
import com.sg.service.CategoryService;
import com.sg.util.BeanCopyUtils;
import com.sg.util.WebUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;


    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        //直接复用前台服务接口,no，这个分类是给新写的博文提供分类的，需要查询所有
        List<CategoryVo> categoryVos = categoryService.listAllCategory();
        return ResponseResult.okResult(categoryVos);
    }

    @GetMapping("/list")
    public ResponseResult listAll(Integer pageNum , Integer pageSize , CategoryVo categoryVo){
        //直接复用前台服务接口,no，这个分类是给新写的博文提供分类的，需要查询所有
        PageVo categorys = categoryService.listAll(pageNum , pageSize , categoryVo);
        return ResponseResult.okResult(categorys);
    }


    @GetMapping("/export")
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    public void export(HttpServletResponse response){
        try {
            WebUtils.setDownLoadHeader("分类",response);
            List<Category> list = categoryService.list();
            List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);

            //把数据写入Excel中
            EasyExcel.write(response.getOutputStream(),CategoryVo.class).autoCloseStream(Boolean.FALSE)
                    .sheet("分类导出").doWrite(categoryVos);
        } catch (Exception e) {
                //如果出现异常也要响应json
                ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
                WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
}
