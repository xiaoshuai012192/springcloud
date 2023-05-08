package com.springcloud.base.setting;

import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

/**
 * @Author: ls
 * @Description: 监听事件类型枚举
 * @Date: 2023/5/6 10:54
 */
public enum WatchKind {

	/**
	 * 事件丢失
	 */
	OVERFLOW(StandardWatchEventKinds.OVERFLOW),
	/**
	 * 修改事件
	 */
	MODIFY(StandardWatchEventKinds.ENTRY_MODIFY),
	/**
	 * 创建事件
	 */
	CREATE(StandardWatchEventKinds.ENTRY_CREATE),
	/**
	 * 删除事件
	 */
	DELETE(StandardWatchEventKinds.ENTRY_DELETE);

	/**
	 * 全部事件
	 */
	public static final WatchEvent.Kind<?>[] ALL = {
			OVERFLOW.getValue(), MODIFY.getValue(), CREATE.getValue(), DELETE.getValue()};

	private final WatchEvent.Kind<?> value;

	/**
	 * 构造
	 *
	 * @param value 事件类型
	 */
	WatchKind(WatchEvent.Kind<?> value) {
		this.value = value;
	}

	/**
	 * 获取枚举对应的事件类型
	 *
	 * @return 事件类型值
	 */
	public WatchEvent.Kind<?> getValue() {
		return this.value;
	}
}
