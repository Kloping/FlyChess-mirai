package io.github.Kloping;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import net.mamoe.mirai.contact.Contact;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.github.Kloping.Rule.*;

/**
 * @author github-kloping
 */
public class ImageDrawer {

    public static net.mamoe.mirai.message.data.Image drawThis() throws IOException {
        java.awt.Image image = chess.getFrame();
        image = putImage(image, player1, blue, player2, yellow);
        image = putImage(image, player3, green, player4, red);
        if (step > 0) {
            BufferedImage i0 = (BufferedImage) STEP2IMAGE.get(step);
            image = ImageDrawerUtils.putImage((BufferedImage) image, i0, 455, 460);
        }
        File file = new File("./temp/" + UUID.randomUUID() + ".jpg");
        file.getParentFile().mkdirs();
        file.createNewFile();
        ImageIO.write((RenderedImage) image, "jpg", file);
        net.mamoe.mirai.message.data.Image im = Contact.uploadImage(context, file);
        return im;
    }

    private static java.awt.Image putImage(java.awt.Image image, long pl1, Position p1, long pl2, Position p2) throws IOException {
        if (pl1 > 0)
            image = ImageDrawerUtils.putImage((BufferedImage) image, ImageIO.read(new URL("http://q1.qlogo.cn/g?b=qq&nk=" + pl1 + "&s=40")), p1.intLeft(), p1.intTop());
        if (pl2 > 0)
            image = ImageDrawerUtils.putImage((BufferedImage) image, ImageIO.read(new URL("http://q1.qlogo.cn/g?b=qq&nk=" + pl2 + "&s=40")), p2.intLeft(), p2.intTop());
        return image;
    }

    public static final Map<Integer, Image> STEP2IMAGE = new HashMap<>();

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
}
