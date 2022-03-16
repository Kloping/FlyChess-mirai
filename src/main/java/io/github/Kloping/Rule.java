package io.github.Kloping;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author github.kloping
 */
public class Rule {
    public static Contact context;
    public static Chess chess;
    public static boolean isStarted = false;
    public static long time = 0;

    public static String create() {
        if (chess != null) {
            return "棋盘已创建";
        } else {
            chess = new Chess();
            time = System.currentTimeMillis();
            return "创建飞行棋盘完成\n发送: 加入飞行棋 #即可加入游戏";
        }
    }

    private static long player1;
    private static long player2;
    private static long player3;
    private static long player4;

    public static Object join(long q) throws IOException {
        if (isStarted) return "游戏已开始";
        if (joined(q)) return "您已经加入了";
        if (player1 <= 0) {
            player1 = q;
            chess.addSide(Side.initBlue().setQ(q));
            return drawThis();
        }
        if (player2 <= 0) {
            player2 = q;
            chess.addSide(Side.initYellow().setQ(q));
            return drawThis();
        }
        if (player3 <= 0) {
            player3 = q;
            chess.addSide(Side.initGreen().setQ(q));
            return drawThis();
        }
        if (player4 <= 0) {
            player4 = q;
            chess.addSide(Side.initRed().setQ(q));
            start();
            return drawThis();
        }
        return null;
    }

    private static boolean joined(long q) {
        return player1 == q || player2 == 1 || player3 == q || player4 == q;
    }

    private static MessageChain drawThis() throws IOException {
        java.awt.Image image = chess.getFrame();
        image = putImage(image, player1, blue, player2, yellow);
        image = putImage(image, player3, green, player4, red);
        if (step > 0) {
            System.out.println(step);
            BufferedImage i0 = (BufferedImage) STEP2IMAGE.get(step);
            image = ImageDrawerUtils.putImage((BufferedImage) image, i0, 455, 460);
        }
        File file = new File("./temp/" + UUID.randomUUID() + ".jpg");
        file.getParentFile().mkdirs();
        file.createNewFile();
        ImageIO.write((RenderedImage) image, "jpg", file);
        Image im = Contact.uploadImage(context, file);
        return im.plus("加入成功");
    }

    private static java.awt.Image putImage(java.awt.Image image, long pl1, Position p1, long pl2, Position p2) throws IOException {
        if (pl1 > 0)
            image = ImageDrawerUtils.putImage((BufferedImage) image, ImageIO.read(new URL("http://q1.qlogo.cn/g?b=qq&nk=" + pl1 + "&s=40")), p1.intLeft(), p1.intTop());
        if (pl2 > 0)
            image = ImageDrawerUtils.putImage((BufferedImage) image, ImageIO.read(new URL("http://q1.qlogo.cn/g?b=qq&nk=" + pl2 + "&s=40")), p2.intLeft(), p2.intTop());
        return image;
    }

    public static Object start() throws IOException {
        if (chess.sides.size() >= 2) {
            return "人数不足两人";
        }
        try {
            isStarted = true;
            chess.next();
            return drawThis();
        } finally {
            tipsShake();
        }
    }

    private static void tipsShake() {
        context.sendMessage(new At(chess.side.getQ()).plus("请投掷骰子#投掷子/扔色子"));
    }

    private static void tipsSelect() {
        context.sendMessage(new At(chess.side.getQ()).plus("请选择棋子 /1 /2 /3 /4"));
    }

    private static int step = 0;
    public static Random random = new Random();

    public static Object shake(long q) throws IOException {
        if (q == chess.side.getQ()) {
            step = random.nextInt(6) + 1;
            if ((step % 2 != 0) && !hasStepable(step)) {
                chess.next();
                tipsShake();
                return drawThis();
            }
            tipsSelect();
            return drawThis();
        }
        return "等待中...";
    }

    private static boolean hasStepable(int step) {
        for (Pieces piece : chess.side.getPieces()) {
            if (piece.isReady()) return true;
        }
        return false;
    }

    public static Object select(long q, int i) throws IOException {
        if (q == chess.side.getQ()) {
            Pieces pieces = chess.side.getPieces()[i - 1];
            if (pieces.isReady()) {
                pieces.jumpStep(step);
                if (step != 6) {
                    chess.next();
                }
                tipsShake();
                return drawThis();
            } else {
                if (step % 2 == 0) {
                    pieces.ready();
                    if (step != 6) {
                        chess.next();
                    }
                    tipsShake();
                    return drawThis();
                } else {
                    return "2,4,6点可起飞";
                }
            }
        }
        return "等待中...";
    }

