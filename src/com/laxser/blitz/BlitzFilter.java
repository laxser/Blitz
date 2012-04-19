/*
 * Copyright 2007-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License i distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laxser.blitz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.NestedServletException;

import com.laxser.blitz.scanner.ModuleResource;
import com.laxser.blitz.scanner.ModuleResourceProvider;
import com.laxser.blitz.scanner.ModuleResourceProviderImpl;
import com.laxser.blitz.scanning.LoadScope;
import com.laxser.blitz.scanning.context.BlitzWebAppContext;
import com.laxser.blitz.util.PrinterHelper;
import com.laxser.blitz.web.RequestPath;
import com.laxser.blitz.web.annotation.ReqMethod;
import com.laxser.blitz.web.impl.mapping.ConstantMapping;
import com.laxser.blitz.web.impl.mapping.Mapping;
import com.laxser.blitz.web.impl.mapping.MappingNode;
import com.laxser.blitz.web.impl.mapping.TreeBuilder;
import com.laxser.blitz.web.impl.mapping.ignored.IgnoredPath;
import com.laxser.blitz.web.impl.mapping.ignored.IgnoredPathEnds;
import com.laxser.blitz.web.impl.mapping.ignored.IgnoredPathEquals;
import com.laxser.blitz.web.impl.mapping.ignored.IgnoredPathRegexMatch;
import com.laxser.blitz.web.impl.mapping.ignored.IgnoredPathStarts;
import com.laxser.blitz.web.impl.module.Module;
import com.laxser.blitz.web.impl.module.ModulesBuilder;
import com.laxser.blitz.web.impl.module.ModulesBuilderImpl;
import com.laxser.blitz.web.impl.thread.LinkedEngine;
import com.laxser.blitz.web.impl.thread.RootEngine;
import com.laxser.blitz.web.impl.thread.Blitz;
import com.laxser.blitz.web.instruction.InstructionExecutor;
import com.laxser.blitz.web.instruction.InstructionExecutorImpl;

/**
 * Blitz 是一个基于Servlet规范、Spring“规范”的WEB开发框架。
 * <p>
 * Blitz 框架通过在web.xml配置过滤器拦截并处理匹配的web请求，如果一个请求应该由在Blitz框架下的类来处理，
 * 该请求将在Blitz调用中完成对客户端响应.
 * 如果一个请求在Blitz中没有找到合适的类来为他服务，Blitz将把该请求移交给web容器的其他组件来处理。
 * <p>
 * 
 * Blitz使用过滤器而非Servlet来接收web请求，这有它的合理性以及好处。
 * <p>
 * Servlet规范以“边走边看”的方式来处理请求，
 * 当服务器接收到一个web请求时，并没有要求在web.xml必须有相应的Servlet组件时才能处理，web请求被一系列Filter过滤时，
 * Filter可以拿到相应的Request和Response对象
 * ，当Filter认为自己已经能够完成整个处理，它可以不调用整个处理链的下个组件处理.
 * <p>
 * 使用过滤器的好处是，Blitz可以很好地和其他web框架兼容。这在改造遗留系统、对各种uri的支持具有天然优越性。正是使用过滤器，
 * Blitz不在要求请求地址具有特殊的后缀。
 * <p>
 * 为了更好地理解，可以把Blitz过滤器看成能将某些请求其它Filter或Servlet传递的Servlet。这个刚好是普通Servlet无法做到的
 * ： 如果一个请求以后缀名配置给他处理时候
 * ，一旦该Servlet处理不了，Servlet规范没有提供机制使得可以由配置在web.xml的其他正常组件处理
 * (除404，500等错误处理组件之外)。
 * <p>
 * 
 * 一个web.xml中可能具有不只一个的Filter，Filter的先后顺序对系统具有重要影响，特别的，Blitz自己的过滤器的配置顺序更是需要讲究
 * 。 如果一个请求在被Blitz处理前应该被某些过滤器过滤，请把这些过滤器的mapping配置在Blitz过滤器之前。
 * <p>
 * 
 * BlitzFilter的配置，建议按以下配置即可：
 * 
 * <pre>
 * 	&lt;filter&gt;
 * 		&lt;filter-name&gt;BlitzFilter&lt;/filter-name&gt;
 * 		&lt;filter-class&gt;net.paoding.Blitz.BlitzFilter&lt;/filter-class&gt;
 * 	&lt;/filter&gt;
 * 	&lt;filter-mapping&gt;
 * 		&lt;filter-name&gt;BlitzFilter&lt;/filter-name&gt;
 * 		&lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 * 		&lt;dispatcher&gt;REQUEST&lt;/dispatcher&gt;
 * 		&lt;dispatcher&gt;FORWARD&lt;/dispatcher&gt;
 * 		&lt;dispatcher&gt;INCLUDE&lt;/dispatcher&gt;
 * 	&lt;/filter-mapping&gt;
 * </pre>
 * 
 * 1) 大多数请况下，<strong>filter-mapping</strong> 应配置在所有Filter Mapping的最后。<br>
 * 2) 不能将 <strong>FORWARD、INCLUDE</strong> 的 dispatcher 去掉，否则forward、
 * include的请求Blitz框架将拦截不到<br>
 * <p>
 * 
 * Blitz框架内部采用<strong>"匹配->执行"</strong>两阶段逻辑。Blitz内部结构具有一个匹配树，
 * 这个数据结构可以快速判断一个请求是否应该由Blitz处理并进行， 没有找到匹配的请求交给过滤器的下一个组件处理。匹配成功的请求将进入”执行“阶段。
 * 执行阶段需要经过6个步骤处理：<strong>“参数解析 -〉 验证器 -〉 拦截器 -〉 控制器 -〉 视图渲染
 * -〉渲染后"</strong>的处理链。
 * <p>
 * 
 * <strong>匹配树</strong>: <br>
 * TODO
 * <p>
 * 
 * <strong>匹配过程</strong>: <br>
 * TODO
 * <P>
 * 
 * <strong>参数解析</strong>: <br>
 * 在调用验证器、拦截器
 * 控制器之前，Blitz完成2个解析：解析匹配树上动态的参数出实际值，解析控制器方法中参数实际的值。参数可能会解析失败(例如转化异常等等
 * )，此时该参数以默认值进行代替，同时Blitz解析失败和异常记录起来放到专门的类中，继续下一个过程而不打断执行。
 * <P>
 * 
 * <strong>拦截器</strong>: <br>
 * Blitz使用自定义的拦截器接口而非一般的拦截器接口这是有理由的。使用Blitz自定义的拦截器接口可以更容易地操作、控制Blitz拦截。
 * 所谓拦截即是对已经匹配的控制器调用进行拦截，在其调用之前、之后以及页面渲染之后执行某些逻辑。设计良好的拦截器可以被多个控制器使用。
 * <P>
 * 
 * <strong>控制器</strong>: <br>
 * 
 * 
 */
