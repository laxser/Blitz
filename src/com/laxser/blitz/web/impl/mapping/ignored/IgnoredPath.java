package com.laxser.blitz.web.impl.mapping.ignored;

import com.laxser.blitz.web.RequestPath;

public interface IgnoredPath {

    public boolean hit(RequestPath requestPath);
}
