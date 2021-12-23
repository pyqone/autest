package com.auxiliary.sikuli.app;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.sikuli.script.App;

import com.google.common.base.Objects;

/**
 * <p>
 * <b>文件名：</b>AbstractApp.java
 * </p>
 * <p>
 * <b>用途：</b> 定义应用启动基本的方法
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
     * 定义默认的等待时间
     */
    protected final int DEFAULT_WAIT_TIME = 5;

    /**
     * 用于存储所有的应用程序
     */
    protected App app = new App();
    /**
     * 存储应用程序相关的信息，键为应用名称，值为应用pid
     */
    protected HashMap<String, Integer> appMap = new HashMap<>();

    /**
     * 用于指向当前正在执行的应用名称
     */
    protected String nowAppName = "";
    /**
     * 存储应用启动后的等待时间
     */
    protected int openWaitTime = DEFAULT_WAIT_TIME;

    /**
     * 该方法用于启动指定的应用程序，并将文件名称作为应用的名称
     *
     * @param appFile 应用程序启动文件类对象
     * @since autest 3.0.0
     */
    public void open(File appFile, String... appCourseKey) {
        if (appFile == null) {
            throw new OpenAppException("未指定应用启动路径");
        }
        open(appFile.getName(), appFile, appCourseKey);
    }

    /**
     * 该方法用于启动应用程序，并根据该应用程序的进程名称，获取应用程序的PID值
     * <p>
     * <b>注意：</b>搜索PID值是通过进程名称关键词，通过操作系统命令来查找，若通过关键词搜索到多个且不是通过程序打开的应用，则可能导致获取的PID值不正确。
     * </p>
     *
     * @param appName      应用名称
     * @param appFile      应用程序启动文件类对象
     * @param appCourseKey 搜索应用进程名称的关键词组
     * @since autest 3.0.0
     * @throws OpenAppException 当打开应用名称失败时，抛出的异常
     */
    public void open(String appName, File appFile, String... appCourseKey) {
        // 判断基本的信息
        Optional.ofNullable(appName).filter(name -> !name.isEmpty()).orElseThrow(() -> new OpenAppException("未指定应用名称"));
        Optional.ofNullable(appName).orElseThrow(() -> new OpenAppException("未指定应用启动路径"));
        // 判断应用名称是否存在，存在则抛出异常
        if (appMap.containsKey(appName)) {
            throw new OpenAppException("存在的应用名称：" + appName);
        }

        // 打开app
        App.open(appFile.getAbsolutePath(), openWaitTime);
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

            // 判断当前是否存储成功，若未成功，则表示无法搜索到相应的pid，故存储-1
            if (!appMap.containsKey(appName)) {
                putApp(appName, -1);
            }
        } else {
            // 若无需获取pid值，则存储pid值为-1
            putApp(appName, -1);
        }

        // 将nowAppName指向为当前打开应用的名称
        nowAppName = appName;
    }

    /**
     * 该方法用于设置应用启动的等待时间，单位为秒，默认为5秒
     * <p>
     * <b>注意：</b>该方法设置为小于0的参数时，则使用默认值
     * </p>
     *
     * @param openWaitTime 应用启动等待时间
     * @since autest 3.0.0
     */
    public void setOpenWaitTime(int openWaitTime) {
        if (openWaitTime < 0) {
            this.openWaitTime = DEFAULT_WAIT_TIME;
        }
        this.openWaitTime = openWaitTime;
    }

    /**
     * 该方法用于将指定的应用切换到顶层
     * <p>
     * <b>注意：</b>若当前应用并未设置PID值，则不会进行切换操作
     * </p>
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
            throw new OpenAppException("当前未打开应用程序");
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
        // 移除应用名称
        removeApp(appName);
    }

    /**
     * 该方法用于终止（强行结束）当前正在操作的应用程序
     *
     * @throws OpenAppException 当前未打开应用时，抛出的异常
     * @since autest 3.0.0
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
     * @param appName 应用名称
     * @since autest 3.0.0
     */
    public abstract void quit(String appName);

    /**
     * 该方法用于终止（强行结束）所有启动的应用程序
     *
     * @since autest 3.0.0
     */
    public void quitAll() {
        Set<String> keySet = new HashSet<>(appMap.keySet());
        for (String key : keySet) {
            quit(key);
        }
    }

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
     * @param pid     应用pid值
     * @return 应用名称是否添加成功
     * @since autest 3.0.0
     */
    protected boolean putApp(String appName, Integer pid) {
        // 判断当前应用名称是否存在
        if (!appMap.containsKey(appName)) {
            Collection<Integer> pidList = appMap.values();
            // 判断当前pid值是否存在，特别的，如果pid为-1（未查找到pid值），则也进行存储
            if (!pidList.contains(pid) || pid == -1) {
                appMap.put(appName, pid);
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
        if (!appMap.containsKey(appName)) {
            throw new OpenAppException("不存在的应用名称：" + appName);
        }

        // 根据下标，获取相应的pid值，若pid为-1，则返回设置失败；为正常的数值，则设置pid，并返回成功
        int pid = appMap.get(appName);
        if (pid == -1) {
            return false;
        } else {
            app.setPID(pid);
            return true;
        }
    }

    /**
     * 该方法用于移除指定名称的应用，若移除的应用为当前正在操作的应用，则将nowAppName置空
     *
     * @param appName 应用名称
     * @since autest 3.0.0
     */
    protected void removeApp(String appName) {
        // 移除相应的应用
        appMap.remove(appName);
        // 若关闭的应用是当前正在执行的应用，则将执行应用置空
        if (Objects.equal(appName, nowAppName)) {
            nowAppName = "";
        }
    }
}
