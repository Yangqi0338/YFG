package com.base.sbc.config;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

/**
 * 为了拦截器获取post请求的参数
 *
 * @author xiong
 * @email 731139982@qq.com
 * @date 2021年9月2日
 */
public class BodyHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private final byte[] data;

	public BodyHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		data = IOUtils.toByteArray(request.getInputStream());
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(data);
		return new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return bais.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener listener) {

			}
		};
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	public byte[] getRequestData() {
		return this.data;
	}
}
