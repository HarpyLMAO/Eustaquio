package me.harpylmao.eustaquio.commands;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import me.harpylmao.eustaquio.managers.Eustaquio;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class DefaultCommand {

  public void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event) {
    Eustaquio eustaquio = Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId());
    if (args.length == 0) {
      command.reply(this.usage());
      return;
    } else {
      switch (args[0]) {
        default:
          break;
      }
    }
  }

  public String usage() {
    return "**Command Usage:** \n" +
            " - /command";
  }

  public String getName() {
    return "command";
  }
}
