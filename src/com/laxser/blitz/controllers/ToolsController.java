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
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laxser.blitz.controllers;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.laxser.blitz.scanning.ResourceRef;
import com.laxser.blitz.scanning.BlitzScanner;
import com.laxser.blitz.web.InterceptorDelegate;
import com.laxser.blitz.web.Invocation;
import com.laxser.blitz.web.RequestPath;
import com.laxser.blitz.web.annotation.HttpFeatures;
import com.laxser.blitz.web.annotation.Path;
import com.laxser.blitz.web.annotation.ReqMethod;
import com.laxser.blitz.web.annotation.rest.Get;
import com.laxser.blitz.web.impl.mapping.EngineGroup;
import com.laxser.blitz.web.impl.mapping.MappingNode;
import com.laxser.blitz.web.impl.mapping.MatchResult;
import com.laxser.blitz.web.impl.module.ControllerRef;
import com.laxser.blitz.web.impl.module.Module;
import com.laxser.blitz.web.impl.thread.ActionEngine;
import com.laxser.blitz.web.impl.thread.InvocationBean;
import com.laxser.blitz.web.impl.thread.LinkedEngine;
import com.laxser.blitz.web.impl.thread.Blitz;


/**
 * 这个Blitz内置的Controller是用来给用户显示Blitz当前配置信息的，可以方便的使用在调试者模式。
 * 若部署上线我们便可以通过配置隐藏这个功能
 * @author laxser  Date 2012-3-23 下午3:49:23
@contact [duqifan@gmail.com]
@ToolsController.java

 * 
 */
@Path("")
public class ToolsController {

    private Date startupTime = new Date();

    @Get
    public String tools(Invocation inv) {
        String prefix = inv.getRequestPath().getUri();
        if (!prefix.endsWith("/")) {
            prefix = prefix + "/";
        }
        String s = "tool list:<p>";
        s += "<div style='margin-left:20px'>";
        s += "<a href=\"" + prefix + "tree\">/blitz-info/tree</a></div>";

        s += "<div style='margin-left:20px'>";
        s += "<a href=\"" + prefix + "modules\">/blitz-info/modules</a></div>";

        s += "<div style='margin-left:20px'>";
        s += "<a href=\"" + prefix + "resources\">/blitz-info/resources</a></div>";

        s += "<div style='margin-left:20px'>";
        s += "<a href=\"" + prefix + "method\">/blitz-info/method</a></div>";

        s += "<div style='margin-left:20px'>";
        s += "<a href=\"" + prefix + "startupInfo\">/blitz-info/startupInfo</a></div>";
        s += "<p>";
        return Utils.wrap(s);
    }

    @Get("tree")
    @HttpFeatures(contentType = "application/xml")
    public String tree(Blitz blitz) throws Exception {
        MappingNode root = blitz.getMappingTree();
        StringBuilder sb = new StringBuilder(2048);
        sb.append("@<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<blitz-web>");
        printNode(root, "", sb);
        sb.append("</blitz-web>");
        return sb.toString();
    }

