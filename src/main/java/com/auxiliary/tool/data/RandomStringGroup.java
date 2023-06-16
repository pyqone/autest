package com.auxiliary.tool.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import com.auxiliary.tool.common.DisposeCodeUtils;
import com.auxiliary.tool.regex.ConstType;

/**
 * <p>
 * <b>文件名：RandomStringGroup.java</b>
 * </p>
 * <p>
 * <b>用途：</b>用于对组合型的随机字符串进行生成的工具。其可对多组随机字符串进行组合，根据每组随机字符串中的默认生成字符串的方法，
 * 以及传入生成随机字符串方法的顺序，生成需要组合的随机字符串。
 * </p>
 * <p>
 * <b>编码时间：2023年6月8日 下午3:01:43
 * </p>
 * <p>
 * <b>修改时间：2023年6月8日 下午3:01:43
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since JDK 1.8
 * @since autest 4.3.0
 */
public class RandomStringGroup {
    /**
     * 文本模板中，需要替换的占位符内容
     * 
     * @since autest 4.3.0
     */
    private final String TEMP_PLACEHOLDER = "%s";

    /**
     * 存储随机字符串组
     * 
     * @since autest 4.3.0
     */
    private Map<String, RandomString> randomStringMap = new HashMap<>(ConstType.DEFAULT_MAP_SIZE);

    /**
     * 用于生成随机数字，避免每次调用方法时均要生成一个对象
     * 
     * @since autest 4.3.0
     */
    private Random randomNum = new Random();

    /**
     * 该方法用于添加随机字符串组
     * 
     * @param groupName    随机字符串组名称
     * @param randomString 随机字符串类对象
     * @return 类本身
     * @since autest 4.3.0
     */
    public RandomStringGroup addRandomString(String groupName, RandomString randomString) {
        if (groupName != null && !groupName.isEmpty() && randomString != null) {
            randomStringMap.put(groupName, randomString);
        }
        
        return this;
    }

    /**
     * 该方法用于返回当前组名称对应的随机字符串类对象
     * 
     * @param groupName 组名称
     * @return 随机字符串类对象
     * @since autest 4.3.0
     * @throws IllegalDataException 当传入的组名称不存在时，抛出的异常
     */
    public RandomString getRandomString(String groupName) {
        // 若当前的名称无法找到对应的随机字符串类，则抛出异常
        if (!randomStringMap.containsKey(groupName)) {
            throw new IllegalDataException("不存在的随机字符串组名称：" + groupName);
        }

        return randomStringMap.get(groupName);
    }

