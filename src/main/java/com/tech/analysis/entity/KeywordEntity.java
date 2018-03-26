package com.tech.analysis.entity;
import java.io.Serializable;
/**
 * Created by XCY on 2018/3/26.
 */
public class KeywordEntity implements Serializable {

    private static final long serialVersionUID = 1269373329410167403l;
    private String name;
    private Long id;

    public KeywordEntity(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
