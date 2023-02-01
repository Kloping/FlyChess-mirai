package io.github.Kloping;

import io.github.kloping.initialize.FileInitializeValue;
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
    public static Config config = new Config();

    public FlyChess() {
        super(new JvmPluginDescriptionBuilder("com.github.kloping.FlyChess", "1.9.2").info("飞行棋插件").build());
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
                onMessage0(event);
            }

            @EventHandler
            public void onMessage(@NotNull GroupMessageSyncEvent event) throws Exception {
                onMessage0(event);
            }

            private void onMessage0(MessageEvent event) throws Exception {
                MessageChain chain = event.getMessage();
                long q = event.getSender().getId();
                if (chain.size() > 1 && chain.get(1) instanceof PlainText) {
                    PlainText plainText = (PlainText) chain.get(1);
                    Object o = command(plainText.getContent().trim(), q, event.getSubject());
                    outR(event, o);
                }
            }

        });
        config = FileInitializeValue.getValue("./conf/flychess/conf.json", config, true);
    }

    public void outR(@NotNull MessageEvent event, Object o) {
        if (o == null) return;
        if (o instanceof Message) event.getSubject().sendMessage((Message) o);
        if (o instanceof String) event.getSubject().sendMessage(o.toString());
    }

    private Object command(String str, long q, Contact contact) throws IOException {
        str = str.trim();
        for (String s : config.getShake()) {
            if (str.equals(s)) {
                Rule.context = contact;
                Rule.shake(q, false);
                return null;
            }

        }
        if (str.equals(config.getCreate())) {
            Rule.context = contact;
            return Rule.create();
        } else if (str.equals(config.getJoin())) {
            Rule.context = contact;
            return Rule.join(q);
        } else if (str.equals(config.getStart())) {
            Rule.context = contact;
            return Rule.start();
        } else if (str.equals(config.getOne())) {
            Rule.context = contact;
            Rule.select(q, 1, true);
            return null;
        } else if (str.equals(config.getTwo())) {
            Rule.context = contact;
            Rule.select(q, 2, true);
            return null;
        } else if (str.equals(config.getTree())) {
            Rule.context = contact;
            Rule.select(q, 3, true);
            return null;
        } else if (str.equals(config.getFour())) {
            Rule.context = contact;
            Rule.select(q, 4, true);
            return null;
        }
        return null;
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