    /**
     * 该方法用于根据指定的模板，和随机字符串的最小、最大生成长度，以及参与随机的字符串组，生成相应的随机字符串
     * <p>
     * <b>注意：</b>
     * <ol>
     * <li>在传入最小与最大生成长度后，其会根据随机字符串类对象的默认长度再次进行判断，若随机字符串类对象的总默认最小生成长度大于传入的最小生成长度
     * （或者传入最大生成长度大于随机字符串类对象的总默认最大长度），则最终会以默认的总长度取值区间随机生成一个长度，特别的，
     * 若传入的长度最大值小于总默认最小值（或长度最小值大于总默认长度最大值），则抛出异常</li>
     * <li>模板中替换随随机字符串的占位符为“%s”，其占位符号必须与传入的名称组数量对应，否则将会抛出异常（模板为空时则不进行判断）</li>
     * <li>模板中已存在的字符（除占位符）不被计入生成的随机字符串的长度中，若直接判断最终生成的长度，则可能会超出传入的长度区间，需要在传入时自行扣除相应的内容长度</li>
     * <li>当传入的最小或最大值小于1时，则表示该参数按照所有随机字符串的总默认值取</li>
     * </ol>
     * </p>
     * 
     * @param minLength  随机字符串最小生成长度
     * @param maxLength  随机字符串最大生成长度
     * @param templet    模板
     * @param groupNames 名称组
     * @return 替换模板后的随机字符串组合
     * @since autest 4.3.0
     * @throws IllegalDataException 当名称组为空、名称未对应随机字符串类对象、名称组数量与模板占位符数量不符、传入的区间在随机字符串默认总区间外时抛出的异常
     */
    public String toString(int minLength, int maxLength, String templet, String... groupNames) {
        // 判断是否传入组名称
        if (groupNames == null || groupNames.length == 0) {
            throw new IllegalDataException("名称组为空，无法生成随机字符串");
        }

        // 处理模板，避免存在传入null的问题
        templet = Optional.ofNullable(templet).orElse("");
        // 若存在模板，则判断模板中的占位符个数
        if (!templet.isEmpty()) {
            // 统计模板中占位符的个数
            int count = DisposeCodeUtils.countOccurrences(templet = Optional.ofNullable(templet).orElse(""),
                    TEMP_PLACEHOLDER);
            // 判断传入的组名称是否足够对其进行替换
            if (count != groupNames.length) {
                throw new IllegalDataException(String.format("组名称个数与模板占位符个数不符。组名称个数：%d；占位符（%s）个数：%d",
                        groupNames.length, TEMP_PLACEHOLDER, count));
            }
        }

        // 若最大值小于等于0（无限制最大值），则对其进行转换（最小值无限制无需转换）
        if (maxLength < 1) {
            maxLength = Integer.MAX_VALUE;
        }
        // 若最大值小于最小值，则对两个数字进行转换
        if (maxLength < minLength) {
            int temp = minLength;
            minLength = maxLength;
            maxLength = temp;
        }

        // 将数组转换为集合
        List<String> groupNameList = new ArrayList<>(Arrays.asList(groupNames));
        // 存储随机字符串类的默认最小和最大长度
        int totalRandomMinLength = 0;
        int totalRandomMaxLength = 0;

        // 存储转换后的随机字符串
        String[] randomStrings = new String[groupNameList.size()];
        // 存储已完全生成内容的随机字符串组序号
        Set<Integer> exsitStringGroupSet = new HashSet<>();
        // 存储当前生成的字符串总长度
        int totalRandomStringLength = 0;
        // 循环，转换名称并存储随机字符串类对象，计算随机字符串默认生成的最大、最小字符串长度
        for (int index = 0; index < groupNameList.size(); index++) {
            // 获取随机字符串类对象，若名称不存在，则此处会异常
            RandomString rs = getRandomString(groupNameList.get(index));
            totalRandomMinLength += rs.getDefaultMinLength();
            totalRandomMaxLength += rs.getDefaultMaxLength();
            
            // 判断默认最大值与最小值是否一致，一致则直接生成随机字符串，并存储相应的余量
            if (rs.getDefaultMinLength() == rs.getDefaultMaxLength()) {
                randomStrings[index] = rs.toString();
                exsitStringGroupSet.add(index);
                totalRandomStringLength += rs.getDefaultMinLength();
            }
        }
        
        // 判断当前存储的随机字符串默认最大、最小默认生成长度是否与实际需要的长度相符
        // 若当前总最小值大于传入的最大值或当前总最大值小于传入的最小值，则抛出异常
        if (totalRandomMinLength > maxLength) {
            throw new IllegalDataException(
                    String.format("所有随机字符串类对象默认最小生成长度（%d）大于传入的随机字符串最大长度（%d）", totalRandomMinLength, maxLength));
        }
        if (totalRandomMaxLength < minLength) {
            throw new IllegalDataException(
                    String.format("所有随机字符串类对象默认最大生成长度（%d）小于传入的随机字符串最小长度（%d）", totalRandomMaxLength, minLength));
        }
        // 若当前的总最小值大于传入的最小值或总最大值小于传入的最大值（包含关系），则转换传入的最大、最小值
        minLength = Math.max(totalRandomMinLength, minLength);
        maxLength = Math.min(totalRandomMaxLength, maxLength);

        // 若当前最大最小值不相等，则生成当前字符串区间内的一个随机长度
        int length = (minLength == maxLength ? maxLength : randomNum.nextInt(maxLength - minLength + 1) + minLength);


        // 存储能继续插入随机字符串类对象，值为剩余量（最大量 - 已生成的随机字符串数量）
        List<RandomStringGroupData> exsitZoneGroupList = new ArrayList<>();
        // 再次遍历并生成每个组的随机字符串
        for (int index = 0; index < groupNameList.size(); index++) {
            // 若已生成随机字符串，则跳过当前下标
            if (exsitStringGroupSet.contains(index)) {
                continue;
            }
            
            // 计算剩余量的平均值，以保证每个随机字符串均能被取到
            int avg = (length - totalRandomStringLength) / (groupNameList.size() - exsitStringGroupSet.size());
            
            // 直接调用结合中的获取方法，可跳过一次空判断（此前已做过一次判断，此处无需再做判断）
            RandomString rs = randomStringMap.get(groupNameList.get(index));

            // 根据平均值，生成对应的长度下的随机字符串
            String randomString = "";
            if (rs.getDefaultMinLength() > avg) {
                randomString = rs.toString(rs.getDefaultMinLength());
            } else if (rs.getDefaultMinLength() <= avg && avg <= rs.getDefaultMaxLength()) {
                randomString = rs.toString(rs.getDefaultMinLength(), avg);
            } else {
                randomString = rs.toString();
            }

            // 存储字符串，并计算总长
            randomStrings[index] = randomString;
            totalRandomStringLength += randomString.length();
            
            // 计算当前随机字符串是否还有余量，若存在余量，则进行存储
            int diff = rs.getDefaultMaxLength() - randomString.length();
            if (diff > 0) {
                exsitZoneGroupList.add(new RandomStringGroupData(rs, diff, index));
            }

            exsitStringGroupSet.add(index);
        }

        // 判断当前生成的总长度是否满足生成的长度，若不满足，则按照剩余个数继续补足
        while (totalRandomStringLength < length) {
            // 打乱集合顺序，取乱序后的第一个元素（来自ChatGPT的改进建议）
            Collections.shuffle(exsitZoneGroupList);
            RandomStringGroupData group = exsitZoneGroupList.get(0);

            // 根据余量，生成随机字符串，并进行拼接
            String randomString = group.rs.toString(1, length - totalRandomStringLength);
            // 将生成的随机字符串进行拼接
            randomStrings[group.index] += randomString;
            totalRandomStringLength += randomString.length();
            
            // 重新计算余量，若余量不足，则将其移除exsitZoneGroupList集合
            int diff = group.surplusLength - randomString.length();
            if (diff == 0) {
                exsitZoneGroupList.remove(0);
            } else {
                group.surplusLength = diff;
            }
        }
        
        // 判断模板是否为空
        if (templet.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String str : randomStrings) {
                sb.append(str);
            }
            return sb.toString();
        } else {
            return String.format(templet, (Object[]) randomStrings);
        }
    }

    /**
     * <p>
     * <b>文件名：RandomStringGroup.java</b>
     * </p>
     * <p>
     * <b>用途：</b>定义随机字符串组的数据
     * </p>
     * <p>
     * <b>编码时间：2023年6月15日 下午3:16:05
     * </p>
     * <p>
     * <b>修改时间：2023年6月15日 下午3:16:05
     * </p>
     *
     * @author 彭宇琦
     * @version Ver1.0
     * @since JDK 1.8
     * @since autest 4.3.0
     */
    private class RandomStringGroupData {
        /**
         * 存储组名称
         * 
         * @since autest 4.3.0
         */
        public RandomString rs;
        /**
         * 存储当前随机字符串剩余长度
         * 
         * @since autest 4.3.0
         */
        public int surplusLength = 0;
        /**
         * 存储当前组在集合中的下标，以便于快速定位
         * 
         * @since autest 4.3.0
         */
        public int index = -1;

        /**
         * 构造对象，初始化可随机的组数据
         * 
         * @param groupName     组名称
         * @param surplusLength 随机字符串剩余长度
         * @param index         集合中的下标
         * @since autest 4.3.0
         */
        public RandomStringGroupData(RandomString rs, int surplusLength, int index) {
            this.rs = rs;
            this.surplusLength = surplusLength;
            this.index = index;
        }
    }
}
