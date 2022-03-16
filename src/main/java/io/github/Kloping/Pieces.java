package io.github.Kloping;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static io.github.Kloping.FlyChess.getUrlsFrom;
import static io.github.Kloping.Position.ID2POSITION;
import static io.github.Kloping.Position.POSITION2ID;

/**
 * @author github.kloping
 */
public class Pieces {
    private Position position;
    private Image icon;
    private String color;
    private Road road;
    private boolean isReady = false;
    private boolean win = false;

    public Pieces(Position position, Image icon, Road road) {
        this.position = position;
        this.icon = icon;
        this.road = road;
    }

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

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
    }

    public void jumpStep(int r) {
        if (win) return;
        if (!isReady) return;
        for (int i = 0; i < r; i++) {
            this.position = road.next();
        }
        testState();
    }

    public void ready() {
        isReady = true;
        this.position = road.start();
    }

    public boolean isReady() {
        return isReady;
    }

    private void testState() {
        if ("win".equalsIgnoreCase(this.position.getState())) {
            Rule.win(this, color);
            win = true;
        }
        if (this.position.getColor() == null) return;
        if (this.position.getColor().equalsIgnoreCase(this.color)) {
            if (this.position.getS() != null) {
                this.position = ID2POSITION.get(Integer.valueOf(this.position.getS()));
                Rule.tipsFly();
            } else {
                this.position = getNextColor(color);
                Rule.tipsJump();
            }
        }
    }

    private Position getNextColor(String color) {
        int i = POSITION2ID.get(this.position);
        while (true) {
            i++;
            Position p0 = ID2POSITION.get(i);
            if (color.equals(p0.getColor())) {
                return p0;
            }
        }
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public static enum SidePieces {
        RED("red", ImageDrawerUtils.image2Size0(read(getUrlsFrom("img/red.png", Position.class)), 40, 40)),
        BLUE("blue", ImageDrawerUtils.image2Size0(read(getUrlsFrom("img/blue.png", Position.class)), 40, 40)),
        GREEN("green", ImageDrawerUtils.image2Size0(read(getUrlsFrom("img/green.png", Position.class)), 40, 40)),
        YELLOW("yellow", ImageDrawerUtils.image2Size0(read(getUrlsFrom("img/yellow.png", Position.class)), 40, 40));

        private static BufferedImage read(URL urlsFrom) {
            try {
                return ImageIO.read(urlsFrom);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        String color;
        Image image;

        SidePieces(String color, Image image) {
            this.color = color;
            this.image = image;
        }

        public Image getImage() {
            return image;
        }
    }
}
