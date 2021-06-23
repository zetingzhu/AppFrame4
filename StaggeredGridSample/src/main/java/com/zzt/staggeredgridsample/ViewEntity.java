package com.zzt.staggeredgridsample;

/**
 * @author: zeting
 * @date: 2021/5/6
 */
public class ViewEntity {
    private String content;
    private int type ;

    public ViewEntity(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

