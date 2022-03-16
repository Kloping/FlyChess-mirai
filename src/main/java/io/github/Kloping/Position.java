package io.github.Kloping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author github.kloping
 */
public class Position {
    public static List<Position> POSITIONS;
    public static Map<Integer, Position> ID2POSITION = new HashMap<>();
    public static Map<Position, Integer> POSITION2ID = new HashMap<>();
    public static JSONArray array;

    static {
        try {
            array = JSON.parseArray(FlyChess.getStringFrom("position.json", Position.class));
            POSITIONS = array.toJavaList(Position.class);
            for (Position position : POSITIONS) {
                ID2POSITION.put(position.getId(), position);
                POSITION2ID.put(position, position.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Integer id;
    private String top;
    private String left;
    private String color;
    /**
     * 飞行到
     */
    @JSONField(name = "super")
    private String s;
    /**
     * 转向终点
     */
    private String r;
    /**
     * 终点
     */
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getLeft() {
        return left;
    }

    public Integer intLeft() {
        return Float.valueOf(left).intValue();
    }

    public Integer intTop() {
        return Float.valueOf(top).intValue();
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    @Override
    public String toString() {
        return "Position{" +
                "id=" + id +
                ", top='" + top + '\'' +
                ", left='" + left + '\'' +
                ", color='" + color + '\'' +
                ", s='" + s + '\'' +
                ", r='" + r + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Objects.equals(id, position.id) && Objects.equals(top, position.top) && Objects.equals(left, position.left) && Objects.equals(color, position.color) && Objects.equals(s, position.s) && Objects.equals(r, position.r) && Objects.equals(state, position.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, top, left, color, s, r, state);
    }
}
