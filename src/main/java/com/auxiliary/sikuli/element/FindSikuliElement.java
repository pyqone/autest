package com.auxiliary.sikuli.element;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.sikuli.script.Match;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

import com.auxiliary.sikuli.location.AbstractSikuliLocation;

/**
 * <p>
 * <b>文件名：</b>FindSingleElement.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 通过指定的元素文件读取类对象（继承自{@link AbstractSikuliLocation}类的类对象），在指定的识别范围中，查找相应的单个元素。在指定的时间内，若能查找到元素，
 * 则返回相应的{@link SikuliElement}类对象
 * </p>
 * <p>
 * <b>编码时间：</b>2022年1月13日 上午8:15:49
 * </p>
 * <p>
 * <b>修改时间：</b>2022年1月13日 上午8:15:49
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.0.0
 */
public class FindSikuliElement {
    /**
     * 指向当前需要读取的元素存储文件
     */
    private AbstractSikuliLocation locationFile;
    /**
     * 指向当前识别范围类对象
     */
    private Region region;

    /**
     * 构造对象，并指定当前需要识别的屏幕范围
     *
     * @param region {@link Region}类对象，默认屏幕时，可传入{@link Screen}类对象
     * @since autest 3.0.0
     */
    public FindSikuliElement(Region region) {
        this.region = Optional.ofNullable(region).orElseThrow(() -> new FindElementException("未指定需要识别的屏幕范围"));
    }

    /**
     * 该方法用于设置当前需要读取的元素存储文件
     *
     * @param locationFile 元素文件读取类对象
     * @since autest 3.0.0
     */
    public void setReadMode(AbstractSikuliLocation locationFile) {
        this.locationFile = locationFile;
    }

    /**
     * 该方法用于根据设置的识别范围，查找对应名称的元素。若界面上识别到多个结果，则取第1个作为目标元素。
     *
     * @param name     元素名称
     * @param linkKeys 外链词语
     * @return 查找到的元素类对象
     * @since autest 3.0.0
     * @throws TimeoutException 当元素查找超时时，抛出的异常
     */
    public SikuliElement findElement(String name, String... linkKeys) {
        return findElement(name, 1, linkKeys);
    }

    /**
     * 该方法用于根据设置的识别范围，查找对应名称的元素，并返回指定下标的元素
     * <p>
     * <b>注意：</b>元素下标传入元素的真实下标，例如，当在界面识别到3个相同的元素时，需要取第2个作为目标元素，则下标传入2。
     * 另外，下标可以传入负数，表示从后向前遍历
     * </p>
     *
     * @param name     元素名称
     * @param linkKeys 外链词语
     * @return 查找到的元素类对象
     * @since autest 3.0.0
     * @throws TimeoutException 当元素查找超时时，抛出的异常
     */
    public SikuliElement findElement(String name, int index, String... linkKeys) {
        List<SikuliElement> elementList = findAllElement(name, linkKeys);
        return elementList.get(disposeElementIndex(index, elementList.size()));
    }

