package com.asiainfo.lcims.omc.param.common;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

public class SessionUtils {
	/**
	 * 设置session
	 * @param key
	 * @param value
	 */
	public static void setSession(Object key, Object value) {
		Session session = getSession();
		if (session != null) {
			session.setAttribute(key, value);
		}
	}

	/**
	 * 从session中获取信息
	 * @param key
	 * @return
	 */
	public static Object getFromSession(String key) {
		Session session = getSession();
		if (session != null) {
			return session.getAttribute(key);
		}
		return null;
	}

	/**
	 * 获取session
	 * @return
	 */
	public static Session getSession() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null)
			return subject.getSession();
		return null;
	}

	/**
	 * 从session中删除信息
	 * @param key
	 */
	public static void removeFromSession(String key) {
		Session session = getSession();
		if (session != null) {
			session.removeAttribute(key);
		}
	}

	/**
	 * 退出清空session
	 * @return
	 */
	public static void clearSession() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null){
			 subject.logout();
		}
	}
}
