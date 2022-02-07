package com.auxiliary.tool.data;

/**
 * <p>
 * <b>文件名：</b>KeyType.java
 * </p>
 * <p>
 * <b>用途：</b>
 * 用于统一键盘按键指向的键值，以便于统一工具的调用
 * </p>
 * <p>
 * <b>编码时间：</b>2022年1月29日 下午5:04:29
 * </p>
 * <p>
 * <b>修改时间：</b>2022年1月29日 下午5:04:29
 * </p>
 *
 * @author 彭宇琦
 * @version Ver1.0
 * @since autest 3.1.0
 */
public enum KeyType {
    /**
     * 退格键(BackSpace)
     */
    BACK_SPACE("BackSpace", '\uE003', '\b'),
    /**
     * tab键
     */
    TAB("Tab", '\uE004', '\t'),
    /**
     * 回车键(Enter)
     */
    ENTER("Enter", '\uE007', '\r'),
    /**
     * shift键
     */
    SHIFT("Shift", '\uE008', '\ue020'),
    /**
     * ctrl键
     */
    CTRL("Ctrl", '\uE009', '\ue021'),
    /**
     * alt键
     */
    ALT("Alt", '\uE00A', '\ue022'),
    /**
     * pause键
     */
    PAUSE("Pause", '\uE00B', '\ue026'),
    /**
     * esc键
     */
    ESC("ESC", '\uE00C', '\ue01b'),
    /**
     * 空格键(Space)
     */
    SPACE("Space", '\uE00D', ' '),
    /**
     * 页面向上按键(Page up)
     */
    PAGE_UP("Page up", '\uE00E', '\ue004'),
    /**
     * 页面向下按键(Page down)
     */
    PAGE_DOWN("Page down", '\uE00F', '\ue005'),
    /**
     * 结束键(End)
     */
    END("End", '\uE010', '\ue007'),
    /**
     * 主页键(Home)
     */
    HOME("Home", '\uE011', '\ue008'),
    /**
     * 方向左键(Left)
     */
    LEFT("Left", '\uE012', '\ue003'),
    /**
     * 方向上键(Up)
     */
    UP("Up", '\uE013', '\ue000'),
    /**
     * 方向右键(Right)
     */
    RIGHT("Right", '\uE014', '\ue001'),
    /**
     * 方向下键(Down)
     */
    DOWN("Down", '\uE015', '\ue002'),
    /**
     * 插入键(Insert/Ins)
     */
    INS("Insert", '\uE016', '\ue009'),
    /**
     * 删除键(Delect/Del)
     */
    DEL("Delect", '\uE017', '\ue006'),
    /**
     * 分号键
     */
    SEMICOLON(";", '\uE018', ';'),
    /**
     * 等号键
     */
    EQUALS("=", '\uE019', '='),
    /**
     * 数字小键盘0键
     */
    NUM0("0", '\uE01A', '\ue030'),
    /**
     * 数字小键盘1键
     */
    NUM1("1", '\uE01B', '\ue031'),
    /**
     * 数字小键盘2键
     */
    NUM2("2", '\uE01C', '\ue032'),
    /**
     * 数字小键盘3键
     */
    NUM3("3", '\uE01D', '\ue033'),
    /**
     * 数字小键盘4键
     */
    NUM4("4", '\uE01E', '\ue034'),
    /**
     * 数字小键盘5键
     */
    NUM5("5", '\uE01F', '\ue035'),
    /**
     * 数字小键盘6键
     */
    NUM6("6", '\uE020', '\ue036'),
    /**
     * 数字小键盘7键
     */
    NUM7("7", '\uE021', '\ue037'),
    /**
     * 数字小键盘8键
     */
    NUM8("8", '\uE022', '\ue038'),
    /**
     * 数字小键盘9键
     */
    NUM9("9", '\uE023', '\ue039'),
    /**
     * 数字小键盘乘号键(*)
     */
    MULTIPLY("*", '\uE024', '\ue03E'),
    /**
     * 数字小键盘加号键(+)
     */
    ADD("+", '\uE025', '\ue03C'),
    /**
     * 数字小键盘回车键(Separator)
     */
    SEPARATOR("Separator", '\uE026', '\ue03A'),
    /**
     * 数字小键盘减键(-)
     */
    SUBTRACT("-", '\uE027', '\ue03D'),
    /**
     * 数字小键盘小数点键(.)
     */
    DECIMAL(".", '\uE028', '\ue040'),
    /**
     * 数字小键盘除号键(/)
     */
    DIVIDE("/", '\uE029', '\ue03F'),
    /**
     * F1键
     */
    F1("F1", '\uE031', '\ue011'),
    /**
     * F2键
     */
    F2("F2", '\uE032', '\ue012'),
    /**
     * F3键
     */
    F3("F3", '\uE033', '\ue013'),
    /**
     * F4键
     */
    F4("F4", '\uE034', '\ue014'),
    /**
     * F5键
     */
    F5("F5", '\uE035', '\ue015'),
    /**
     * F6键
     */
    F6("F6", '\uE036', '\ue016'),
    /**
     * F7键
     */
    F7("F7", '\uE037', '\ue017'),
    /**
     * F8键
     */
    F8("F8", '\uE038', '\ue018'),
    /**
     * F9键
     */
    F9("F9", '\uE039', '\ue019'),
    /**
     * F10键
     */
    F10("F10", '\uE03A', '\ue01a'),
    /**
     * F11键
     */
    F11("F11", '\uE03B', '\ue01b'),
    /**
     * F12键
     */
    F12("F12", '\uE03C', '\ue01c'),
    /**
     * 命令按键，在Windows系统键盘上对应Win键；在Sun系统键盘上对应实心宝石键(◆)；在Mac系统键盘上对应command按键（但在win系统上，selenium工具使用无效）
     */
    META("Meta", '\uE03D', '\ue023'),
    /**
     * Mac系统键盘上对应command按键
     */
    COMMAND("Cmd", '\uE03D', '\ue023'),
    /**
     * Windows系统键盘上对应Win键，在selenium工具中不生效
     */
    WIN("Win", '\uE03D', '\ue042'),
    /**
     * A键
     */
    A("A", 'A', 'A'),
    /**
     * B键
     */
    B("B", 'B', 'B'),
    /**
     * C键
     */
    C("C", 'C', 'C'),
    /**
     * D键
     */
    D("D", 'D', 'D'),
    /**
     * E键
     */
    E("E", 'E', 'E'),
    /**
     * F键
     */
    F("F", 'F', 'F'),
    /**
     * G键
     */
    G("G", 'G', 'G'),
    /**
     * H键
     */
    H("H", 'H', 'H'),
    /**
     * I键
     */
    I("I", 'I', 'I'),
    /**
     * J键
     */
    J("J", 'J', 'J'),
    /**
     * K键
     */
    K("K", 'K', 'K'),
    /**
     * L键
     */
    L("L", 'L', 'L'),
    /**
     * M键
     */
    M("M", 'M', 'M'),
    /**
     * N键
     */
    N("N", 'N', 'N'),
    /**
     * O键
     */
    O("O", 'O', 'O'),
    /**
     * P键
     */
    P("P", 'P', 'P'),
    /**
     * Q键
     */
    Q("Q", 'Q', 'Q'),
    /**
     * R键
     */
    R("R", 'R', 'R'),
    /**
     * S键
     */
    S("S", 'S', 'S'),
    /**
     * T键
     */
    T("T", 'T', 'T'),
    /**
     * U键
     */
    U("U", 'U', 'U'),
    /**
     * V键
     */
    V("V", 'V', 'V'),
    /**
     * W键
     */
    W("W", 'W', 'W'),
    /**
     * X键
     */
    X("X", 'X', 'X'),
    /**
     * Y键
     */
    Y("Y", 'Y', 'Y'),
    /**
     * Z键
     */
    Z("Z", 'Z', 'Z')
    ;

