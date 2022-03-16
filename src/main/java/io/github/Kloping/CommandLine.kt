package io.github.Kloping

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.java.JCompositeCommand
import java.io.File

class CommandLine private constructor() : JCompositeCommand(FlyChess.INSTANCE, "flyChess") {
    companion object {
        @JvmField
        val INSTANCE = CommandLine()
    }

    @Description("结束游戏")
    @SubCommand("overGame")
    suspend fun CommandSender.flyChessOver() {
        Rule.destroy();
        sendMessage("游戏结束")
    }

    @Description("清除缓存图片")
    @SubCommand("clearTemp")
    suspend fun CommandSender.flyChessClear() {
        File("./temp")?.listFiles { it -> it.name.endsWith("jpg") }
            ?.forEach { f -> f.delete() }
        sendMessage("清除完成")
    }
}