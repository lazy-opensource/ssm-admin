package com.lzy.innovate.utils.tree;

import com.lzy.innovate.utils.Sets;

import java.util.List;

/**
 * Created by lzy on 2017/3/19.
 * 前台菜单树
 */
public class BootstrapTreeView {

    private String uuid;
    private String text;
    private List<BootstrapTreeView> nodes = Sets.list();
    private String icon;
    private String href;
    private String color;
    private String backColor;
    private TreeState state = new TreeState();
    private String[] tags = {};
    private String pid;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<BootstrapTreeView> getNodes() {
        return nodes;
    }

    public void setNodes(List<BootstrapTreeView> nodes) {
        this.nodes = nodes;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        if (icon == null){
            icon = "";
        }
        this.icon = icon;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        if (href == null){
            href = "";
        }
        this.href = href;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {

        if (color == null){
            color = "";
        }
        this.color = color;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {

        if (backColor == null){
            backColor = "";
        }
        this.backColor = backColor;
    }

    public TreeState getState() {
        return state;
    }

    public void setState(TreeState state) {
        if (state == null){
            state = new TreeState();
        }
        this.state = state;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        if (tags == null){
            tags = new String[]{};
        }
        this.tags = tags;
    }
}
