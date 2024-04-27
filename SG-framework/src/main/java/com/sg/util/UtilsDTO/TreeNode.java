package com.sg.util.UtilsDTO;

import java.util.List;

/**
 * @author qi_coding
 * @version 1.00
 * @time 2024/4/25 16:31
 */
public interface TreeNode<T> {
    Long getId(); // 获取当前节点的ID

    Long getParentId(); // 获取父节点的ID

    List<T> getChildren(); // 获取子节点的列表

    void setChildren(List<T> children); // 设置子节点的列表
}
