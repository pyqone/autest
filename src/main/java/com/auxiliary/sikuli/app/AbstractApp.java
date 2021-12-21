package com.auxiliary.sikuli.app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.sikuli.script.App;

/**
 * <p>
 * <b>文件名：</b>AbstractApp.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 定义应用启动基本的方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年12月17日 上午7:54:06
 * </p>
 * <p>
 * <b>修改时间：</b>2021年12月17日 上午7:54:06
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.0.0
 */
public abstract class AbstractApp {
    /**
     * 用于存储所有的应用程序
     */
    protected App app = new App();
    /**
     * 用于存储所有应用程序的pid值
     */
    protected List<Integer> pidList = new ArrayList<>();
    /**
     * 存储所有应用程序的名称
     */
    protected List<String> appNameList = new ArrayList<>();

    /**
     * 用于指向当前正在执行的应用名称
     */
    protected String nowAppName = "";

    /**
     * 该方法用于启动指定的应用程序
     *
     * @param appFile 应用程序启动文件类对象
     * @since autest 3.0.0
     */
    public void open(File appFile) {
        open(appFile.getName(), appFile);
    }

    /**
     * 该方法用于启动应用程序，并根据该应用程序的进程名称，获取应用程序的PID值
     * <p>
     * <b>注意：</b>搜索PID值是通过进程名称关键词，通过操作系统命令来查找，若通过关键词搜索到多个且不是通过程序打开的应用，则可能导致获取的PID值不正确。
     * </p>
     *
     * @param appName 应用名称
     * @param appFile       应用程序启动文件类对象
     * @param appCourseKey 搜索应用进程名称的关键词组
     * @since autest 3.0.0
     * @throws OpenAppException 当打开应用名称失败时，抛出的异常
     */
    public void open(String appName, File appFile, String... appCourseKey) {
        // 判断应用名称是否存在，存在则抛出异常
        if (appNameList.contains(appName)) {
            throw new OpenAppException("存在的应用名称：" + appName);
        }

        // 打开app
        App.open(appFile.getAbsolutePath());
        // 判断是否需要获取应用的pid值（应用进程名称为空）
        // 过滤数组为空的情况
        boolean isGetPid = Optional.ofNullable(appCourseKey).filter(keys -> keys.length != 0)
                // 过滤所有关键词均为空串的情况
                .filter(keys -> {
                    // 遍历关键词组，判断所有的关键词组，若存在不为空的内容，则返回true
                    for (String key : keys) {
                        if (!key.isEmpty()) {
                            return true;
                        }
                    }
                    return false;
                }).isPresent();
        if (isGetPid) {
            // 如果需要获取pid，则通过相应操作系统的命令，获取应用的pid
            List<Integer> pidList = getAppPid(appCourseKey);
            for (int i = 0; i < pidList.size(); i++) {
                if (putApp(appName, pidList.get(i))) {
                    break;
                }
            }
        } else {
            // 若无需获取pid值，则存储pid值为-1
            putApp(appName, -1);
        }

        // 将nowAppName指向为当前打开应用的名称
        nowAppName = appName;
    }

    /**
     * 该方法用于将指定的应用切换到顶层
     * <p><b>注意：</b>若当前应用并未设置PID值，则不会进行切换操作</p>
     *
     * @param appName 应用名称
     * @since autest 3.0.0
     * @throws OpenAppException 当应用名称不存在时，抛出的异常
     */
    public void switchApp(String appName) {
        // 设置pid值，并判断是否设置成功
        if (setAppPid(appName)) {
            // 若设置成功，则调用打开方法，对指定的应用进行切换
            app.open();
        }

        // 将nowAppName指向为当前切换的应用名称
        nowAppName = appName;
    }

    /**
     * 该方法用于关闭当前正在操作的应用程序
     *
     * @throws OpenAppException 当前未打开应用时，抛出的异常
     * @since autest 3.0.0
     */
    public void close() {
        // 若当前并未打开应用，则抛出异常，若存在已打开的应用，则将其关闭
        if (nowAppName.isEmpty()) {
            throw new OpenAppException("当前并未打开应用");
        }

        close(nowAppName);
    }

    /**
     * 该方法用于关闭指定的应用程序
     *
     * @since autest 3.0.0
     * @throws OpenAppException 当应用名称不存在时，抛出的异常
     */
    public void close(String appName) {
        // 设置pid值
        setAppPid(appName);
        app.close();

        // 移除相应的应用
        removeApp(appName);
    }

    /**
     * 该方法用于终止（强行结束）当前正在操作的应用程序
     *
     * @throws OpenAppException 当前未打开应用时，抛出的异常
     * @since autest
     */
    public void quit() {
        // 若当前并未打开应用，则抛出异常，若存在已打开的应用，则将其关闭
        if (nowAppName.isEmpty()) {
            throw new OpenAppException("当前并未打开应用");
        }

        quit(nowAppName);
    }

    /**
     * 该方法用于终止（强行结束）启动的应用程序
     *
     * @since autest 3.0.0
     */
    public abstract void quit(String appName);

    /**
     * 该方法用于根据应用程序在终端中的名称，查找其对应的pid值，并进行返回。若无法查找到相应的PID值，则返回只有一个-1的集合
     *
     * @param appCourseKeys 应用在终端中的名称
     * @return 应用的pid值集合
     * @since autest 3.0.0
     */
    protected abstract List<Integer> getAppPid(String... appCourseKeys);

    /**
     * 该方法用于对应用的名称和pid值进行判断，对判断通过的值进行存储，并返回是否添加成功
     *
     * @param appName 应用名称
     * @param pid 应用pid值
     * @return 应用名称是否添加成功
     * @since autest 3.0.0
     */
    protected boolean putApp(String appName, Integer pid) {
        // 判断当前应用名称是否存在
        if (appNameList.contains(appName)) {
            // 判断当前pid值是否存在，特别的，如果pid为-1（未查找到pid值），则也进行存储
            if (pidList.contains(pid) || pid == -1) {
                appNameList.add(appName);
                pidList.add(pid);
                return true;
            }
        }

        return false;
    }

    /**
     * 该方法用于根据应用名称，对存储的pid值进行切换
     *
     * @param appName app名称
     * @return 是否成功设置
     * @since autest 3.0.0
     */
    protected boolean setAppPid(String appName) {
        // 根据app名称，获取其相应的下标，若应用名称不存在，则抛出异常
        int index = appNameList.indexOf(appName);
        if (index == -1) {
            throw new OpenAppException("不存在的应用名称，无法切换应用：" + appName);
        }

        // 根据下标，获取相应的pid值，若pid为-1，则返回设置失败；为正常的数值，则设置pid，并返回成功
        int pid = pidList.get(index);
        if (pid == -1) {
            return false;
        } else {
            app.setPID(pid);
            return true;
        }
    }

    /**
     * 该方法用于根据应用名称，移除相关的应用
     *
     * @param appName 应用名称
     * @since autest 3.0.0
     */
    protected void removeApp(String appName) {
         int index = appNameList.indexOf(appName);
         appNameList.remove(index);
         pidList.remove(index);
    }
}
