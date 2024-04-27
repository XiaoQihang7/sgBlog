package com.sg.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.sg.util.UtilsDTO.TreeNode;
import lombok.Data;

import java.util.List;

@Data
public class MenuVo implements TreeNode<MenuVo> {
    private Long id;
    private String label; // menu_name 映射为 label
    private Long parentId;

    @TableField(exist = false)
    private List<MenuVo> children;

    private List<String> checkedKeys;

    // 省略构造函数、getter 和 setter 方法
}
