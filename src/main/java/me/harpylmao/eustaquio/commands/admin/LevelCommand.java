package me.harpylmao.eustaquio.commands.admin;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.managers.Eustaquio;
import me.harpylmao.eustaquio.managers.user.User;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class LevelCommand implements BaseCommand {

    @Override
    public void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event) {
        Eustaquio eustaquio = Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId());
        String modifiedMessage = "**Level messages sucessfully modified to " + (eustaquio.isLevelMessages() ? "OFF" : "ON") + "**";
        if (args.length == 0) {
            textChannel.sendMessage("**" + this.usage() + "**").queue();
        } else {
            switch (args[0]) {
                case "mirar":
                    User user = Bot.getInstance().getUserManager().getUserObjectRepository().find(member.getId());
                    textChannel.sendMessage("level: " + user.getLevel() + ", experience: " + user.getExperience()).queue();
                    break;
                case "on":
                    if (!eustaquio.isLevelMessages()) {
                        eustaquio.setLevelMessages(true);
                        textChannel.sendMessage(modifiedMessage).queue();
                    } else {
                        textChannel.sendMessage("**Level messages already enabled**").queue();
                    }
                    break;
                case "off":
                    if (eustaquio.isLevelMessages()) {
                        eustaquio.setLevelMessages(false);
                        textChannel.sendMessage(modifiedMessage).queue();
                    } else {
                        textChannel.sendMessage("**Level messages already disabled**").queue();
                    }
                    break;
                case "set":
                    eustaquio.setLevelChannel(command.getChannel().getId());
                    textChannel.sendMessage("Level channel was modified!").queue();
                    break;
                default:
                    textChannel.sendMessage("**" + this.usage() + "**").queue();
                    break;
            }
            Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().save(eustaquio);
        }
    }

    @Override
    public String usage() {
        return "Usage: /levels <on/off/set>";
    }

    @Override
    public String getName() {
        return "levels";
    }

    @Override
    public String category() {
        return "administrators";
    }
}