    @Get("method")
    public Object method(Invocation pinv) {
        InvocationBean inv = (InvocationBean) pinv;
        RequestPath curpath = inv.getRequestPath();
        String testUri = inv.getRequest().getQueryString(); // queryString as uri
        if (testUri == null || testUri.length() == 0) {
            return "@e.g. <a href='/blitz-info/method?get=/blitz-info/tree'>/blitz-info/method?get=/blitz-info/tree</a>";
        }
        ReqMethod testMethod = curpath.getMethod();
        if (testUri.indexOf('=') > 0) {
            int index = testUri.indexOf('=');
            testMethod = ReqMethod.parse(testUri.substring(0, index));
            testUri = testUri.substring(index + 1);
        }
        if (!testUri.startsWith(curpath.getCtxpath())) {
            return "@wrong uri:" + testUri;
        }
        MappingNode tree = inv.getBlitz().getMappingTree();
        RequestPath testPath = new RequestPath(//
                testMethod, testUri, curpath.getCtxpath(), curpath.getDispatcher());
        //
        ArrayList<MatchResult> matchResults = tree.match(testPath);
        if (matchResults == null) {
            // not blitz uri
            return ("@404: <br>Not blitz uri: '" + testUri + "'");
        }

        final MatchResult lastMatched = matchResults.get(matchResults.size() - 1);
        final EngineGroup leafEngineGroup = lastMatched.getMappingNode().getLeafEngines();
        final LinkedEngine leafEngine = select(leafEngineGroup.getEngines(testMethod), inv
                .getRequest());
        if (leafEngine == null) {
            if (leafEngineGroup.size() == 0) {
                // not blitz uri
                return ("@404: <br>not blitz uri, not exsits leaf engines for it: '" + testUri + "'");

            } else {
                // 405 Method Not Allowed
                StringBuilder allow = new StringBuilder();
                final String gap = ", ";

                for (ReqMethod m : leafEngineGroup.getAllowedMethods()) {
                    allow.append(m.toString()).append(gap);
                }
                if (allow.length() > 0) {
                    allow.setLength(allow.length() - gap.length());
                }

                // true: don't forward to next filter or servlet
                return "@405: allowed=" + allow.toString();
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("@200:");
        ActionEngine actionEngine = (ActionEngine) leafEngine.getTarget();
        sb.append(" <br>mapped '" + testUri + "' to " + actionEngine.getControllerClass().getName()
                + "#" + actionEngine.getMethod().getName());

        sb.append("<br>intectptors:");
        for (InterceptorDelegate i : actionEngine.getRegisteredInterceptors()) {
            sb.append("<br>").append(i.getName()).append("=").append(
                    InterceptorDelegate.getMostInnerInterceptor(i).getClass().getName()).append(
                    "(p=").append(i.getPriority()).append(")");
        }

        return sb;
    }

    @Get("modules")
    public Object modules(Blitz blitz) throws Exception {
        List<Module> modules = blitz.getModules();
        StringBuilder sb = new StringBuilder(2048);
        int i = 1;
        for (Module module : modules) {
            printModule(sb, i++, module);
        }
        return Utils.wrap(sb.toString());
    }

    @Get("resources")
    public String resources() throws Exception {
        List<ResourceRef> resources = BlitzScanner.getInstance().getJarOrClassesFolderResources();
        StringBuilder sb = new StringBuilder(1024).append("<ul>");
        for (ResourceRef resource : resources) {
            sb.append("<li>");
            sb.append(resource.getResource().getURL());
            sb.append(Arrays.toString(resource.getModifiers()));
            sb.append("</li>");
        }
        sb.append("</ul>");
        return Utils.wrap(sb.toString());
    }

    @Get("startupInfo")
    public String startupInfo() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(startupTime);
        String startup = String.format("<strong>startup</strong>=%s", time);
        return Utils.wrap(startup);
    }

    //------------rewriter--------

    @Get("module")
    public String module() {
        return "r:a:modules";
    }

    @Get("jar")
    public String jar() {
        return "r:a:resources";
    }

    //---privates

    /**
     * @see #modules(Blitz)
     */
    private void printModule(StringBuilder sb, int i, Module module) {
        sb.append("<div style=\"margin-top:5px;border-top:solid black 1px;\"><table>");
        // number
        sb.append("<tr valign=\"top\"><td>").append(i).append("</td><td></td></tr>");
        // mappingPath
        sb.append("<tr><td>mappingPath</td><td>").append(module.getMappingPath()).append(
                "</td></tr>");
        // relativePackagePath
        sb.append("<tr><td>relativePackagePath</td><td>").append(module.getRelativePath()).append(
                "</td></tr>");
        // url
        sb.append("<tr valign=\"top\"><td>url</td><td>").append(module.getUrl()).append(
                "</td></tr>");
        sb.append("<tr valign=\"top\"><td>controllers</td><td>");
        for (ControllerRef controller : module.getControllers()) {
            sb.append("'").append(Arrays.toString(controller.getMappingPaths())).append("'=")
                    .append(controller.getControllerClass().getName()).append(";<br>");
        }
        sb.append("</td></tr>");
        // resolvers
        sb.append("<tr><td valign=\"top\">resolvers</td><td>").append(
                Arrays.toString(module.getCustomerResolvers().toArray())).append("</td></tr>");
        // validators
        sb.append("<tr><td valign=\"top\">validators</td><td>").append(
                Arrays.toString(module.getValidators().toArray())).append("</td></tr>");
        // interceptors
        sb.append("<tr><td valign=\"top\">interceptors</td><td>").append(
                Arrays.toString(module.getInterceptors().toArray())).append("</td></tr>");
        // errorhandler
        sb.append("<tr><td>errorHanlder</td><td>").append(
                module.getErrorHandler() == null ? "" : module.getErrorHandler()).append(
                "</td></tr>");
        // end
        sb.append("</table></div>");
    }

    /**
     * @see #method(Invocation)
     * @param engines
     * @param request
     * @return
     */
    private LinkedEngine select(LinkedEngine[] engines, HttpServletRequest request) {
        LinkedEngine selectedEngine = null;
        int score = 0;

        for (LinkedEngine engine : engines) {
            int candidate = engine.isAccepted(request);
            if (candidate > score) {
                selectedEngine = engine;
                score = candidate;
            }
        }
        return selectedEngine;
    }

    /**
     * @see #tree(Blitz)
     * @param node
     * @param prefix
     * @param sb
     */
    private void printNode(final MappingNode node, String prefix, StringBuilder sb) {
        sb.append("<node path=\"").append(prefix + node.getMappingPath()).append("\">");
        //
        EngineGroup leaf = node.getLeafEngines();
        if (leaf.size() > 0) {
            for (ReqMethod method : leaf.getAllowedMethods()) {
                for (LinkedEngine engine : leaf.getEngines(method)) {
                    ActionEngine action = (ActionEngine) engine.getTarget();
                    Method m = action.getMethod();
                    Class<?> cc = action.getControllerClass();
                    String rm = method.toString();
                    sb.append("<allowed ");
                    sb.append(rm + "=\"" + cc.getSimpleName() + "#" + m.getName() + "\" ");
                    sb.append("package=\"" + m.getDeclaringClass().getPackage().getName() + "\" ");
                    sb.append(" />");
                }
            }
        }

        MappingNode child = node.getLeftMostChild();
        while (child != null) {
            printNode(child, prefix + node.getMappingPath(), sb);
            child = child.getSibling();
        }
        sb.append("</node>");
    }

}
