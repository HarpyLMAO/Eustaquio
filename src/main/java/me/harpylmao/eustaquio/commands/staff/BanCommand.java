package me.harpylmao.eustaquio.commands.staff;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import me.harpylmao.eustaquio.managers.Eustaquio;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

public class BanCommand implements BaseCommand {

    @Override
    public void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event) {
        Eustaquio eustaquio = Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId());
        if (args.length == 0) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(eustaquio.getColorColored());
            embedBuilder.setTitle("Who you want ban 😈.");
            embedBuilder.setImage("https://c.tenor.com/7EYsZ1IF23YAAAAC/thor-avengers.gif");
            command.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        } else {
            String id = args[0];
            if (!id.startsWith("<@!")) {
                command.reply("**ERROR:** You need tag a member.");
                return;
            } else {
                String formatedId = id.replace("<@!", "").replace(">", "");
                command.getGuild().retrieveMemberById(formatedId).queue(cachedMember -> {
                    if (cachedMember == null) return;
                    try {
                        command.getGuild()
                                .ban(cachedMember, 7)
                                .reason(args.length == 1 ? "You are banned reason: none" : args[0])
                                .queue();
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setColor(eustaquio.getColorColored());
                        embedBuilder.setTitle(cachedMember.getUser().getName() + " has been banned from the server 😈.");
                        embedBuilder.setImage("https://c.tenor.com/k0jaPE9KtNsAAAAM/almighty-thunder.gif");
                        command.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                    } catch (HierarchyException exception) {
                        command.reply("Can't modify a member with higher or equal highest role than yourself!");
                        exception.printStackTrace();
                        return;
                    }
                });
            }
        }
    }

    @Override
    public String category() {
        return "staffs";
    }

    @Override
    public String usage() {
        return "**Command Usage:** \n" +
                " - /ban <tag>";
    }

    @Override
    public String getName() {
        return "ban";
    }
}