    /**
     * 在selenium工具中按键指向的key值
     */
    private char seleniumKey;
    /**
     * 在sikuli工具中按键指向的key值
     */
    private char sikuliKey;

    /**
     * 存储按键名称
     */
    private String name = "";

    /**
     * 初始化枚举值
     * @param seleniumKey selenium工具中按键指向的key值
     * @param sikuliKey sikuli工具中按键指向的key值
     * @param name 按键对应的名称
     * @since autest 3.1.0
     */
    private KeyType(String  name, char seleniumKey, char sikuliKey) {
        this.seleniumKey = seleniumKey;
        this.sikuliKey = sikuliKey;
        this.name = name;
    }

    /**
     * 该方法用于返回在selenium工具中按键指向的key值
     *
     * @return selenium工具中按键指向的key值
     * @since autest 3.1.0
     */
    public char getSeleniumKey() {
        return seleniumKey;
    }

    /**
     * 该方法用于返回在sikuli工具中按键指向的key值
     *
     * @return sikuli工具中按键指向的key值
     * @since autest 3.1.0
     */
    public char getSikuliKey() {
        return sikuliKey;
    }

    /**
     * 该方法用于返回按键的名称
     *
     * @return 按键名称
     * @since autest 3.1.0
     */
    public String getName() {
        return name;
    }
}
