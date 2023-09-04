package com.auxiliary.tool.common;

import java.util.Optional;

/**
 * <p>
 * <b>文件名：Entry.java</b>
 * </p>
 * <p>
 * <b>用途：</b>提供单一键值对类对象，以便于根据存储键值对形式的数据
 * </p>
 * <p>
 * <b>编码时间：2022年3月31日 上午9:01:58
 * </p>
 * <p>
 * <b>修改时间：2022年3月31日 上午9:01:58
 * </p>
 *
 * @param <K> 键
 * @param <V> 值
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 3.3.0
 */
public class Entry<K, V> {
    /**
     * 键
     */
    private K key;
    /**
     * 值
     */
    private V value;

    /**
     * 构造对象，并初始化键值对
     *
     * @param key   键
     * @param value 值
     * @since autest 3.3.0
     */
    public Entry(K key, V value) {
        super();
        this.key = key;
        this.value = value;
    }

    /**
     * 该方法用于返回值中存储的内容
     *
     * @return 值
     * @since autest 3.3.0
     */
    public V getValue() {
        return value;
    }

    /**
     * 该方法用于返回值的封装类对象
     *
     * @return 值的封装类对象
     * @since autest 3.3.0
     */
    public Optional<V> getOptionValue() {
        return Optional.ofNullable(value);
    }

    /**
     * 该方法用于设置值的内容
     *
     * @param value 值
     * @since autest 3.3.0
     */
    public void setValue(V value) {
        this.value = value;
    }

    /**
     * 该方法用于返回键中存储的内容
     *
     * @return 键
     * @since autest 3.3.0
     */
    public K getKey() {
        return key;
    }

    /**
     * 该方法用于返回键的封装类对象
     *
     * @return 键的封装类对象
     * @since autest 3.3.0
     */
    public Optional<K> getOptionalKey() {
        return Optional.ofNullable(key);
    }

    @Override
    public String toString() {
        return "[key=" + key + ", value=" + value + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        Entry<K, V> other = (Entry<K, V>) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }
}
