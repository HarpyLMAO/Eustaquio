package me.harpylmao.eustaquio.commands.user;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.managers.Eustaquio;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.stream.Collectors;

public class StatsCommand implements BaseCommand {
    @Override
    public void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event) {
        Eustaquio eustaquio = Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId());
        if (args.length == 0) {
            me.harpylmao.eustaquio.managers.user.User userMember = Bot.getInstance().getUserManager().getUserObjectRepository().find(member.getId());

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(eustaquio.getColorColored());
            embedBuilder.setTitle("**" + member.getUser().getName() + "'s stats**");
            embedBuilder.setThumbnail(member.getUser().getEffectiveAvatarUrl());
            embedBuilder.addField("Level", "" + userMember.getLevel() + " (" + userMember.getExperience() + "%)", true);
            embedBuilder.addField("Groups", "" + getUserGroups(member), true);
            textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
        } else if (args.length == 1) {
            String id = args[0];
            if (NumberUtils.isParsable(id)) {
                User discordUser = command.getJDA().getUserById(id);

                if (discordUser == null) {
                    command.reply("**ERROR:** Discord user vinculated with that id not found.");
                    return;
                }

                me.harpylmao.eustaquio.managers.user.User user = Bot.getInstance().getUserManager().getUserObjectRepository().find(id);

                EmbedBuilder embedBuilder2 = new EmbedBuilder();
                embedBuilder2.setColor(eustaquio.getColorColored());
                embedBuilder2.setTitle("**" + discordUser.getName() + "'s stats**");
                embedBuilder2.setThumbnail(discordUser.getEffectiveAvatarUrl());
                embedBuilder2.addField("Level", "" + user.getLevel() + " (" + user.getExperience() + "%)", true);
                embedBuilder2.addField("Groups", "" + getUserGroups(command.getGuild().getMember(discordUser)), true);
                textChannel.sendMessageEmbeds(embedBuilder2.build()).queue();
            } else {
                EmbedBuilder therockEmbed = new EmbedBuilder();
                therockEmbed.setColor(eustaquio.getColorColored());
                therockEmbed.setTitle("Are you idiot?");
                therockEmbed.setImage("https://c.tenor.com/ZX95mDnlodwAAAAM/the-rock-sus-eye.gif");
                command.getMessage().replyEmbeds(therockEmbed.build()).queue();
                return;
            }
        }
    }

    public String getUserGroups(Member member) {
        return member.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.joining("\n"));
    }


    @Override
    public String category() {
        return "users";
    }

    @Override
    public String usage() {
        return "/stats | /stats (tag)";
    }

    @Override
    public String getName() {
        return "stats";
    }
}