    /**
     * 该方法用于根据设置的识别范围，查找对应名称的元素所有元素，并返回目标元素集合
     *
     * @param name     元素名称
     * @param linkKeys 外链词语
     * @return 查找到的元素类对象
     * @since autest3.0.0
     * @throws TimeoutException 当元素查找超时时，抛出的异常
     */
    public List<SikuliElement> findAllElement(String name, String... linkKeys) {
        // 查找元素信息
        List<ElementLocationInfo> infoList = locationFile.getElementLocationList(name);
        // 根据外链词信息，将包含占位符的内容进行替换
        if (Optional.ofNullable(linkKeys).filter(arr -> arr.length != 0).isPresent()) {
            infoList.forEach(info -> {
                info.setScreenFilePath(disposeLinkKeys(info.getScreenFilePath(), linkKeys));
            });
        }

        // 在指定的操作时间内，循环调用exists方法，判断元素是否存在，若超过执行时间仍未找到元素，则抛出异常
        // TODO 此处等待时间单位为秒，故乘以1000，换算成毫秒，后续优化单位时，注意修改
        long waitTime = locationFile.getWaitTime(name) * 1000L;
        while (waitTime > 0) {
            // 记录执行开始时间
            long startTime = System.currentTimeMillis();
            // 将infoList中存储的所有元素遍历一次，直至界面上出现相应元素为止
            for (ElementLocationInfo info : infoList) {
                List<Match> matchList = region.findAllList(info.getPattern());
                if (!matchList.isEmpty()) {
                    return matchList.stream().map(match -> new SikuliElement(name, match)).collect(Collectors.toList());
                }
            }

            // 若所有的元素文件均无法查到元素，则计算剩余等待时间，重新查找元素
            waitTime += (startTime - System.currentTimeMillis());
        }

        // 若循环结束仍未查找到元素，则抛出超时异常
        // TODO 当前异常说明中的单位也写死，后续优化需要注意
        throw new TimeoutException(String.format("元素“%s”%d%s内未出现在指定的识别区域内", name, locationFile.getWaitTime(name), "秒"));
    }

    /**
     * 该方法用于处理传入的元素下标内容，下标传入元素真实下标，可传入负数，表示向后遍历
     * <ol>
     * <li>当下标绝对值大于元素总数量时，若下标为正数，则返回总数减1；反之，则返回0</li>
     * <li>当下标为正数，且小于总数时，则返回下标减1</li>
     * <li>当下标为负数，且下标绝对值小于总数时，则返回总数加上下标的值（即总数 - 下标绝对值）</li>
     * <li>当下标为0时，则在元素总数范围内，随机返回一个数</li>
     * </ol>
     *
     * @param index  传入的下标
     * @param length 元素总数量
     * @return 处理后的元素下标
     * @since autest 3.0.0
     */
    private int disposeElementIndex(int index, int length) {
        // 判断元素下标是否超出范围，若下标为负数，则返回0；否则，返回元素总个数减1
        if (Math.abs(index) >= length) {
            if (index > 0) {
                return length - 1;
            } else {
                return 0;
            }
        }

        // 判断index的值，若大于0，则从前向后遍历，若小于0，则从后往前遍历，若等于0，则随机输入
        if (index > 0) {
            // 选择元素，正数的选项值从1开始，故需要减小1
            return index - 1;
        } else if (index < 0) {
            // 选择元素，由于index为负数，则长度加上选项值即可得到需要选择的选项
            return length + index;
        } else {
            return new Random().nextInt(length);
        }
    }

    /**
     * 该方法用于使用外链词语，替换字符串中的剩余占位符
     *
     * @param fileName 待替换字符串
     * @param linkKeys 外链词语
     * @return 替换后的词语
     * @since autest 3.0.0
     */
    private String disposeLinkKeys(String fileName, String... linkKeys) {
        // 定义对占位符进行判断的正则表达式
        String regex = String.format("%s.*?%s", locationFile.getStartElementPlaceholder(),
                locationFile.getEndElementPlaceholder());

        // 判断当前字符串中是否包含正则表达式，若未包含，则返回原字符串
        if (!fileName.matches(String.format(".*%s.*", regex))) {
            return fileName;
        }

        // 获取元素定位内容
        StringBuilder locationText = new StringBuilder(fileName);

        // 定义正则表达式类对象
        Pattern pattern = Pattern.compile(regex);
        Matcher mather = pattern.matcher(locationText);

        // 根据正则查找相应的数据，对该数据进行替换
        for (int index = 0; index < linkKeys.length && mather.find(); index++) {
            // 存储替换符的开始和结束位置
            int replaceStartIndex = mather.start();
            int replaceEndIndex = mather.end();

            // 对当前位置的词语进行替换
            locationText.replace(replaceStartIndex, replaceEndIndex, linkKeys[index]);
            mather = pattern.matcher(locationText);
        }

        // 存储当前替换后的定位内容
        return locationText.toString();
    }
}
