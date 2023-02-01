package io.github.Kloping;

import io.github.kloping.common.Public;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static io.github.Kloping.ImageDrawer.drawThis;

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

    protected static long player1;
    protected static long player2;
    protected static long player3;
    protected static long player4;

    public static Object join(long q) throws IOException {
        if (isStarted) return "游戏已开始";
        if (joined(q)) return "您已经加入了";
        if (chess == null) return "游戏未创建";
        if (player1 <= 0) {
            player1 = q;
            chess.addSide(Side.initBlue().setQ(q));
            return drawThis().plus("加入成功");
        }
        if (player2 <= 0) {
            player2 = q;
            chess.addSide(Side.initYellow().setQ(q));
            return drawThis().plus("加入成功");
        }
        if (player3 <= 0) {
            player3 = q;
            chess.addSide(Side.initGreen().setQ(q));
            return drawThis().plus("加入成功");
        }
        if (player4 <= 0) {
            player4 = q;
            chess.addSide(Side.initRed().setQ(q));
            start();
            return drawThis().plus("加入成功");
        }
        return null;
    }

    private static boolean joined(long q) {
        return player1 == q || player2 == q || player3 == q || player4 == q;
    }

    public static Object start() throws IOException {
        if (chess.getSides().size() < 2) {
            return "人数不足两人";
        }
        if (isStarted) {
            return "游戏已经开始";
        }
        try {
            chess.next();
            return drawThis();
        } finally {
            state = 1;
            isStarted = true;
            tipsShake();
        }
    }

    private static void tipsShake() {
        context.sendMessage(new At(chess.getSide().getQ()).plus("请投掷骰子#投掷子/扔色子"));
        if (FlyChess.config.getAuto()) {
            try {
                shake(chess.getSide().getQ(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void tipsSelect() {
        context.sendMessage(new At(chess.getSide().getQ()).plus("请选择棋子 /1 /2 /3 /4"));
        tryAutoSelect();
    }

    private static void tryAutoSelect() {
        if (oneLeft()) {
            return;
        }
        int r = 0;
        int i = 0;
        for (Pieces piece : chess.getSide().getPieces()) {
            if (!piece.isWin() && piece.isReady()) {
                r++;
                i = r;
            } else if (step % 2 == 0 && !piece.isReady()) {
                r++;
                i = r;
            }
        }
        if (r == 1) {
            try {
                select(chess.getSide().getQ(), i, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean oneLeft() {
        int i = 0;
        for (Pieces piece : chess.getSide().getPieces()) {
            if (piece.isWin()) i++;
        }
        return i == 3;
    }

    public static int state = 0;
    protected static int step = 0;
    public static Random random = new Random();
    public static Future future = null;

    public static void shake(long q, boolean breakOld) throws IOException {
        if (state == 1) {
            if (q == chess.getSide().getQ()) {
                step = random.nextInt(6) + 1;
                context.sendMessage(new At(q).plus("投掷结果:" + step));
                if (chess.getSide().test(step)) {
                    chess.next();
                    tipsShake();
                    sendNow(breakOld);
                    return;
                }
                if ((step % 2 != 0)) {
                    if (!hasStepable(step)) {
                        chess.next();
                        tipsShake();
                        sendNow(breakOld);
                        return;
                    }
                }
                state = 2;
                tipsSelect();
                sendNow(breakOld);
                return;
            }
        }
        context.sendMessage(new At(q).plus("等待中...on shake"));
    }

    private static void sendNow(boolean b) {
        if (future != null) {
            if (!future.isCancelled() && !future.isDone()) {
                if (b) future.cancel(true);
                else try {
                    future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        future = Public.EXECUTOR_SERVICE.submit(() -> {
            try {
                Thread.sleep(200L);
                context.sendMessage(drawThis());
            } catch (InterruptedException e) {
                System.err.println("任务已取消");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static boolean hasStepable(int step) {
        for (Pieces piece : chess.getSide().getPieces()) {
            if (piece.isReady() && !piece.isWin()) return true;
        }
        return false;
    }

    public static void select(long q, int i, boolean b) throws IOException {
        if (state == 2) {
            if (q == chess.getSide().getQ()) {
                Pieces pieces = chess.getSide().getPieces()[i - 1];
                if (pieces.isWin()) {
                    context.sendMessage(new At(q).plus("棋子已到终点"));
                    return;
                } else if (pieces.isReady()) {
                    chess.getSide().step(step, i - 1);
                    if (step != 6) {
                        chess.next();
                    }
                    state = 1;
                    tipsShake();
                    sendNow(b);
                    return;
                } else {
                    if (step % 2 == 0) {
                        pieces.ready();
                        if (step != 6) {
                            chess.next();
                        }
                        state = 1;
                        tipsShake();
                        sendNow(b);
                        return;
                    } else {
                        context.sendMessage("2,4,6点可起飞");
                        return;
                    }
                }
            }
        }
        context.sendMessage(new At(q).plus("等待中...on select"));
    }


    public static void tipsJump() {
        context.sendMessage("跨越了捷径");
    }

    public static void tipsFly() {
        context.sendMessage("棋子起飞");
    }

    public static void win(Pieces pieces, String color) {
        context.sendMessage("一颗棋子达到终点");
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
            sb.append(minutes).append("分钟");
        }
        if (seconds > 0) {
            sb.append(seconds).append("秒");
        }
        return sb.toString();
    }

    public static void destroy() {
        chess = null;
        isStarted = false;
        player1 = -1;
        player2 = -1;
        player3 = -1;
        player4 = -1;
        context.sendMessage("游戏结束\n耗时:" + getLoseTime());
    }

    /**
     * 已经赢了
     */
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
        int r = 0;
        for (Side side : chess.getSides()) {
            if (side.isWin()) {
                r++;
            }
        }
        return r >= chess.getSides().size() - 1;
    }

    private static boolean playWin(String color) {
        for (Side side : chess.getSides()) {
            if (side.getColor().equalsIgnoreCase(color)) {
                int i = 0;
                for (Pieces piece : side.getPieces()) {
                    if (piece.isWin()) {
                        i++;
                    }
                }
                if (i == 4) {
                    side.setWin(true);
                    pl.add(side.getQ());
                    return true;
                } else return false;
            }
        }
        return false;
    }

    public static void rollback(Side side) {
        for (Pieces piece : side.getPieces()) {
            if (!piece.isWin()) piece.reset();
        }
        context.sendMessage(new At(side.getQ()).plus("掷到了3个6,所有棋子返回"));
    }

    public static void attack() {
        context.sendMessage("击退一个棋子");
    }
}
