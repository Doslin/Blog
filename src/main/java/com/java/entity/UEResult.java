package com.java.entity;

public class UEResult {
    private String title;    //������Ҫ�����ļ�������
    private String original;    //������Ҫ�����ļ�������
    private String state;    //�ϴ�״̬ SUCCESS�����д
    private String url; //ͼƬ��url
    private String code;

    public UEResult() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

