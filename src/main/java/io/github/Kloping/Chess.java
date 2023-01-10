package io.github.Kloping;

import net.mamoe.mirai.contact.Contact;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.*;

/**
 * @author github.kloping
 */
public class Chess {
    public static URL url;

    static {
        url = FlyChess.getUrlsFrom("img/background.png", Chess.class);
    }

    public Side side;
    public List<Side> sides = new LinkedList<>();
    private int index = -1;

    public void addSide(Side side) {
        sides.add(side);
    }

    public Side getSide() {
        return side;
    }

    public List<Side> getSides() {
        return sides;
    }

    public void next() {
        index++;
        if (index >= sides.size()) {
            index = 0;
        }
        side = sides.get(index);
        if (side.isWin()) {
            next();
        }
    }

    public Image getFrame() throws IOException {
        BufferedImage back = ImageIO.read(url);
        for (Side s : sides) {
            int index = 1;
            for (Pieces piece : s.getPieces()) {
                Position position = piece.getPosition();
                int x = position.intLeft() + 5;
                int y = position.intTop() + 5;
                back = ImageDrawerUtils.putImage(back, (BufferedImage) piece.getIcon(), x, y);
                back = ImageDrawerUtils.writeFont(back, x, y, index + "");
                index++;
            }
        }
        return back;
    }

    public net.mamoe.mirai.message.data.Image getImage(Contact contact) throws IOException {
        File file = new File("./temp/" + UUID.randomUUID() + ".jpg");
        file.getParentFile().mkdirs();
        file.createNewFile();
        ImageIO.write((RenderedImage) getFrame(), "jpg", file);
        return Contact.uploadImage(contact, file);
    }
}
