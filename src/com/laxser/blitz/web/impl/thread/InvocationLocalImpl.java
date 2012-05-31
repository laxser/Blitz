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
package com.laxser.blitz.web.impl.thread;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import com.laxser.blitz.web.Invocation;
import com.laxser.blitz.web.InvocationLocal;
import com.laxser.blitz.web.InvocationUtils;
import com.laxser.blitz.web.RequestPath;
import com.laxser.blitz.web.paramresolver.ParamMetaData;
import com.laxser.blitz.web.var.Flash;
import com.laxser.blitz.web.var.Model;

/**
 * 
 * @author laxser  Date 2012-5-21 上午11:30:21
@contact [duqifan@gmail.com]
@InvocationLocalImpl.java

 * 
 */
public class InvocationLocalImpl implements InvocationLocal {

    @Override
    public Invocation getCurrent(boolean required) {
        Invocation inv = InvocationUtils.getInvocation(InvocationUtils.getCurrentThreadRequest());
        if (inv == null && required) {
            throw new IllegalStateException("invocation");
        }
        return inv;
    }

    private Invocation required() {
        return getCurrent(true);
    }

    @Override
    public void addModel(Object value) {
        required().addModel(value);
    }

    @Override
    public void addModel(String name, Object value) {
        required().addModel(name, value);
    }

    @Override
    public void changeMethodParameter(int index, Object newValue) {
        required().changeMethodParameter(index, newValue);

    }

    @Override
    public void changeMethodParameter(String name, Object newValue) {
        required().changeMethodParameter(name, newValue);

    }
    
    @Override
    public void changeMethodParameter(ParamMetaData paramMeta, Object newValue) {
        required().changeMethodParameter(paramMeta, newValue);
    }

    @Override
    public WebApplicationContext getApplicationContext() {
        return required().getApplicationContext();
    }

    @Override
    public Object getAttribute(String name) {
        return required().getAttribute(name);
    }

    @Override
    public Set<String> getAttributeNames() {
        return required().getAttributeNames();
    }

    @Override
    public BindingResult getBindingResult(String bean) {
        return required().getBindingResult(bean);
    }

    @Override
    public List<String> getBindingResultNames() {
        return required().getBindingResultNames();
    }

    @Override
    public List<BindingResult> getBindingResults() {
        return required().getBindingResults();
    }

    @Override
    public Object getController() {
        return required().getController();
    }

    @Override
    public Class<?> getControllerClass() {
        return required().getControllerClass();
    }

    @Override
    public void addFlash(String name, String msg) {
        required().addFlash(name, msg);
    }

    @Override
    public Flash getFlash() {
        return required().getFlash();
    }

    @Override
    public Method getMethod() {
        return required().getMethod();
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return required().isAnnotationPresent(annotationClass);
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return required().getAnnotation(annotationClass);
    }

    @Override
    public Object getMethodParameter(String name) {
        return required().getMethodParameter(name);
    }

    @Override
    public String[] getMethodParameterNames() {
        return required().getMethodParameterNames();
    }

    @Override
    public Object[] getMethodParameters() {
        return required().getMethodParameters();
    }

    @Override
    public Model getModel() {
        return required().getModel();
    }

    @Override
    public Object getModel(String name) {
        return required().getModel(name);
    }

    @Override
    public String getParameter(String name) {
        return required().getParameter(name);
    }

    @Override
    public BindingResult getParameterBindingResult() {
        return required().getParameterBindingResult();
    }

    @Override
    public HttpServletRequest getRequest() {
        return required().getRequest();
    }

    @Override
    public RequestPath getRequestPath() {
        return required().getRequestPath();
    }

    @Override
    public HttpServletResponse getResponse() {
        return required().getResponse();
    }

    @Override
    public ServletContext getServletContext() {
        return required().getServletContext();
    }

    @Override
    public void removeAttribute(String name) {
        required().removeAttribute(name);

    }

    @Override
    public Invocation setAttribute(String name, Object value) {
        return required().setAttribute(name, value);
    }

    @Override
    public void setRequest(HttpServletRequest request) {
        required().setRequest(request);
    }

    @Override
    public Flash getFlash(boolean create) {
        return required().getFlash(create);
    }

    @Override
    public Invocation getPreInvocation() {
        return required().getPreInvocation();
    }

    @Override
    public Invocation getHeadInvocation() {
        return required().getHeadInvocation();
    }

    @Override
    public String getResourceId() {
        return required().getResourceId();
    }

    @Override
    public void addAfterCompletion(AfterCompletion afterComletion) {
        required().addAfterCompletion(afterComletion);
    }
}
