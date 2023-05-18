//package com.base.sbc.config;
//
//import com.base.sbc.module.common.entity.HttpLog;
//import lombok.Data;
//
///**
// * @author 卞康
// * @date 2023/5/18 16:15:20
// * @mail 247967116@qq.com
// */
//@Data
//public final class ContextHolder extends HttpLog {
//
//	private static ThreadLocalContext context = new ThreadLocalContext();
//
//	public static ContextHolder ctx() {
//		return context.get();
//	}
//
//	/*** 清空线程*/
//	public static void remove() {
//		context.remove();
//	}
//
//	public static void setContext(ContextHolder contextHolder) {
//		context.set(contextHolder);
//	}
//
//	public static ThreadLocalContext getContext() {
//		return context;
//	}
//
//	private static class ThreadLocalContext extends ThreadLocal<ContextHolder> {
//
//		private ThreadLocalContext() {
//		}
//
//		@Override
//		protected ContextHolder initialValue() {
//			return new ContextHolder();
//		}
//	}
//}
