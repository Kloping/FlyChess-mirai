## 一个群内飞行棋的插件

玩法: 普通飞行棋玩法 2,4,6点 起飞 同色方块跳跃

其命令:
![img.png](img.png)



    创建飞行棋
    加入飞行棋        #最大四个玩家,最小2个 
    掷骰子/扔色子     
    开始游戏         #人数2,3人时使用即可开始游戏
    /1             #选择要操作的棋子 下同
    /2
    /3
    /4

![img_1.png](img_1.png)

配合 [chat-command](https://github.com/project-mirai/chat-command)

    /flyChess clearTemp    # 清除缓存图片
    /flyChess overGame    # 结束游戏