package me.harpylmao.eustaquio.commands.staff;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import me.harpylmao.eustaquio.managers.Eustaquio;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MuteCommand implements BaseCommand {


    @Override
    public void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event) {
        Eustaquio eustaquio = Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId());
        if (args.length == 1) {
            if (command.getGuild().getRoles().stream().filter(role -> role.getName().equalsIgnoreCase("Muted")).findFirst().orElse(null) == null) {

                command.getGuild().createRole()
                        .setName("Muted")
                        .setColor(Color.LIGHT_GRAY)
                        .queue(muted -> {
                            eustaquio.setMutedRoleId(muted.getId());
                            Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().save(eustaquio);

                            for (Category category : command.getGuild().getCategories()) {
                                category.createPermissionOverride(muted).setDeny(Permission.VOICE_SPEAK, Permission.MESSAGE_WRITE);
                            }
                        });
                
            }

            Runnable runnable = () -> {
                String id = args[0].replace("<@!", "").replace(">", "");
                Role role = command.getGuild().getRoleById(eustaquio.getMutedRoleId());

                command.getGuild().retrieveMemberById(id).queue(cachedMember -> {
                    try {
                        AuditableRestAction<Void> action = command
                                .getGuild()
                                .addRoleToMember(cachedMember, role);

                        action.queue();

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setColor(eustaquio.getColorColored());
                        embedBuilder.setTitle("You're now muted. Stfu " + cachedMember.getUser().getName());
                        embedBuilder.setImage("https://c.tenor.com/DosyOk8bRkYAAAAC/mute-discord.gif");
                        command.reply("Successfully operation, member muted.");
                        command.getChannel().sendMessage(embedBuilder.build()).queue();

                    } catch (Exception exception) {
                        exception.printStackTrace();
                        return;
                    }
                });
            };

            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.schedule(runnable, 2, TimeUnit.SECONDS);
            executor.shutdown();
        }
    }

    @Override
    public String category() {
        return "staffs";
    }

    @Override
    public String usage() {
        return "**Command Usage:** \n" +
                " - /mute <tag>";
    }

    @Override
    public String getName() {
        return "mute";
    }
}
