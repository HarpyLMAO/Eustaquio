package me.harpylmao.eustaquio.commands.user;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import me.harpylmao.eustaquio.managers.user.User;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class UserCommand implements BaseCommand {

  @Override
  public void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event) {
    if (args.length == 2) {
      if (args[0].equalsIgnoreCase("reset")) {
        String id = args[1];
        assert id != null;

        if (!id.startsWith("<@!")) {
          command.reply("Please tag a member");
          return;
        } else {
          String formatedId = id.replace("<@!", "").replace(">", "");
          if (Bot.getInstance().getUserManager().getUserObjectRepository().find(formatedId) != null) {
            Bot.getInstance().getUserManager().getUserObjectRepository().delete(formatedId);
            command.getGuild().retrieveMemberById(formatedId).queue(cachedMember -> {
              User user = new User(formatedId, cachedMember.getUser().getName());
              Bot.getInstance().getUserManager().getUserObjectRepository().save(user);
              command.reply("Successfully operation, member was reset.");
            });
          } else {
            command.reply("**ERROR:** User not found, we can't delete hes profile.");
            return;
          }
        }
      } else if (args[0].equalsIgnoreCase("staff")) {
        String id = args[1];
        assert id != null;

        if (!id.startsWith("<@!")) {
          command.reply("Please tag a member");
          return;
        } else {
          String formatedId = id.replace("<@!", "").replace(">", "");
          if (Bot.getInstance().getUserManager().getUserObjectRepository().find(formatedId) != null) {
            command.getGuild().retrieveMemberById(formatedId).queue(cachedMember -> {
              User user = Bot.getInstance().getUserManager().getUserObjectRepository().find(formatedId);
              assert user != null;
              if (user.isStaff()) {
                user.setStaff(false);
                command.getGuild().getTextChannels().forEach(var1 -> {
                  if (var1.getName().startsWith("ticket-")) {
                    if (var1.getMemberPermissionOverrides().stream().filter(permissionOverride -> permissionOverride.getMember().equals(cachedMember)).findFirst().orElse(null) != null) {
                      var1.getPermissionOverride(cachedMember)
                              .getManager().setDeny(Permission.ALL_PERMISSIONS).queue();
                      System.out.println("removed permission override member from tickets.");
                    }
                  }
                });
                command.reply("Successfully operation, revoked staff permissions for " + user.getName());
              } else {
                user.setStaff(true);
                command.getGuild().getTextChannels().forEach(var1 -> {
                  if (var1.getName().startsWith("ticket-")) {
                    var1.getPermissionOverride(cachedMember)
                            .getManager()
                            .setAllow(
                                    Permission.VIEW_CHANNEL,
                                    Permission.MESSAGE_WRITE,
                                    Permission.MESSAGE_READ,
                                    Permission.MESSAGE_HISTORY,
                                    Permission.MESSAGE_EMBED_LINKS,
                                    Permission.MESSAGE_ATTACH_FILES,
                                    Permission.MESSAGE_ADD_REACTION,
                                    Permission.MESSAGE_EXT_EMOJI
                            ).queue();
                    System.out.println("granted permission override member from tickets.");
                  }
                });
                command.reply("Successfully operation, granted staff permissions for " + user.getName());
              }
              Bot.getInstance().getUserManager().getUserObjectRepository().save(user);
            });
          } else {
            command.reply("**ERROR:** User not found, we can't delete hes profile.");
            return;
          }
        }
      }
    } else {
      command.reply(this.usage());
    }
  }

  @Override
  public String usage() {
    return "USAGE: /user reset <tag>";
  }

  @Override
  public String getName() {
    return "user";
  }

  @Override
  public String category() {
    return "administrators";
  }
}
