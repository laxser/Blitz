package com.laxser.blitz.web.impl.mapping.ignored;

import com.laxser.blitz.web.RequestPath;
/**
 * 
 * @author laxser
 * @ contact qifan.du@renren-inc.com
 * date: 2012-4-5
 */
public class IgnoredPathStarts implements IgnoredPath {

    private String path;

    public IgnoredPathStarts(String path) {
        this.path = path;
    }

    @Override
    public boolean hit(RequestPath requestPath) {
        return requestPath.getRosePath().startsWith(path);
    }
}