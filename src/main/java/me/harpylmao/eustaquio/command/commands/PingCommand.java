package me.harpylmao.eustaquio.command.commands;

import me.harpylmao.eustaquio.command.CommandContext;
import me.harpylmao.eustaquio.command.ICommand;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        System.out.println("A");

        jda.getRestPing().queue(
                (ping) -> ctx.getChannel()
                .sendMessageFormat("Pong: %sms", ping, jda.getGatewayPing()).queue()
        );
    }

    @Override
    public String getHelp() {
        return "Te enseña el ping del bot pishita";
    }

    @Override
    public String getName() {
        return "ping";
    }
}
