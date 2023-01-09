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
import net.mamoe.mirai.event.events.GroupMessageSyncEvent;
import net.mamoe.mirai.event.events.MessageEvent;
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
        super(new JvmPluginDescriptionBuilder("com.github.kloping.FlyChess", "1.7")
                .info("飞行棋插件").build());
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public MessageEvent event;

    @Override
    public void onEnable() {
        CommandManager.INSTANCE.registerCommand(CommandLine.INSTANCE, true);
        GlobalEventChannel.INSTANCE.registerListenerHost(new ListenerHost() {
            @EventHandler
            public void onMessage(@NotNull GroupMessageEvent event) throws Exception {
                FlyChess.INSTANCE.event = event;
                onMessage();
            }

            @EventHandler
            public void onMessage(@NotNull GroupMessageSyncEvent event) throws Exception {
                FlyChess.INSTANCE.event = event;
                onMessage();
            }

            private void onMessage() throws Exception {
                MessageChain chain = event.getMessage();
                long q = event.getSender().getId();
                if (chain.size() > 1 && chain.get(1) instanceof PlainText) {
                    PlainText plainText = (PlainText) chain.get(1);
                    Object o = command(plainText.getContent().trim(), q, event.getSubject());
                    outR(event, o);
                }
            }

        });
    }

    public void outR(@NotNull MessageEvent event, Object o) {
        if (o == null) return;
        this.event = event;
        if (o instanceof Message) event.getSubject().sendMessage((Message) o);
        if (o instanceof String) event.getSubject().sendMessage(o.toString());
    }

    private Object command(String str, long q, Contact contact) throws IOException {
        Rule.context = contact;
        switch (str) {
            case "创建飞行棋":
                return Rule.create();
            case "掷骰子":
            case "掷色子":
            case "扔色子":
            case "扔骰子":
                Rule.shake(q, false);
                return null;
            case "加入飞行棋":
                return Rule.join(q);
            case "开始游戏":
                return Rule.start();
            case "/1":
                Rule.select(q, 1, false);
                return null;
            case "/2":
                Rule.select(q, 2, false);
                return null;
            case "/3":
                Rule.select(q, 3, false);
                return null;
            case "/4":
                Rule.select(q, 4, false);
                return null;
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
