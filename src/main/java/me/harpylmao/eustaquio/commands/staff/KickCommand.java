package me.harpylmao.eustaquio.commands.staff;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import me.harpylmao.eustaquio.managers.Eustaquio;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class KickCommand implements BaseCommand {

    @Override
    public void execute(
            CommandEvent command,
            TextChannel textChannel,
            Member member,
            String[] args,
            GuildMessageReceivedEvent event
    ) {
        Eustaquio eustaquio = Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId());

        if (args.length == 0) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(eustaquio.getColorColored());
            embedBuilder.setTitle("Who you want kick 😈.");
            embedBuilder.setImage("https://c.tenor.com/7EYsZ1IF23YAAAAC/thor-avengers.gif");
            command.getChannel().sendMessage(embedBuilder.build()).queue();
        } else {
            String id = args[0].replace("<@!", "").replace(">", "");

            command.getGuild().retrieveMemberById(id).queue(cachedMember -> {
                try {
                    command.getGuild()
                            .kick(cachedMember)
                            .queue();
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(eustaquio.getColorColored());
                    embedBuilder.setTitle(cachedMember.getUser().getName() + " has been kicked from the server 😈.");
                    embedBuilder.setImage("https://c.tenor.com/k0jaPE9KtNsAAAAM/almighty-thunder.gif");
                    command.getChannel().sendMessage(embedBuilder.build()).queue();
                } catch (Exception exception) {
                    exception.printStackTrace();
                    return;
                }
            });
        }
    }

    @Override
    public String category() {
        return "staffs";
    }

    @Override
    public String usage() {
        return "**Command Usage:** \n" +
                " - /kick <tag>";
    }

    @Override
    public String getName() {
        return "kick";
    }
}
