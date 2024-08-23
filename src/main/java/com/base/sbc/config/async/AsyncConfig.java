package com.base.sbc.config.async;


import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

@EnableAsync
@Configuration
public class AsyncConfig extends AsyncConfigurerSupport {

    public class ContextAwareCallable<T> implements Callable<T> {
        private final Callable<T> task;
        private final RequestAttributes context;

        public ContextAwareCallable(Callable<T> task, RequestAttributes context) {
            this.task = task;
            this.context = context;
        }

        @Override
        public T call() throws Exception {
            if (context != null) {
                RequestContextHolder.setRequestAttributes(context, true);
            }
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            ServletRequestAttributes newRequestAttributes = new ServletRequestAttributes(
                    new TinyHttpServletRequest(requestAttributes.getRequest()), requestAttributes.getResponse());
            // 线程上下文传递
            RequestContextHolder.setRequestAttributes(newRequestAttributes);

            try {
                return task.call();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        }
    }

    public class ContextAwareRunnable implements Runnable {
        private final Runnable thread;
        private final RequestAttributes context;

        public ContextAwareRunnable(Runnable thread, RequestAttributes context) {
            this.thread = thread;
            this.context = context;
        }

        @Override
        public void run()  {
            if (context != null) {
                RequestContextHolder.setRequestAttributes(context, true);
            }
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes newRequestAttributes = new ServletRequestAttributes(
                    new TinyHttpServletRequest(requestAttributes.getRequest()), requestAttributes.getResponse());
            // 线程上下文传递
            RequestContextHolder.setRequestAttributes(newRequestAttributes);
            try {
                thread.run();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        }
    }

    public class ContextAwarePoolExecutor extends ThreadPoolTaskExecutor {
        @Override
        public <T> Future<T> submit(Callable<T> task) {
            return super.submit(TtlCallable.get(new ContextAwareCallable<>(task, RequestContextHolder.currentRequestAttributes())));
        }

        @Override
        public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
            return super.submitListenable(TtlCallable.get(new ContextAwareCallable<>(task, RequestContextHolder.currentRequestAttributes())));
        }

        @Override
        public void execute(Runnable thread) {
            super.execute(TtlRunnable.get(new ContextAwareRunnable(thread, RequestContextHolder.currentRequestAttributes())));
        }
    }


    @Override
    @Bean("asyncExecutor")
    public ThreadPoolTaskExecutor getAsyncExecutor() {
        return new ContextAwarePoolExecutor();
    }

    /**
     * 极小的Request
     *
     * @author Administrator
     */
    public class TinyHttpServletRequest implements HttpServletRequest {

        private Map<String, String> headerMap = new HashMap<>();

        public TinyHttpServletRequest(HttpServletRequest request) {
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String header = request.getHeader(headerName);
                headerMap.put(headerName, header);
            }
        }

        @Override
        public String getAuthType() {
            return "";
        }

        @Override
        public Cookie[] getCookies() {
            return new Cookie[0];
        }

        @Override
        public long getDateHeader(String s) {
            return 0;
        }

        @Override
        public String getHeader(String name) {
            return headerMap.get(name);
        }

        @Override
        public Enumeration<String> getHeaders(String s) {
            return null;
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            Iterator<String> iterator = headerMap.keySet().iterator();
            return new Enumeration<String>() {
                @Override
                public boolean hasMoreElements() {
                    return iterator.hasNext();
                }

                @Override
                public String nextElement() {
                    return iterator.next();
                }
            };
        }

        @Override
        public int getIntHeader(String s) {
            return 0;
        }

        @Override
        public String getMethod() {
            return "";
        }

        @Override
        public String getPathInfo() {
            return "";
        }

        @Override
        public String getPathTranslated() {
            return "";
        }

        @Override
        public String getContextPath() {
            return "";
        }

        @Override
        public String getQueryString() {
            return "";
        }

        @Override
        public String getRemoteUser() {
            return "";
        }

        @Override
        public boolean isUserInRole(String s) {
            return false;
        }

        @Override
        public Principal getUserPrincipal() {
            return null;
        }

        @Override
        public String getRequestedSessionId() {
            return "";
        }

        @Override
        public String getRequestURI() {
            return "";
        }

        @Override
        public StringBuffer getRequestURL() {
            return null;
        }

        @Override
        public String getServletPath() {
            return "";
        }

        @Override
        public HttpSession getSession(boolean b) {
            return null;
        }

        @Override
        public HttpSession getSession() {
            return null;
        }

        @Override
        public String changeSessionId() {
            return "";
        }

        @Override
        public boolean isRequestedSessionIdValid() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromCookie() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromURL() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromUrl() {
            return false;
        }

        @Override
        public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
            return false;
        }

        @Override
        public void login(String s, String s1) throws ServletException {

        }

        @Override
        public void logout() throws ServletException {

        }

        @Override
        public Collection<Part> getParts() throws IOException, ServletException {
            return Collections.emptyList();
        }

        @Override
        public Part getPart(String s) throws IOException, ServletException {
            return null;
        }

        @Override
        public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
            return null;
        }

        @Override
        public Object getAttribute(String s) {
            return null;
        }

        @Override
        public Enumeration<String> getAttributeNames() {
            return null;
        }

        @Override
        public String getCharacterEncoding() {
            return "";
        }

        @Override
        public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

        }

        @Override
        public int getContentLength() {
            return 0;
        }

        @Override
        public long getContentLengthLong() {
            return 0;
        }

        @Override
        public String getContentType() {
            return "";
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return null;
        }

        @Override
        public String getParameter(String s) {
            return "";
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return null;
        }

        @Override
        public String[] getParameterValues(String s) {
            return new String[0];
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return Collections.emptyMap();
        }

        @Override
        public String getProtocol() {
            return "";
        }

        @Override
        public String getScheme() {
            return "";
        }

        @Override
        public String getServerName() {
            return "";
        }

        @Override
        public int getServerPort() {
            return 0;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return null;
        }

        @Override
        public String getRemoteAddr() {
            return "";
        }

        @Override
        public String getRemoteHost() {
            return "";
        }

        @Override
        public void setAttribute(String s, Object o) {

        }

        @Override
        public void removeAttribute(String s) {

        }

        @Override
        public Locale getLocale() {
            return null;
        }

        @Override
        public Enumeration<Locale> getLocales() {
            return null;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public RequestDispatcher getRequestDispatcher(String s) {
            return null;
        }

        @Override
        public String getRealPath(String s) {
            return "";
        }

        @Override
        public int getRemotePort() {
            return 0;
        }

        @Override
        public String getLocalName() {
            return "";
        }

        @Override
        public String getLocalAddr() {
            return "";
        }

        @Override
        public int getLocalPort() {
            return 0;
        }

        @Override
        public ServletContext getServletContext() {
            return null;
        }

        @Override
        public AsyncContext startAsync() throws IllegalStateException {
            return null;
        }

        @Override
        public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
            return null;
        }

        @Override
        public boolean isAsyncStarted() {
            return false;
        }

        @Override
        public boolean isAsyncSupported() {
            return false;
        }

        @Override
        public AsyncContext getAsyncContext() {
            return null;
        }

        @Override
        public DispatcherType getDispatcherType() {
            return null;
        }
    }


}

