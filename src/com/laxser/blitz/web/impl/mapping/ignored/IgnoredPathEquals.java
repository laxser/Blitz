package com.laxser.blitz.web.impl.mapping.ignored;

import com.laxser.blitz.web.RequestPath;


public class IgnoredPathEquals implements IgnoredPath {

    private String path;

    public IgnoredPathEquals(String path) {
        this.path = path;
    }

    @Override
    public boolean hit(RequestPath requestPath) {
        return requestPath.getRosePath().equals(path);
    }
}