    public static final Map<Integer, java.awt.Image> STEP2IMAGE = new HashMap<>();

    static {
        try {
            BufferedImage i0 = ImageIO.read(FlyChess.getUrlsFrom("img/1.jpg", Position.class));
            i0 = (BufferedImage) ImageDrawerUtils.image2Size(i0, 70, 70);
            STEP2IMAGE.put(1, (java.awt.Image) i0);
            BufferedImage i1 = ImageIO.read(FlyChess.getUrlsFrom("img/2.jpg", Position.class));
            i1 = (BufferedImage) ImageDrawerUtils.image2Size(i1, 70, 70);
            STEP2IMAGE.put(2, (java.awt.Image) i1);
            BufferedImage i2 = ImageIO.read(FlyChess.getUrlsFrom("img/3.jpg", Position.class));
            i2 = (BufferedImage) ImageDrawerUtils.image2Size(i2, 70, 70);
            STEP2IMAGE.put(3, (java.awt.Image) i2);
            BufferedImage i3 = ImageIO.read(FlyChess.getUrlsFrom("img/4.jpg", Position.class));
            i3 = (BufferedImage) ImageDrawerUtils.image2Size(i3, 70, 70);
            STEP2IMAGE.put(4, (java.awt.Image) i3);
            BufferedImage i4 = ImageIO.read(FlyChess.getUrlsFrom("img/5.jpg", Position.class));
            i4 = (BufferedImage) ImageDrawerUtils.image2Size(i4, 70, 70);
            STEP2IMAGE.put(5, (java.awt.Image) i4);
            BufferedImage i5 = ImageIO.read(FlyChess.getUrlsFrom("img/6.jpg", Position.class));
            i5 = (BufferedImage) ImageDrawerUtils.image2Size(i5, 70, 70);
            STEP2IMAGE.put(6, (java.awt.Image) i5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Position green;
    private static Position red;
    private static Position yellow;
    private static Position blue;

    static {
        green = new Position();
        green.setLeft("125");
        green.setTop("122");

        red = new Position();
        red.setLeft("823");
        red.setTop("125");

        yellow = new Position();
        yellow.setLeft("125");
        yellow.setTop("822");

        blue = new Position();
        blue.setLeft("822");
        blue.setTop("824");
    }

    public static void tipsJump() {
        context.sendMessage("跨越了捷径");
    }

    public static void tipsFly() {
        context.sendMessage("棋子起飞");
    }

    public static void win(Pieces pieces, String color) {
        context.sendMessage("一颗棋子达到重点");
        if (playWin(color)) {
            context.sendMessage("该玩家获得了胜利");
            if (winAll()) {
                destroy();
                tipsList();
            }
        }
    }

    private static String getLoseTime() {
        long now = System.currentTimeMillis();
        long st = now - time;
        return getFormat(st);
    }

    public static String getFormat(long st) {
        long seconds = 0;
        while (st >= 1000) {
            seconds++;
            st -= 1000;
        }
        long minutes = 0;
        while (seconds >= 60) {
            seconds -= 60;
            minutes++;
        }
        long hour = 0;
        while (minutes >= 60) {
            minutes -= 60;
            hour++;
        }

        long day = 0;
        while (hour >= 24) {
            hour -= 24;
            day++;
        }
        StringBuilder sb = new StringBuilder();
        if (day > 0) {
            sb.append(day).append("天");
        }
        if (hour > 0) {
            sb.append(hour).append("小时");
        }
        if (minutes > 0) {
            sb.append(day).append("分钟");
        }
        if (seconds > 0) {
            sb.append(day).append("秒");
        }
        return sb.toString();
    }

    public static void destroy() {
        chess = null;
        isStarted = false;
        context.sendMessage("游戏结束\n耗时:" + getLoseTime());
    }

    private static List<Long> pl = new LinkedList<>();

    private static void tipsList() {
        MessageChainBuilder builder = new MessageChainBuilder();
        int i = 1;
        for (Long aLong : pl) {
            builder.append("第 " + i).append(new At(aLong.longValue())).append("\n");
        }
        context.sendMessage(builder.build());
        pl.clear();
    }

    private static boolean winAll() {
        for (Side side : chess.sides) {
            if (!side.isWin())
                return false;
        }
        return true;
    }

    private static boolean playWin(String color) {
        for (Side side : chess.sides) {
            if (side.getColor().equalsIgnoreCase(color)) {
                for (Pieces piece : side.getPieces()) {
                    if (!piece.isWin()) {
                        return false;
                    }
                }
                pl.add(side.getQ());
                return true;
            }
        }
        return false;
    }
}
