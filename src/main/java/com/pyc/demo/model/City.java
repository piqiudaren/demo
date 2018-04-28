package com.pyc.demo.model;

public class City {
    private Long id;

    private String code;

    private String name;

    private String url;

    private Long parentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "Province{" +
                "id=" + id +
                ", code=" + code +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}
