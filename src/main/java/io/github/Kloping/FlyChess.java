package io.github.Kloping;

import io.github.kloping.io.ReadUtils;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author github.kloping
 */
public class FlyChess extends JavaPlugin {
    public static FlyChess INSTANCE = new FlyChess();

    public FlyChess() {
        super(new JvmPluginDescriptionBuilder("com.github.kloping.FlyChess", "1.3").info("飞行棋插件").build());
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        CommandManager.INSTANCE.registerCommand(CommandLine.INSTANCE, true);
        GlobalEventChannel.INSTANCE.registerListenerHost(new ListenerHost() {
            @EventHandler
            public void onMessage(@NotNull GroupMessageEvent event) throws Exception {
                MessageChain chain = event.getMessage();
                long q = event.getSender().getId();
                if (chain.size() > 1 && chain.get(1) instanceof PlainText) {
                    PlainText plainText = (PlainText) chain.get(1);
                    Object o = command(plainText.getContent().trim(), q, event.getGroup());
                    if (o == null) return;
                    if (o instanceof Message) event.getGroup().sendMessage((Message) o);
                    if (o instanceof String) event.getGroup().sendMessage(o.toString());
                }
            }
        });
    }

    private Object command(String str, long q, Contact contact) throws IOException {
        Rule.context = contact;
        switch (str) {
            case "创建飞行棋":
                return Rule.create();
            case "加入飞行棋":
                return Rule.join(q);
            case "掷骰子":
            case "扔色子":
                return Rule.shake(q);
            case "开始游戏":
                return Rule.start();
            case "/1":
                return Rule.select(q, 1);
            case "/2":
                return Rule.select(q, 2);
            case "/3":
                return Rule.select(q, 3);
            case "/4":
                return Rule.select(q, 4);
            case "/r":
                while (true) {
                    Rule.chess.getSide().step(5, 0);
                    contact.sendMessage(Rule.drawThis());
                }
            default:
                return null;
        }
    }

    @Override
    public void onLoad(@NotNull PluginComponentStorage storage) {
        super.onLoad(storage);
    }

    public static String getStringFrom(String path, Class<?> cla) throws IOException {
        URL url = cla.getClassLoader().getResource(path);
        InputStream is = url.openStream();
        byte[] bytes = ReadUtils.readAll(is);
        return new String(bytes);
    }

    public static byte[] getBytesFrom(String path, Class<?> cla) throws IOException {
        URL url = cla.getClassLoader().getResource(path);
        InputStream is = url.openStream();
        byte[] bytes = ReadUtils.readAll(is);
        return bytes;
    }

    public static URL getUrlsFrom(String path, Class<?> cla) {
        URL url = cla.getClassLoader().getResource(path);
        return url;
    }
}
