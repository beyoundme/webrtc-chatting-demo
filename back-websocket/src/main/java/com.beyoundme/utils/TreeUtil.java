package com.beyoundme.utils;


import com.beyoundme.bean.Depart;
import com.beyoundme.bean.TreeOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TreeUtil {

    public static List<TreeOptions> getOrgsTree(List<Depart> depts) {
        final int[] count = {0};
        List<TreeOptions> treeList = depts.stream()
                .filter(dept -> !dept.getUuid().equals(dept.getOrgRelation()))
                .map(dept -> {
                    TreeOptions node = new TreeOptions();
                    node.setCurenetId(dept.getUuid());
                    node.setId(count[0]++);
                    node.setParentId(dept.getOrgRelation());
                    node.setLabel(dept.getOrgName());
                    return node;
                }).collect(Collectors.toList());
        return TreeUtil.buildByRecursive(treeList, "");
    }

    public static <T extends TreeOptions> List<T> bulid(List<T> treeNodes, Object root) {
        List<T> trees = new ArrayList<>();
        for (T treeNode : treeNodes) {
            if (root.equals(treeNode.getParentId())) {
                trees.add(treeNode);
            }
            for (T it : treeNodes) {
                if (it.getParentId() == treeNode.getCurenetId()) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<>());
                    }
                    treeNode.add(it);
                }
            }
        }
        return trees;
    }

    public static <T extends TreeOptions> List<T> buildByRecursive(List<T> treeNodes, Object root) {
        List<T> trees = new ArrayList<T>();
        for (T treeNode : treeNodes) {
            if (root.equals(treeNode.getParentId())) {
                trees.add(findChildren(treeNode, treeNodes));
            }
        }
       return trees;
   }

    public static  <T extends TreeOptions> T findChildren(T treeNode, List<T> treeNodes) {
        for (T it : treeNodes) {
            if (treeNode.getCurenetId().equals(it.getParentId())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.add(findChildren(it, treeNodes));
            }
        }
        return treeNode;
    }
}
