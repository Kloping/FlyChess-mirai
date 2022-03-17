package io.github.Kloping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.github.kloping.map.MapUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static io.github.Kloping.FlyChess.getStringFrom;

/**
 * @author github.kloping
 */
public class Side {
    private long q;
    private Pieces[] pieces;
    public int up0 = 0;
    public int up1 = 0;
    private String color;
    private boolean win = false;

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getQ() {
        return q;
    }

    public Side setQ(long q) {
        this.q = q;
        return this;
    }

    public void setPieces(Pieces[] pieces) {
        this.pieces = pieces;
    }

    public Pieces[] getPieces() {
        return pieces;
    }

    public boolean test(int step) {
        if (step == 6 && up0 == 6 && up1 == 6) {
            Rule.rollback(this);
            return true;
        }
        return false;
    }

    public void step(int step, int index) {
        pieces[index].jumpStep(step);
        if (pieces[index].getPosition().getId() != null)
            MapUtils.append(Rule.chess.positionId2PiecesMap, pieces[index].getPosition().getId(), pieces[index]);
        up1 = up0;
        up0 = step;
    }

    public int getUp0() {
        return up0;
    }

    public int getUp1() {
        return up1;
    }

    public static Side initRed() throws IOException {
        return initSide("readCoord.json", Pieces.SidePieces.RED, Road.RED);
    }

    public static Side initBlue() throws IOException {
        return initSide("blueCoord.json", Pieces.SidePieces.BLUE, Road.BLUE);
    }

    public static Side initGreen() throws IOException {
        return initSide("greenCoord.json", Pieces.SidePieces.GREEN, Road.GREEN);
    }

    public static Side initYellow() throws IOException {
        return initSide("yellowCoord.json", Pieces.SidePieces.YELLOW, Road.YELLOW);
    }

    public static Side initSide(String path, Pieces.SidePieces res, Road road) throws IOException {
        JSONArray array = JSON.parseArray(getStringFrom(path, Position.class));
        List<Position> positions = array.toJavaList(Position.class);
        Side side = new Side();
        BufferedImage bi = (BufferedImage) res.getImage();
        List<Pieces> list = new LinkedList<>();
        for (Position position : positions) {
            Pieces pieces = new Pieces(position, bi, road.copy());
            pieces.setColor(res.color);
            list.add(pieces);
        }
        Side side0 = new Side();
        side0.setColor(res.color);
        side0.setPieces(list.toArray(new Pieces[0]));
        return side0;
    }
}
