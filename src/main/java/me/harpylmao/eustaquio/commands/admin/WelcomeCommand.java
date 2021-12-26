package me.harpylmao.eustaquio.commands.admin;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.managers.Eustaquio;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class WelcomeCommand implements BaseCommand {

    @Override
    public void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event) {
        Eustaquio eustaquio = Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId());
        String modifiedMessage = "**Welcome messages sucessfully modified to " + (eustaquio.isWelcomeMessages() ? "OFF" : "ON") + "**";
        if (args.length == 0) {
            textChannel.sendMessage("**" + this.usage() + "**").queue();
        } else {
            switch (args[0]) {
                case "on":
                    if (!eustaquio.isWelcomeMessages()) {
                        eustaquio.setWelcomeMessages(true);
                        textChannel.sendMessage(modifiedMessage).queue();
                    } else {
                        textChannel.sendMessage("**Welcome messages already enabled**").queue();
                    }
                    break;
                case "off":
                    if (eustaquio.isWelcomeMessages()) {
                        eustaquio.setWelcomeMessages(false);
                        textChannel.sendMessage(modifiedMessage).queue();
                    } else {
                        textChannel.sendMessage("**Welcome messages already disabled**").queue();
                    }
                    break;
                case "set":
                    eustaquio.setWelcomeChannel(command.getChannel().getId());
                    textChannel.sendMessage("Welcome channel was modified!").queue();
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
        return "Usage: /welcome <on/off/set>";
    }

    @Override
    public String getName() {
        return "welcome";
    }

    @Override
    public String category() {
        return "administrators";
    }
}