public class BlitzFilter extends GenericFilterBean {

    private static final String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE;

    /** 使用的applicationContext地址 */
    private String contextConfigLocation;

    private InstructionExecutor instructionExecutor = new InstructionExecutorImpl();

    private List<Module> modules;

    private MappingNode mappingTree;

    private Class<? extends ModuleResourceProvider> moduleResourceProviderClass = ModuleResourceProviderImpl.class;

    private Class<? extends ModulesBuilder> modulesBuilderClass = ModulesBuilderImpl.class;

    private LoadScope load = new LoadScope("", "controllers");

    private IgnoredPath[] ignoredPaths = new IgnoredPath[] {
            new IgnoredPathStarts(BlitzConstants.VIEWS_PATH_WITH_END_SEP),
            new IgnoredPathEquals("/favicon.ico") };

    /**
     * 改变默认行为，告知Blitz要读取的applicationContext地址
     */
    public void setContextConfigLocation(String contextConfigLocation) {
        if (StringUtils.isBlank(contextConfigLocation)) {
            throw new IllegalArgumentException("contextConfigLocation");
        }
        this.contextConfigLocation = contextConfigLocation;
    }

    public void setInstructionExecutor(InstructionExecutor instructionExecutor) {
        this.instructionExecutor = instructionExecutor;
    }

    public void setModuleResourceProviderClass(
            Class<? extends ModuleResourceProvider> moduleResourceProviderClass) {
        this.moduleResourceProviderClass = moduleResourceProviderClass;
    }

    public void setModulesBuilderClass(Class<? extends ModulesBuilder> modulesBuilderClass) {
        this.modulesBuilderClass = modulesBuilderClass;
    }

    /**
     * <pre>
     * like: &quot;com.renren.myapp, com.renren.yourapp&quot; etc
     * </pre>
     * 
     * @param load
     */
    public void setLoad(String load) {
        this.load = new LoadScope(load, "controllers");
    }

