package com.auxiliary.tool.date;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**
 * <p>
 * <b>文件名：</b>Time.java
 * </p>
 * <p>
 * <b>用途：</b>提供对时间进行转换相关的工具
 * </p>
 * <p>
 * <b>编码时间：</b>2019年12月2日下午5:15:55
 * </p>
 * <p>
 * <b>修改时间：</b>2023年5月6日 上午11:15:40
 * </p>
 * 
 * @author 彭宇琦
 * @version Ver1.1
 * @since JDK 1.8
 * @since autest 2.0.0
 */
public class Time implements Comparable<Time> {
    /**
     * 定义默认时区
     */
    public static ZoneId defaultZoneId = ZoneId.systemDefault();

    /**
     * 指向初始化时设置的时间
     */
    private LocalDateTime initTime;
    /**
     * 指向根据初始化时间计算后得到的时间
     */
    private LocalDateTime calculateTime;

    /**
     * 用于存储日期的格式，默认格式为yyyy-MM-dd HH:mm:ss
     */
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 定义日期约束类型的传入格式
     */
    private final static String REGEX_DATE = "(\\D*((\\d{1,2})|(\\d{4}))\\D+\\d{1,2}\\D+\\d{1,2})((\\D+\\d{1,2}){3})?\\D*";

    /**
     * 初始化日期/时间
     * 
     * @param initTime 日期/时间
     * @since autest 2.0.0
     */
    private Time(LocalDateTime initTime) {
        this.initTime = initTime;
        this.calculateTime = initTime;
    }

    /**
     * 用于根据{@link Date}类对象初始化时间
     * 
     * @param date Date类对象
     * @return 初始化后的类对象
     * @since autest 2.0.0
     */
    public static Time parse(Date date) {
        return parse(Optional.ofNullable(date).orElse(new Date()).getTime());
    }

