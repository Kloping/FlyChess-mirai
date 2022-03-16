import io.github.Kloping.Rule;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;

/**
 * @author github.kloping
 */
public class m0 {
    public static void main(String[] args) throws Exception {
        MiraiConsoleTerminalLoader.INSTANCE.startAsDaemon(new MiraiConsoleImplementationTerminal());
        System.out.println(Rule.getFormat(1001*60*60*25));
    }
}