    /**
     * @see #quicklyPass(RequestPath)
     * @param ignoredPathStrings
     */
    public void setIgnoredPaths(String[] ignoredPathStrings) {
        List<IgnoredPath> list = new ArrayList<IgnoredPath>(ignoredPathStrings.length + 2);
        for (String ignoredPath : ignoredPathStrings) {
            ignoredPath = ignoredPath.trim();
            if (StringUtils.isEmpty(ignoredPath)) {
                continue;
            }
            if (ignoredPath.equals("*")) {
                list.add(new IgnoredPathEquals(""));
                list.add(new IgnoredPathStarts("/"));
                break;
            }
            if (ignoredPath.startsWith("regex:")) {
                list.add(new IgnoredPathRegexMatch(ignoredPath.substring("regex:".length())));
            } else {
                if (ignoredPath.length() > 0 && !ignoredPath.startsWith("/")
                        && !ignoredPath.startsWith("*")) {
                    ignoredPath = "/" + ignoredPath;
                }
                if (ignoredPath.endsWith("*")) {
                    list.add(new IgnoredPathStarts(ignoredPath.substring(0,
                            ignoredPath.length() - 1)));
                } else if (ignoredPath.startsWith("*")) {
                    list.add(new IgnoredPathEnds(ignoredPath.substring(1)));
                } else {
                    list.add(new IgnoredPathEquals(ignoredPath));
                }
            }
        }
        IgnoredPath[] ignoredPaths = Arrays.copyOf(this.ignoredPaths, this.ignoredPaths.length
                + list.size());
        for (int i = this.ignoredPaths.length; i < ignoredPaths.length; i++) {
            ignoredPaths[i] = list.get(i - this.ignoredPaths.length);
        }
        this.ignoredPaths = ignoredPaths;
    }

    /**
     * 实现 {@link GenericFilterBean#initFilterBean()}，对 Blitz 进行初始化
     */

