package com.base.sbc.config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 为了拦截器获取post响应的参数
 *
 * @author xiong
 * @email 731139982@qq.com
 * @date 2021年9月2日
 */
public class BodyHttpServletResponseWrapper extends HttpServletResponseWrapper {

	private ByteArrayOutputStream outputStream;

	public BodyHttpServletResponseWrapper(HttpServletResponse response) {
		super(response);
		this.outputStream = new ByteArrayOutputStream();
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(outputStream);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return new ServletOutputStream() {
			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setWriteListener(WriteListener listener) {

			}

			@Override
			public void write(int b) throws IOException {
				outputStream.write(b);
			}
		};
	}

	public byte[] getResponseData() {
		try {
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}

}
