package com.auxiliary.sikuli.app;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.auxiliary.tool.data.WindowsCmdUtil;

/**
 * <p>
 * <b>文件名：</b>WindowsApp.java
 * </p>
 * <p>
 * <b>用途：</b> 定义在windows操作系统上，对应用程序进行操作的方法
 * </p>
 * <p>
 * <b>编码时间：</b>2021年12月22日 上午8:09:41
 * </p>
 * <p>
 * <b>修改时间：</b>2021年12月22日 上午8:09:41
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.0.0
 */
public class WindowsApp extends AbstractApp {
    /**
     * 获取应用PID的dos命令
     */
    private final String PID_COMMAND = "tasklist";
    /**
     * 终止应用的dos命令
     */
    private final String KILL_COMMAND = "taskkill /f /pid %s";

    @Override
    public void quit(String appName) {
        // 根据app名称，获取其相应的下标，若应用名称不存在，则抛出异常
        if (!appMap.containsKey(appName)) {
            throw new OpenAppException("不存在的应用名称：" + appName);
        }

        // 调用终止命令，结束相应应用的进程
        WindowsCmdUtil.action(String.format(KILL_COMMAND, appMap.get(appName)));
        // 移除应用名称
        removeApp(appName);
    }

    @Override
    protected List<Integer> getAppPid(String... appCourseKeys) {
        // 根据命令，获取到所有相关的返回
        String resultText = WindowsCmdUtil.action(PID_COMMAND, "GBK", true, appCourseKeys);
        // 根据正则，过滤到所有的非数字字符，再将字符串转换为数字，最后合成集合
        return Arrays.stream(resultText.split(WindowsCmdUtil.APPEND_RESULT_SIGE))
                // 由于获取的结果为整个进程的所有信息，故需要转换，只获取其中的pid值即可
                .map(result -> Arrays.stream(result.split(" ")).filter(s -> !s.isEmpty()).collect(Collectors.toList())
                        .get(1))
                .filter(pid -> pid.matches("\\d+")).map(Integer::valueOf).collect(Collectors.toList());
    }
}
