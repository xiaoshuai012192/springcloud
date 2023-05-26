package com.spring.cloud.base.utils.utils;

import com.spring.cloud.base.utils.Html4Escape;
import com.spring.cloud.base.utils.Html4Unescape;
import com.spring.cloud.base.utils.XmlEscape;
import com.spring.cloud.base.utils.XmlUnescape;
import com.spring.cloud.base.utils.interf.Filter;
import com.spring.cloud.base.utils.str.StrUtil;

/**
 * @Author: ls
 * @Description: 转义和反转义工具类Escape
 * @Date: 2023/4/16 16:11
 */
public class EscapeUtil {

	/**
	 * 不转义的符号编码
	 */
	private static final String NOT_ESCAPE_CHARS = "*@-_+./";

	private static final Filter<Character> JS_ESCAPE_FILTER
			= c -> !(Character.isDigit(c) || Character.isLowerCase(c) || Character.isUpperCase(c) || StrUtil.contains(NOT_ESCAPE_CHARS, c));

	/**
	 * 转义XML中的特殊字符
	 *
	 * @param xml XML文本
	 * @return 转义后的文本
	 */
	public static String escapeXml(CharSequence xml) {
		XmlEscape escape = new XmlEscape();
		return escape.replace(xml).toString();
	}

	/**
	 * 反转义XML中的特殊字符
	 *
	 * @param xml XML文本
	 * @return 转义后的文本
	 */
	public static String unescapeXml(CharSequence xml) {
		XmlUnescape unescape = new XmlUnescape();
		return unescape.replace(xml).toString();
	}

	/**
	 * 转义HTML4中的特殊字符
	 *
	 * @param html HTML文本
	 * @return 转义后的文本
	 */
	public static String escapeHtml4(CharSequence html) {
		Html4Escape escape = new Html4Escape();
		return escape.replace(html).toString();
	}

	/**
	 * 反转义HTML4中的特殊字符
	 *
	 * @param html HTML文本
	 * @return 转义后的文本
	 *
	 */
	public static String unescapeHtml4(CharSequence html) {
		Html4Unescape unescape = new Html4Unescape();
		return unescape.replace(html).toString();
	}

	/**
	 * Escape编码（Unicode）（等同于JS的escape()方法）
	 *
	 * @param content 被转义的内容
	 * @return 编码后的字符串
	 */
	public static String escape(CharSequence content) {
		return escape(content, JS_ESCAPE_FILTER);
	}

	/**
	 * Escape编码（Unicode）
	 *
	 * @param content 被转义的内容
	 * @return 编码后的字符串
	 */
	public static String escapeAll(CharSequence content) {
		return escape(content, c -> true);
	}

	/**
	 * Escape编码（Unicode）
	 *
	 * @param content 被转义的内容
	 * @param filter  编码过滤器，对于过滤器中accept为false的字符不做编码
	 * @return 编码后的字符串
	 */
	public static String escape(CharSequence content, Filter<Character> filter) {
		if (StrUtil.isEmpty(content)) {
			return StrUtil.str(content);
		}
		final StringBuilder tmp = new StringBuilder(content.length() * 6);
		char c;
		for (int i = 0; i < content.length(); i++) {
			c = content.charAt(i);
			if (!filter.accept(c)) {
				tmp.append(c);
			} else if (c < 256) {
				tmp.append("%");
				if (c < 16) {
					tmp.append("0");
				}
				tmp.append(Integer.toString(c, 16));
			} else {
				tmp.append("%u");
				if (c <= 0xfff) {
					// issue#I49JU8@Gitee
					tmp.append("0");
				}
				tmp.append(Integer.toString(c, 16));
			}
		}
		return tmp.toString();
	}

	/**
	 * Escape解码
	 *
	 * @param content 被转义的内容
	 * @return 解码后的字符串
	 */
	public static String unescape(String content) {
		if (StrUtil.isBlank(content)) {
			return content;
		}
		StringBuilder tmp = new StringBuilder(content.length());
		int lastPos = 0;
		int pos;
		char ch;
		while (lastPos < content.length()) {
			pos = content.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (content.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(content.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(content.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(content.substring(lastPos));
					lastPos = content.length();
				} else {
					tmp.append(content, lastPos, pos);
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	/**
	 * 安全的unescape文本，当文本不是被escape的时候，返回原文。
	 *
	 * @param content 内容
	 * @return 解码后的字符串，如果解码失败返回原字符串
	 */
	public static String safeUnescape(String content) {
		try {
			return unescape(content);
		} catch (Exception e) {
			// Ignore Exception
		}
		return content;
	}
}