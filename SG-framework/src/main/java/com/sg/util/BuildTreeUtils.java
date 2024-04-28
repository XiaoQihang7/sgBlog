package com.sg.util;

import com.sg.util.UtilsDTO.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qi_coding
 * @version 1.00
 * @time 2024/4/25 16:30
 */
public class BuildTreeUtils {

    //将上述方法封装为一个通用的方法
    //使用一个接口进行抽象，适配
    //todo 分析这个泛型方法有没有什么隐患，可否优化
    public static <T extends TreeNode<T>> List<T> buildTree(List<T> items) {
        // 1、将得到的所有信息存入map中便于存、取
        Map<Long, T> itemMap = items.stream().collect(Collectors.toMap(TreeNode::getId, item -> item));
        // 2、构建树结构
        List<T> tree = new ArrayList<>();
        for (T item : items) {
            Long parentId = item.getParentId();
            if (parentId.equals(0L)) {
                // 如果是顶层节点，则直接添加到树中
                tree.add(item);
            } else {
                T parentItem = itemMap.get(parentId);
                if (parentItem != null) {
                    if (parentItem.getChildren() == null) {
                        parentItem.setChildren(new ArrayList<>());
                    }
                    parentItem.getChildren().add(item);
                }
            }
        }
        return tree;
    }
}
