package com.laxser.blitz.web.impl.mapping.ignored;

import com.laxser.blitz.web.RequestPath;
/**
 * 配置忽略的path
 * @author laxser
 * @ contact qifan.du@renren-inc.com
 * date: 2012-4-5
 */
public interface IgnoredPath {

    public boolean hit(RequestPath requestPath);
}
