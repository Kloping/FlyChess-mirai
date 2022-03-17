package io.github.Kloping;

import java.util.LinkedList;
import java.util.List;

/**
 * @author github.kloping
 */
public class Road {

    public static Road GREEN;
    public static Road RED;
    public static Road BLUE;
    public static Road YELLOW;

    static {
        RED = new Road();
        Position p1 = new Position();
        p1.setTop("52");
        p1.setLeft("658");
        p1.setColor("red");
        p1.setState("ready");
        RED.list.add(p1);
        for (int i = 1; i <= 50; i++) {
            RED.list.add(Position.ID2POSITION.get(i));
        }
        for (int i = 61; i <= 66; i++) {
            RED.list.add(Position.ID2POSITION.get(i));
        }
        //==
        GREEN = new Road();
        Position p2 = new Position();
        p2.setTop("250");
        p2.setLeft("50");
        p2.setColor("green");
        p2.setState("ready");
        GREEN.list.add(p2);
        for (int i = 40; i <= 52; i++) {
            GREEN.list.add(Position.ID2POSITION.get(i));
        }
        for (int i = 1; i <= 37; i++) {
            GREEN.list.add(Position.ID2POSITION.get(i));
        }
        for (int i = 91; i <= 96; i++) {
            RED.list.add(Position.ID2POSITION.get(i));
        }
        //==
        YELLOW = new Road();
        Position p3 = new Position();
        p3.setTop("898");
        p3.setLeft("260");
        p3.setColor("yellow");
        p3.setState("ready");
        YELLOW.list.add(p3);
        for (int i = 27; i <= 52; i++) {
            YELLOW.list.add(Position.ID2POSITION.get(i));
        }
        for (int i = 1; i <= 24; i++) {
            YELLOW.list.add(Position.ID2POSITION.get(i));
        }
        for (int i = 81; i <= 86; i++) {
            YELLOW.list.add(Position.ID2POSITION.get(i));
        }
        //==
        BLUE = new Road();
        Position p4 = new Position();
        p4.setTop("678");
        p4.setLeft("900");
        p4.setColor("blue");
        p4.setState("ready");
        BLUE.list.add(p4);
        for (int i = 14; i <= 52; i++) {
            BLUE.list.add(Position.ID2POSITION.get(i));
        }
        for (int i = 1; i <= 11; i++) {
            BLUE.list.add(Position.ID2POSITION.get(i));
        }
        for (int i = 71; i <= 76; i++) {
            BLUE.list.add(Position.ID2POSITION.get(i));
        }
    }

    private List<Position> list = new LinkedList<>();
    private int index = 0;

    public Position next(int r) {
        index += r;
        int max = list.size() - 1;
        if (index >= max) {
            int m = index - max;
            index = max;
            index -= m;
        }
        Position position = list.get(index);
        if (position == null) {
            position = list.get(list.size() - 1);
        }
        return position;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Position start() {
        index = 0;
        return list.get(index);
    }

    public Road copy() {
        Road r = new Road();
        for (Position position : this.list) {
            r.list.add(position);
        }
        r.index = this.index;
        return r;
    }
}
