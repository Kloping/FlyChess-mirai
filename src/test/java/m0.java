import io.github.Kloping.*;
import io.github.kloping.file.FileUtils;
import net.mamoe.mirai.contact.Contact;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author github.kloping
 */
public class m0 {
    public static void main(String[] args) throws Exception {
        URL url = FlyChess.getUrlsFrom("img/background.png", Chess.class);
        Road road = Road.YELLOW;
        BufferedImage back = ImageIO.read(url);
        int r = 0;
        for (Position position : road.list) {
            int x = position.intLeft() + 5;
            int y = position.intTop() + 5;
            back = ImageDrawerUtils.writeFont(back, x, y, r++ + ":" + position.getId());
        }
        File file = new File("./temp/" + UUID.randomUUID() + ".jpg");
        file.getParentFile().mkdirs();
        file.createNewFile();
        ImageIO.write((RenderedImage) back, "jpg", file);
    }
}