    /**
     * 用于根据毫秒数初始化时间
     * 
     * @param ms 时间戳（毫秒值）
     * @return 初始化后的类对象
     * @since autest 2.0.0
     */
    public static Time parse(long ms) {
        // 转换时间戳
        return new Time(LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), defaultZoneId));
    }

    /**
     * 用于根据已格式化的时间初始化时间
     * 
     * @param formatTime 已格式化的时间
     * @return 初始化后的类对象
     * @throws IncorrectConditionException 时间转换错误时抛出的异常
     * @since autest 2.0.0
     */
    public static Time parse(String formatTime) {
        // 判断传入的格式化时间是否符合要求，并将其转换为格式化字符串
        return parse(formatTime,
                Optional.ofNullable(formatTime).filter(text -> !text.isEmpty()).filter(text -> text.matches(REGEX_DATE))
                        .map(Time::judgeDateFormatText)
                        .orElseThrow(() -> new IncorrectConditionException("时间“" + formatTime + "”不符合格式的规则")));
    }

    /**
     * 用于根据格式化的日期/时间，及相应的时间格式，初始化日期/时间
     * <p>
     * 该方法允许只传入格式化的日期或者时间，如： <code><pre>
     * Time time1 = Time.parse("2020-12-12", "yyyy-MM-dd");//初始化为2020年12月12日的0点
     * Time time2 = Time.parse("15:15:15", "HH:mm:ss");//初始化为当天的15时15分15秒
     * </pre></code>
     * </p>
     * 
     * @param formatTime 格式化的日期/时间
     * @param formatText 时间格式
     * @return 初始化后的类对象
     * @throws DateTimeParseException 日期/时间无法转换时抛出的异常
     * @since autest 2.0.0
     */
    public static Time parse(String formatTime, String formatText) {
        // 定义相应的时间格式，并用于解析传入的时间
        dateFormat = DateTimeFormatter.ofPattern(formatText);
        try {
            return new Time(LocalDateTime.parse(formatTime, dateFormat));
        } catch (DateTimeParseException e) {
            if (formatText.matches(".*M+.*")) {
                return new Time(LocalDate.parse(formatTime, dateFormat).atStartOfDay());
            } else {
                return new Time(LocalTime.parse(formatTime, dateFormat).atDate(LocalDate.now()));
            }
        }
    }

    /**
     * 用于根据{@link LocalDateTime}对象初始化日期/时间，若未传入时间或时间写入有误，则初始化为当前时间
     * 
     * @param dateTime 指定的{@link LocalDateTime}对象
     * @return 初始化后的类对象
     * @since autest 2.0.0
     */
    public static Time parse(LocalDateTime dateTime) {
        return new Time(Optional.ofNullable(dateTime).orElse(LocalDateTime.now()));
    }

    /**
     * 用于将时间初始化为当前时间
     * 
     * @return 初始化后的类对象
     * @since autest 2.0.0
     */
    public static Time parse() {
        return new Time(LocalDateTime.now());
    }

    /**
     * 用于对日期/时间中的指定单位进行赋值
     * 
     * @param timeNum  指定的数值
     * @param timeUnit 时间单位枚举{@link TimeUnit}
     * @return 类本身
     * @throws IncorrectConditionException 数值无法被赋入相应的单位下时抛出的异常
     * @since autest 2.0.0
     */
    public Time setTime(int timeNum, TimeUnit timeUnit) {
        // 记录每个时间下的数值
        int year = calculateTime.getYear();
        int month = calculateTime.getMonthValue();
        int day = calculateTime.getDayOfMonth();
        int hour = calculateTime.getHour();
        int minute = calculateTime.getMinute();
        int second = calculateTime.getSecond();

        // 根据枚举，对相应的日期进行赋值
        switch (timeUnit) {
        case YEAR:
            year = timeNum;
            break;
        case MONTH:
            month = timeNum;
            break;
        case DAY:
            day = timeNum;
            break;
        case HOUR:
            hour = timeNum;
            break;
        case MINUTE:
            minute = timeNum;
            break;
        case SECOND:
            second = timeNum;
            break;
        default:
            break;
        }

        // 格式化时间，若时间无法被写入，则抛出IncorrectConditionException异常
        try {
            calculateTime = LocalDateTime.of(year, month, day, hour, minute, second);
        } catch (Exception e) {
            throw new IncorrectConditionException(
                    String.format("不存在的日期：%d-%d-%d %d:%d:%d", year, month, day, hour, minute, second), e);
        }

        return this;
    }

    /**
     * 设置返回时间的格式，该方法可传入时间格式，亦可向该方法中传入时间格式的模板，
     * 通过识别模板得到日期的格式，但作为模板的日期也必须满足时间格式。例如：<br>
     * 
     * <pre>
     * <code>
     * Time time = new Time(1575387800000L);
     * 
     * time.setTimeFormat("yyyy年MM月dd日 HH:mm:ss");
     * getFormatTime();//输出：2019年12月03日 23:43:20
     * 
     * time.setTimeFormat("2019/12/04 03:03:20");
     * getFormatTime();//输出：2019/12/03 23:43:20
     * </code>
     * </pre>
     * 
     * <p>
     * <b>注意</b>
     * <ol>
     * <li>传入已格式化的时间时，其不会改变当前存储的时间</li>
     * <li>已格式化的时间中，其分隔符不能包含字母，否则转译将出错（在格式化时间的方法中也不允许存在字母）</li>
     * </ol>
     * </p>
     * 
     * @param pattern 指定的格式或已格式化的时间
     * @return 类本身
     * @since autest 2.0.0
     */
    public Time setTimeFormat(String pattern) {
        dateFormat = pattern2DateTimeFormatter(pattern);

        return this;
    }

    /**
     * 该方法用于将时间格式化文本转换为{@link DateTimeFormatter}类对象
     * 
     * @param pattern 时间格式
     * @return 转换后的类对象
     * @since autest 4.2.0
     */
    private DateTimeFormatter pattern2DateTimeFormatter(String pattern) {
        pattern = Optional.ofNullable(pattern).filter(text -> !text.isEmpty())
                .orElseThrow(() -> new IncorrectConditionException("未指定时格式"));

        if (pattern.matches(REGEX_DATE)) {
            try {
                return DateTimeFormatter.ofPattern(judgeDateFormatText(pattern));
            } catch (IncorrectConditionException e) {
                // 若转换时出现异常，则按照基本的方式进行转换
                return DateTimeFormatter.ofPattern(pattern);
            }
        } else {
            return DateTimeFormatter.ofPattern(pattern);
        }
    }

    /**
     * 用于返回Date类对象
     * 
     * @return Date类对象
     * @since autest 2.0.0
     */
    public Date getDate() {
        return Date.from(calculateTime.atZone(defaultZoneId).toInstant());
    }

    /**
     * 用于返回设置的时间的时间戳
     * 
     * @return 时间戳
     * @since autest 2.0.0
     */
    public long getMilliSecond() {
        return calculateTime.atZone(defaultZoneId).toInstant().toEpochMilli();
    }

    /**
     * 用于以指定的格式返回格式化后的时间
     * <p>
     * 若未设置时间格式，则默认按照“yyyy-MM-dd HH:mm:ss”的格式进行返回
     * </p>
     * 
     * @return 格式化后的时间
     * @since autest 2.0.0
     */
    public String getFormatTime() {
        return calculateTime.format(dateFormat);
    }

    /**
     * 该方法用于以临时指定的格式输出当前设置的日期时间，通过该方法设置的格式化时间不会影响通过{@link #setTimeFormat(String)}方法设置的时间格式，
     * 具体的设置方法可参考{@link #setTimeFormat(String)}
     * 
     * @param timeRegex 格式化规则
     * @return 格式化后的日期时间
     * @since autest 4.2.0
     */
    public String getFormatTime(String pattern) {
        return calculateTime.format(pattern2DateTimeFormatter(pattern));
    }

    /**
     * 用于以{@link LocalDateTime}类对象的形式，返回计算后的日期/时间
     * 
     * @return {@link LocalDateTime}类对象
     * @since autest 2.0.0
     */
    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.of(calculateTime.toLocalDate(), calculateTime.toLocalTime());
    }

    /**
     * 用于以{@link Time}的形式返回初始化时设置的时间
     * 
     * @return 始化时设置的时间
     * @since autest 2.0.0
     */
    public Time getInitTime() {
        return Time.parse(initTime);
    }

    /**
     * 用于以{@link Time}的形式将设置后的时间作为初始时间进行返回
     * 
     * @return 计算后的时间
     * @since autest 2.0.0
     */
    public Time getCalculateTime() {
        return Time.parse(calculateTime);
    }

    /**
     * 用于还原初始化时设置的日期/时间
     * 
     * @since autest 2.0.0
     */
    public Time initTime() {
        calculateTime = initTime;
        return this;
    }

    /**
     * 用于根据条件计算日期/时间，方法允许传入小数与负数进行计算
     * <p>
     * <b>注意：</b>在计算年、月时，若传入的数值是小数，在转换毫秒值时，其会按照
     * <ul>
     * <li>1年 = 365天</li>
     * <li>1月 = 30天</li>
     * </ul>
     * 进行计算，在跨度大的计算中，其会存在精度的丢失
     * </p>
     * 
     * @param num      日期/时间增减的数量
     * @param timeUnit 日期计算的单位
     * @since autest 2.0.0
     */
    public Time addTime(double num, TimeUnit timeUnit) {
        calculateTime = calcuLocalTime(Double.valueOf(num), timeUnit, calculateTime);
        return this;
    }

    /**
     * 用于根据传入的增减时间的规则对时间进行增减。
     * <p>
     * 根据单位前的数值对指定的单位进行增减。例如：需要对当前设置的时间增加1年3个月又5天并较少2小时30分钟45秒，
     * 此时可以传入“1年3月5日-2时-30分-45秒”，亦可以传入“1y3m5d-2h-30min-45s”。
     * <ul>
     * 可传入的单位有：
     * <li>年单位：年、y、Y</li>
     * <li>月单位：月、m、M</li>
     * <li>周单位：周、w、W</li>
     * <li>日单位：日、d、D</li>
     * <li>小时单位：时、h、H</li>
     * <li>分钟单位：分、min、MIN</li>
     * <li>秒单位：秒、s、S</li>
     * </ul>
     * 具体的计算规则与{@link #addTime(double, TimeUnit)}方法一致
     * </p>
     * 
     * @param calculateTimeText 增减时间的规则
     * @return 返回修改后的时间戳
     * @since autest 2.0.0
     */
    public Time addTime(String calculateTimeText) {
        // 将字符串转换为char[]数组
        char[] chars = Optional.ofNullable(calculateTimeText).filter(text -> !text.isEmpty())
                // 由于需要在获取到当前字段后才能计算已保存的内容，为保证最后一位能进行计算，在字符串末尾拼接一个“-”符号
                .map(text -> text + "-").map(String::toCharArray)
                .orElseThrow(() -> new IncorrectConditionException("必须指定修改时间的参数"));

        // 记录当前计算的时间
        LocalDateTime nowTime = calculateTime;

        /*
         * 判断单位思路： 1.遍历通过calculateTimeText得到的每一个字符 2.判断当前字符是否为数字：
         * a.若为数字，则判断上一次读取的内容是否为字符： I.若为字符，则表示上一个单位及计算数值已读取完毕，则先对上一次的数值对日期时间进行一次计算
         * II.若为数字，则表示当前正在读取计算的数值，则不进行操作 判断结束后，记录isUnit为false，表示当前字符为数字，并拼接到numText中
         * b.若为非数字，则将isUnit设置为true，并拼接计算单位
         */
        // 遍历所有的字符，区别存储单位与增减的数值
        StringBuilder numText = new StringBuilder();
        StringBuilder unitText = new StringBuilder();
        boolean isUnit = false;
        for (char ch : chars) {
            // 判断当前字符是否为数字
            if (Character.isDigit(ch) || ch == '.' || ch == '-') {
                // 判断上一次读取的内容是否为字符
                if (isUnit) {
                    nowTime = calcuLocalTime(disposeDoubleText(numText.toString()),
                            Arrays.stream(TimeUnit.values()).filter(unit -> unit.isTimeUnit(unitText.toString()))
                                    .findFirst().orElseThrow(
                                            () -> new IncorrectConditionException("无法识别的计算公式：" + numText + unitText)),
                            nowTime);

                    numText.delete(0, numText.length());
                    unitText.delete(0, unitText.length());
                }

                numText.append(ch);
                isUnit = false;
            } else {
                isUnit = true;
                unitText.append(ch);
            }
        }

        calculateTime = nowTime;
        return this;
    }

    /**
     * 用于对计算的double数值进行处理，补全小数点前后缺失的内容
     * 
     * @param doubleText 数值文本
     * @return 转换后的double类型
     * @since autest 2.0.0
     */
    private Double disposeDoubleText(String doubleText) {
        int index = doubleText.indexOf(".");
        if (index == doubleText.length() - 1) {
            return Double.valueOf(doubleText + "0");
        } else if (index == 0) {
            return Double.valueOf("0" + doubleText);
        } else {
            return Double.valueOf(doubleText);
        }
    }

    /**
     * 用于对传入的时间进行计算，并返回计算结果
     * 
     * @param num      计算数值
     * @param timeUnit 计算单位
     * @param time     指定的日期
     * @return 计算后得到的日期
     * @since autest 2.0.0
     */
    private LocalDateTime calcuLocalTime(Double num, TimeUnit timeUnit, LocalDateTime time) {
        // 为避免出现数字过大导致计算出错的问题，先计算整数部分，再将小数部分转换为时间戳后，计算毫秒值
        time = time.plus(num.intValue(), timeUnit.getChronoUnit());
        num = num - num.intValue();
        time = time.plus((long) (num * timeUnit.getToMillisNum()), ChronoUnit.MILLIS);

        return time;
    }

    /**
     * 用于识别传入的日期文本，并将日期文本转换为相应的日期格式化字符串
     * <p>
     * <b>注意：</b>
     * <ol>
     * <li>日期字符串必须是三位完整的日期（X年X月X日）或时间（X时X分X秒），或者是完整的日期+时间（X年X月X日X时X分X秒）</li>
     * <li>日期字符串前后允许添加非数字字符</li>
     * <li>无法识别纯数字的日期格式</li>
     * </ol>
     * </p>
     * 
     * @param dateText 日期文本
     * @return 相应的日期格式化字符串
     * @since autest 2.0.0
     */
    private static String judgeDateFormatText(String dateText) {
        // 判断格式化日期时间中是否存在字母
        if (dateText.matches(".*[a-zA-Z]+.*")) {
            throw new IncorrectConditionException("格式化的日期/时间中存在字母：" + dateText);
        }

        // 将传入的日期文本转换为字符数组
        char[] chars = dateText.toCharArray();

        /*
         * 转换思路： 1.遍历通过dateText得到的每一个字符 2.判断当前字符是否为数字：
         * a.若为数字，则记录isSign为false，表示当前字符为数字，并拼接index指向的位数
         * b.若为非数字，则记录isSign为true,表示当前字符为字符，则需要再次判断上一个字符是 否也是非数字（即isSign是否本身为false）:
         * I.若上一个字符不为非数字（isSign原为true），则设置index指向的位数加1（即第一次读取到分隔符， 表示上一位的日期以存储完毕）
         * II.若上一位为非数字（isSign原为false），则不做改动（即该字符仅为分隔符的一部分） 判断结束后，将isSign设置为true，并拼接分隔符
         * 3.结束循环后，得到一个待转译的中间字符串
         * 
         * 举例：传入“2020-12-25 14:12:12”最终会转换为“1111-22-33 44:55:66”
         */
        int index = 1;
        boolean isSign = false;
        StringBuilder formatTextBuilder = new StringBuilder();
        for (char ch : chars) {
            if (Character.isDigit(ch)) {
                isSign = false;
                formatTextBuilder.append(index);
            } else {
                if (!isSign) {
                    index++;
                }

                isSign = true;
                formatTextBuilder.append(ch);
            }
        }

        // 判断中间字符串最后一位是否为非数字字符，若为非数字字符，表示位数多移动了1位，需要减1后得到真实的位数
        index -= (formatTextBuilder.substring(formatTextBuilder.length() - 1).matches("\\d") ? 0 : 1);

        // 判断位数，若位数为3，则表示只传入了日期或者时间
        if (index == 3) {
            // 若第一位包含4个字符，则按日期转换，否则按时间转换
            if (formatTextBuilder.substring(formatTextBuilder.indexOf("1"), formatTextBuilder.lastIndexOf("1") + 1)
                    .length() == 4) {
                return formatTextBuilder.toString().replaceAll("1", "y").replaceAll("2", "M").replaceAll("3", "d");
            } else {
                return formatTextBuilder.toString().replaceAll("1", "H").replaceAll("2", "m").replaceAll("3", "s");
            }
        } else if (index == 6) {
            // 若位数为6，表示既传入了日期也传入了时间
            return formatTextBuilder.toString().replaceAll("1", "y").replaceAll("2", "M").replaceAll("3", "d")
                    .replaceAll("4", "H").replaceAll("5", "m").replaceAll("6", "s");
        } else {
            throw new IncorrectConditionException("时间“" + dateText + "”不符合格式的规则");
        }
    }

    @Override
    public int compareTo(Time compateTime) {
        return Optional.ofNullable(compateTime).map(Time::getLocalDateTime).map(calculateTime::compareTo)
                .orElseThrow(() -> new IncorrectConditionException("需要比较的时间存在异常"));
    }

    @Override
    public String toString() {
        return getFormatTime();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((calculateTime == null) ? 0 : calculateTime.hashCode());
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
        Time other = (Time) obj;
        if (calculateTime == null) {
            if (other.calculateTime != null) {
                return false;
            }
        } else if (!calculateTime.equals(other.calculateTime)) {
            return false;
        }
        return true;
    }

    /**
     * 用于根据最小比较单位，对时间进行比较
     * <p>
     * 通过指定最小比较单位，从而返回该单位及以上单位的对比结果，例如：
     * <ul>
     * 假定有如下两个日期： <code><pre>
     * Time t1 = Time.parse("2020-12-10 17:22:12");
     * Time t2 = Time.parse("2020-12-10 20:27:12");
     * </pre></code>
     * <li>当调用{@code t1.equalsForUnit(t2, TimeUnit.SECOND)}时，则返回结果为false</li>
     * <li>当调用{@code t1.equalsForUnit(t2, TimeUnit.DAY)}时，则返回结果为true</li>
     * </ul>
     * </p>
     * <p>
     * <b>注意：</b>除{@link TimeUnit#YEAR}、{@link TimeUnit#MONTH}、{@link TimeUnit#DAY}、
     * {@link TimeUnit#HOUR}、{@link TimeUnit#MINUTE}、{@link TimeUnit#SECOND}单位外，其他的
     * 单位传入进行判断时，会抛出异常
     * </p>
     * 
     * @param compareTime 需要比对的时间
     * @param timeUnit    最小判断单位
     * @return 对比结果
     * @throws IncorrectConditionException 单位传入有误时抛出的异常
     * @since autest 2.0.0
     */
    public boolean equalsForUnit(Time compareTime, TimeUnit timeUnit) {
        if (compareTime == null || timeUnit == null) {
            return false;
        }

        boolean result = equals(compareTime);
        // 若两时间一致，则直接返回true
        if (result) {
            return result;
        }

        switch (timeUnit) {
        case SECOND:
            result = (compareTime.getLocalDateTime().getSecond() == calculateTime.getSecond());
            if (!result) {
                return result;
            }
        case MINUTE:
            result = (compareTime.getLocalDateTime().getMinute() == calculateTime.getMinute());
            if (!result) {
                return result;
            }
        case HOUR:
            result = (compareTime.getLocalDateTime().getHour() == calculateTime.getHour());
            if (!result) {
                return result;
            }
        case DAY:
            result = (compareTime.getLocalDateTime().getDayOfMonth() == calculateTime.getDayOfMonth());
            if (!result) {
                return result;
            }
        case MONTH:
            result = (compareTime.getLocalDateTime().getMonth() == calculateTime.getMonth());
            if (!result) {
                return result;
            }
        case YEAR:
            result = (compareTime.getLocalDateTime().getYear() == calculateTime.getYear());
            return result;
        default:
            throw new IncorrectConditionException("无法比较的单位：" + timeUnit);
        }
    }
}
