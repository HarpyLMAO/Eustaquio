package me.harpylmao.eustaquio.commands.staff;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import me.harpylmao.eustaquio.managers.Eustaquio;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;

public class UnmuteCommand implements BaseCommand {

    @Override
    public void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event) {
        Eustaquio eustaquio = Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId());

        String id = args[0].replace("<@!", "").replace(">", "");
        Role role = command.getGuild().getRoleById(eustaquio.getMutedRoleId());

        command.getGuild().retrieveMemberById(id).queue(cachedMember -> {
            try {
                AuditableRestAction<Void> action = command
                        .getGuild()
                        .removeRoleFromMember(cachedMember, role);

                action.queue();

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(eustaquio.getColorColored());
                embedBuilder.setTitle("You're now unmuted " + cachedMember.getUser().getName());
                embedBuilder.setImage("https://c.tenor.com/sHYApzPFT9MAAAAd/goku-unmuted.gif");
                command.reply("Successfully operation, member unmuted.");
                command.getChannel().sendMessage(embedBuilder.build()).queue();
            } catch (Exception exception) {
                exception.printStackTrace();
                return;
            }
        });
    }

    @Override
    public String category() {
        return "staffs";
    }

    @Override
    public String usage() {
        return "**Command Usage:** \n" +
                " - /unmute <tag>";
    }

    @Override
    public String getName() {
        return "unmute";
    }
}
