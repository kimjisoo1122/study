package com.study.servlet;

import java.util.HashMap;
import java.util.Map;

public class MyModelAndView {
    private String logicalViewPath;
    private final Map<String, Object> model = new HashMap<>();

    public Map<String, Object> getModel() {
        return model;
    }

    public String getLogicalViewPath() {
        return logicalViewPath;
    }

    public void setLogicalViewPath(String logicalViewPath) {
        this.logicalViewPath = logicalViewPath;
    }
}
