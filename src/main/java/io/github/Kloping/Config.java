package io.github.Kloping;

/**
 * @author github.kloping
 */
public class Config {
    private String create = "创建飞行棋";
    private String[] shake = {"掷骰子", "掷色子", "扔色子", "扔骰子"};
    private String join = "加入飞行棋";
    private String start = "开始游戏";
    private String one = "/1";
    private String two = "/2";
    private String tree = "/3";
    private String four = "/4";
    private Boolean auto = true;

    public Boolean getAuto() {
        return auto;
    }

    public void setAuto(Boolean auto) {
        this.auto = auto;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public String[] getShake() {
        return shake;
    }

    public void setShake(String[] shake) {
        this.shake = shake;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getTree() {
        return tree;
    }

    public void setTree(String tree) {
        this.tree = tree;
    }

    public String getFour() {
        return four;
    }

    public void setFour(String four) {
        this.four = four;
    }
}
