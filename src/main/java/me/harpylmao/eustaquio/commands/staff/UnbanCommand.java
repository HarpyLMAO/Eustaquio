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

public class UnbanCommand implements BaseCommand {

    @Override
    public void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event) {
        Eustaquio eustaquio = Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId());
        if (args.length == 0) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(eustaquio.getColorColored());
            embedBuilder.setTitle("Who you want unban 😈.");
            embedBuilder.setImage("https://c.tenor.com/7EYsZ1IF23YAAAAC/thor-avengers.gif");
            command.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        } else {
            String id = args[0];
            if (!id.startsWith("<@!")) {
                command.reply("**ERROR:** You need tag a member.");
                return;
            } else {
                String formatedId = id.replace("<@!", "").replace(">", "");
                Member cachedMember = command.getGuild().getMemberById(formatedId);
                if (cachedMember == null) return;
                try {
                    command.getGuild()
                            .unban(cachedMember.getUser())
                            .reason(args.length == 1 ? "You are unbanned reason: none" : args[0])
                            .queue();
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(eustaquio.getColorColored());
                    embedBuilder.setTitle(cachedMember.getUser().getName() + " has been unbaned from the server 😈.");
                    embedBuilder.setImage("https://c.tenor.com/m9hcQszgFjUAAAAC/zombie-rip.gif");
                    command.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                } catch (HierarchyException exception) {
                    command.reply("Can't modify a member with higher or equal highest role than yourself!");
                    exception.printStackTrace();
                    return;
                }
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
                " - /unban <tag>";
    }

    @Override
    public String getName() {
        return "unban";
    }
}
