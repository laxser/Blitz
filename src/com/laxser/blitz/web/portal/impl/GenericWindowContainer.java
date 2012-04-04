/*
 * Copyright 2007-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laxser.blitz.web.portal.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.laxser.blitz.BlitzConstants;
import com.laxser.blitz.web.Invocation;
import com.laxser.blitz.web.portal.Window;
import com.laxser.blitz.web.portal.WindowCallback;
import com.laxser.blitz.web.portal.WindowContainer;
import com.laxser.blitz.web.portal.WindowListener;
import com.laxser.blitz.web.portal.WindowListeners;
import com.laxser.blitz.web.portal.WindowRender;

/**
 * {@link ServerPortal} 的实现类，Portal 框架的核心类。
 * 也就是通用的Window容器，用来容纳所有的Window对象
 *
 * @author laxser  Date 2012-3-23 下午4:52:54
@contact [duqifan@gmail.com]
@GenericWindowContainer.java

 */
public abstract class GenericWindowContainer implements WindowRender, WindowContainer,
        WindowListener {

    private static final Log logger = LogFactory.getLog(GenericWindowContainer.class);

    protected static final NestedWindowRender singletonRender = new NestedWindowRender();

    protected NestedWindowRender render = singletonRender;

    protected ExecutorService executorService;

    protected WindowListeners windowListeners;

    protected Invocation invocation;

    protected List<Window> windows = new LinkedList<Window>();

    protected long timeout;

    public GenericWindowContainer(Invocation inv, ExecutorService executorService,
            WindowListener portalListener) {
        this.invocation = inv;
        this.executorService = executorService;
        addListener(portalListener);
    }

    public void setTimeout(long timeoutInMills) {
        this.timeout = timeoutInMills;
    }

    public long getTimeout() {
        return timeout;
    }

    @Override
    public Invocation getInvocation() {
        return invocation;
    }



    @Override
    public HttpServletRequest getRequest() {
        return invocation.getRequest();
    }

    @Override
    public HttpServletResponse getResponse() {
        return invocation.getResponse();
    }

    @Override
    public void addListener(WindowListener l) {
        if (l == null) {
            return;
        } else {
            synchronized (this) {
                if (windowListeners == null) {
                    windowListeners = new WindowListeners();
                }
                windowListeners.addListener(l);
            }
        }
    }

    @Override
    public Window addWindow(String name, String windowPath) {
        return this.addWindow(name, windowPath, (WindowCallback) null);
    }

    @Override
    public Window addWindow(String name, String windowPath, final Map<String, Object> attributes) {
        WindowCallback callback = null;
        if (attributes != null && attributes.size() > 0) {
            callback = new WindowCallback() {

                @Override
                public void beforeSubmit(Window window) {
                    synchronized (attributes) {
                        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                            window.set(entry.getKey(), entry.getValue());
                        }
                    }
                }
            };
        }
        return this.addWindow(name, windowPath, callback);
    }

    @Override
    public Window addWindow(String name, String windowPath, WindowCallback callback) {
        // 创建 窗口对象
        WindowImpl window = new WindowImpl(this, name, windowPath);

        WindowRequest request = new WindowRequest(window, getRequest());
        WindowResponse response = new WindowResponse(window);

        request.setAttribute("$$blitz-portal.window.name", name);
        request.setAttribute("$$biltz-portal.window.path", windowPath);

        // PortalWaitInterceptor#waitForWindows
        // RoseFilter#supportsRosepipe
        request.removeAttribute(BlitzConstants.PIPE_WINDOW_IN);

        // 定义窗口任务
        WindowTask task = new WindowTask(window, request, response);

        // 注册到相关变量中
        Window windowInView = new WindowForView(window);
        synchronized (windows) {
            this.windows.add(windowInView);
        }
        // for render
        this.invocation.addModel(name, windowInView);

        if (callback != null) {
            callback.beforeSubmit(window);
        }

        // 事件侦听回调
        onWindowAdded(window);

        // 提交到执行服务中执行
        WindowFuture<?> future = submitWindow(this.executorService, task);
        window.setFuture(future);

        // 返回窗口对象
        return window;
    }

    @Override
    public List<Window> getWindows() {
        return windows;
    }

    @Override
    public WindowRender getWindowRender() {
        return render.getInnerRender();
    }

    @Override
    public void setWindowRender(WindowRender render) {
        if (render == null) {
            this.render = singletonRender;
        } else {
            if (this.render == singletonRender) {
                this.render = new NestedWindowRender(render);
            } else {
                this.render.setInnerRender(render);
            }
        }
    }

    @SuppressWarnings( { "unchecked", "rawtypes" })
    protected WindowFuture<?> submitWindow(ExecutorService executor, WindowTask task) {
        Future<?> future = executor.submit(task);
        return new WindowFuture(future, task.getWindow());
    }

    @Override
    public void render(Writer out, Window window) throws IOException {
        render.render(out, window);
    }

    //-------------实现toString()---------------F

    @Override
    public String toString() {
        return "aggregate ['" + invocation.getRequestPath().getUri() + "']";
    }

    //------------ 以下代码是PortalListener和Invocation的实现代码 --------------------------------

    @Override
    public void onWindowAdded(Window window) {
        if (windowListeners != null) {
            try {
                windowListeners.onWindowAdded(window);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    @Override
    public void onWindowStarted(Window window) {
        if (windowListeners != null) {
            try {
                windowListeners.onWindowStarted(window);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    @Override
    public void onWindowCanceled(Window window) {
        if (windowListeners != null) {
            try {
                windowListeners.onWindowCanceled(window);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    @Override
    public void onWindowDone(Window window) {
        if (windowListeners != null) {
            try {
                windowListeners.onWindowDone(window);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    @Override
    public void onWindowError(Window window) {
        if (windowListeners != null) {
            try {
                windowListeners.onWindowError(window);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    @Override
    public void onWindowTimeout(Window window) {
        if (windowListeners != null) {
            try {
                windowListeners.onWindowTimeout(window);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

}
