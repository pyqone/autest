package com.auxiliary.sikuli.location;

public abstract class AbstractSikuliLocation {
    /**
     * 用于存储当前查找的元素名称
     */
    protected String name = "";

    /**
     * 该方法用于在文件中，查找相应的元素并缓存元素的信息
     *
     * @param name 元素名称
     * @return 类本身
     * @since autest 3.0.0
     */
    public AbstractSikuliLocation find(String name) {
        // 存储元素名称，并使用缓存的元素名称，对元素进行查找
        this.name = name;
        findElement();

        return this;
    }

    protected abstract void findElement();
}