    @Override
    protected final void initFilterBean() throws ServletException {
        try {
            if (logger.isInfoEnabled()) {
                logger.info("[init] call 'init/rootContext'");
            }

            if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder();
                Enumeration<String> iter = getFilterConfig().getInitParameterNames();
                while (iter.hasMoreElements()) {
                    String name = (String) iter.nextElement();
                    sb.append(name).append("='").append(getFilterConfig().getInitParameter(name))
                            .append("'\n");
                }
                logger.debug("[init] parameters: " + sb);
            }

            WebApplicationContext rootContext = prepareRootApplicationContext();

            if (logger.isInfoEnabled()) {
                logger.info("[init] exits from 'init/rootContext'");
                logger.info("[init] call 'init/module'");
            }

            // 识别 Blitz 程序模块
            this.modules = prepareModules(rootContext);

            if (logger.isInfoEnabled()) {
                logger.info("[init] exits from 'init/module'");
                logger.info("[init] call 'init/mappingTree'");
            }

            // 创建匹配树以及各个结点的上的执行逻辑(Engine)
            this.mappingTree = prepareMappingTree(modules);

            if (logger.isInfoEnabled()) {
                logger.info("[init] exits from 'init/mappingTree'");
                logger.info("[init] exits from 'init'");
            }

            // 打印启动信息
            printBlitzInfos();

            //
        } catch (final Throwable e) {
            StringBuilder sb = new StringBuilder(1024);
            sb.append("[Blitz-").append(BlitzVersion.getVersion());
            sb.append("@Spring-").append(SpringVersion.getVersion()).append("]:");
            sb.append(e.getMessage());
            logger.error(sb.toString(), e);
            throw new NestedServletException(sb.toString(), e);
        }
    }

    /**
     * 接收所有进入 BlitzFilter 的请求进行匹配，如果匹配到有相应的处理类处理它则由这个类来处理他、渲染并响应给客户端。
     * 如果没有找到匹配的处理器，Blitz将把请求转交给整个过滤链的下一个组件，让web容器的其他组件来处理它。
     */
    @Override
    public void doFilter(ServletRequest request, final ServletResponse response,
            final FilterChain filterChain) throws IOException, ServletException {
        // cast
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 打开DEBUG级别信息能看到所有进入BlitzFilter的请求
        if (logger.isDebugEnabled()) {
            StringBuffer sb = httpRequest.getRequestURL();
            String query = httpRequest.getQueryString();
            if (query != null && query.length() > 0) {
                sb.append("?").append(query);
            }
            logger.debug(httpRequest.getMethod() + " " + sb.toString());
        }

        supportsBlitzpipe(httpRequest);

        // 创建RequestPath对象，用于记录对地址解析的结果
        final RequestPath requestPath = new RequestPath(httpRequest);

        //  简单、快速判断本次请求，如果不应由Blitz执行，返回true
        if (quicklyPass(requestPath)) {
            notMatched(filterChain, httpRequest, httpResponse, requestPath);
            return;
        }

        // matched为true代表本次请求被Blitz匹配，不需要转发给容器的其他 flter 或 servlet
        boolean matched = false;
        try {
            // Blitz 对象代表Blitz框架对一次请求的执行：一朵玫瑰出墙来
            final Blitz Blitz = new Blitz(modules, mappingTree, httpRequest, httpResponse, requestPath);

            // 对请求进行匹配、处理、渲染以及渲染后的操作，如果找不到映配则返回false
            matched = Blitz.start();

        } catch (Throwable exception) {
            throwServletException(requestPath, exception);
        }

        // 非Blitz的请求转发给WEB容器的其他组件处理，而且不放到上面的try-catch块中
        if (!matched) {
            notMatched(filterChain, httpRequest, httpResponse, requestPath);
        }
    }

    // @see com.laxser.blitz.web.portal.impl.PortalWaitInterceptor#waitForWindows
    protected void supportsBlitzpipe(final HttpServletRequest httpRequest) {
        // 这个代码为Blitzpipe所用，以避免Blitzpipe的"Cannot forward after response has been committed"异常
        // @see net.paoding.Blitz.web.portal.impl.PortalWaitInterceptor
        Object window = httpRequest.getAttribute(BlitzConstants.WINDOW_ATTR);
        if (window != null && window.getClass().getName().startsWith("com.laxser.blitz.web.portal")) {
            httpRequest.setAttribute(BlitzConstants.PIPE_WINDOW_IN, Boolean.TRUE);
            if (logger.isDebugEnabled()) {
                try {
                    logger.debug("notify window '"
                            + httpRequest.getAttribute("$$blitz-portal.window.name") + "'");
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
            synchronized (window) {
                window.notifyAll();
            }
        }
    }

    /**
     * 创建最根级别的 ApplicationContext 对象，比如WEB-INF、WEB-INF/classes、
     * jar中的spring配置文件所组成代表的、整合为一个 ApplicationContext 对象
     * 
     * @return
     * @throws IOException
     */
    private WebApplicationContext prepareRootApplicationContext() throws IOException {

        if (logger.isInfoEnabled()) {
            logger.info("[init/rootContext] starting ...");
        }

        ApplicationContext oldRootContext = (ApplicationContext) getServletContext().getAttribute(
                ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        // 如果web.xml配置使用了spring装载root应用context ...... 不可以
        // BlitzFilter可能因为启动失败，在请求的时候容器还会尝试重新启动，此时rootContext可能已经存在，不要简单地抛出异常
        // 同时这样留出了使用Listener作为init Blitz context的扩展机会
        if (oldRootContext != null) {
            if (oldRootContext.getClass() != BlitzWebAppContext.class) {
                throw new IllegalStateException(
                        "Cannot initialize context because there is already a root application context present - "
                                + "check whether you have multiple ContextLoader* definitions in your web.xml!");
            }
            if (logger.isInfoEnabled()) {
                logger.info("[init/rootContext] the root context exists:" + oldRootContext);
            }
            return (BlitzWebAppContext) oldRootContext;
        }

        BlitzWebAppContext rootContext = new BlitzWebAppContext(getServletContext(), load, false);

        String contextConfigLocation = this.contextConfigLocation;
        // 确认所使用的applicationContext配置
        if (StringUtils.isBlank(contextConfigLocation)) {
            String webxmlContextConfigLocation = getServletContext().getInitParameter(
                    "contextConfigLocation");
            if (StringUtils.isBlank(webxmlContextConfigLocation)) {
                contextConfigLocation = BlitzWebAppContext.DEFAULT_CONFIG_LOCATION;
            } else {
                contextConfigLocation = webxmlContextConfigLocation;
            }
        }
        rootContext.setConfigLocation(contextConfigLocation);
        rootContext.setId("Blitz.root");
        rootContext.refresh();

        if (logger.isInfoEnabled()) {
            logger.info("[init/rootContext] exits");
        }

        /* enable: WebApplicationContextUtils.getWebApplicationContext() */
        getServletContext().setAttribute(ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, rootContext);

        if (logger.isInfoEnabled()) {
            logger.info("[init/rootContext] Published Blitz.root WebApplicationContext ["
                    + rootContext + "] as ServletContext attribute with name ["
                    + ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE + "]");
        }

        return rootContext;
    }

    private List<Module> prepareModules(WebApplicationContext rootContext) throws Exception {
        // 自动扫描识别web层资源，纳入Blitz管理
        if (logger.isInfoEnabled()) {
            logger.info("[init/mudule] starting ...");
        }

        ModuleResourceProvider provider = moduleResourceProviderClass.newInstance();

        if (logger.isInfoEnabled()) {
            logger.info("[init/module] using provider: " + provider);
            logger.info("[init/module] call 'moduleResource': to find all module resources.");
            logger.info("[init/module] load " + load);
        }
        List<ModuleResource> moduleResources = provider.findModuleResources(load);

        if (logger.isInfoEnabled()) {
            logger.info("[init/mudule] exits 'moduleResource'");
        }

        ModulesBuilder modulesBuilder = modulesBuilderClass.newInstance();

        if (logger.isInfoEnabled()) {
            logger.info("[init/module] using modulesBuilder: " + modulesBuilder);
            logger.info("[init/module] call 'moduleBuild': to build modules.");
        }

        List<Module> modules = modulesBuilder.build(moduleResources, rootContext);

        if (logger.isInfoEnabled()) {
            logger.info("[init/module] exits from 'moduleBuild'");
            logger.info("[init/mudule] found " + modules.size() + " modules.");
        }

        return modules;
    }

    private MappingNode prepareMappingTree(List<Module> modules) {
        Mapping rootMapping = new ConstantMapping("");
        MappingNode mappingTree = new MappingNode(rootMapping);
        LinkedEngine rootEngine = new LinkedEngine(null, new RootEngine(instructionExecutor),
                mappingTree);
        mappingTree.getMiddleEngines().addEngine(ReqMethod.ALL, rootEngine);

        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.create(mappingTree, modules);

        return mappingTree;
    }

    /**
     * 简单、快速判断本次请求，如果不应由Blitz执行，返回true
     * 
     * @param requestPath
     * @return
     */
    private boolean quicklyPass(final RequestPath requestPath) {
        for (IgnoredPath p : ignoredPaths) {
            if (p.hit(requestPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        WebApplicationContext rootContext = WebApplicationContextUtils
                .getWebApplicationContext(getServletContext());
        if (rootContext != null) {
            try {
                if (rootContext instanceof AbstractApplicationContext) {
                    ((AbstractApplicationContext) rootContext).close(); // Blitz.root
                }
            } catch (Throwable e) {
                logger.error("", e);
                getServletContext().log("", e);
            }
        }

        try {
            mappingTree.destroy();
        } catch (Throwable e) {
            logger.error("", e);
            getServletContext().log("", e);
        }
        super.destroy();
    }

    /**
     * Blitz 原理 首先Blitz尝试第一个拦截请求，若经过匹配是Blitz的Uri，那么由Blitz内部机制处理。
     * 否则将resp req传给FilterChain的其他Filter处理
     * 
     * @param filterChain
     * @param httpRequest
     * @param httpResponse
     * @param path
     * @throws IOException
     * @throws ServletException
     */
    protected void notMatched(//
            FilterChain filterChain, //
            HttpServletRequest httpRequest,//
            HttpServletResponse httpResponse,//
            RequestPath path)//
            throws IOException, ServletException {
        if (logger.isDebugEnabled()) {
            logger.debug("not a Blitz uri: " + path.getUri());
        }
        // 调用其它Filter
        filterChain.doFilter(httpRequest, httpResponse);
    }

    
    /**
     * 处理过程中碰到问题统统使用
     * @param requestPath
     * @param exception
     * @throws ServletException
     */
    private void throwServletException(RequestPath requestPath, Throwable exception)
            throws ServletException {
        String msg = requestPath.getMethod() + " " + requestPath.getUri();
        ServletException servletException;
        if (exception instanceof ServletException) {
            servletException = (ServletException) exception;
        } else {
            servletException = new NestedServletException(msg, exception);
        }
        logger.error(msg, exception);
        getServletContext().log(msg, exception);
        throw servletException;
    }

    private void printBlitzInfos() {
        if (logger.isDebugEnabled()) {
            logger.debug(PrinterHelper.dumpModules(modules));
            logger.debug("Mapping tree:\n" + PrinterHelper.list(mappingTree));
        }

        String msg = String.format("[init] Blitz initialized, %s modules loaded! (version=%s)",
                modules.size(), BlitzVersion.getVersion());
        logger.info(msg);
        getServletContext().log(msg);
    }
}
