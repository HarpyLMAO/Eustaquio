package me.harpylmao.eustaquio.commands.command.defaults;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.commands.command.CommandManager;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.Category;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ryzeon Project: JDA-CommandAPI Date: 06/06/2021 @ 21:01 Twitter: @Ryzeon_ 😎 Github:
 * github.ryzeon.me
 */
public class DefaultHelpCommand implements BaseCommand {

    @Override
    public void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event) {
        if (args.length == 0) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored());
            List<String> description = new ArrayList<>();
            for (Category category : Category.getCategories()) {
                StringBuilder builder = new StringBuilder();
                description.add("__** " + (category.getName().toUpperCase().substring(0, 1).toUpperCase() + category.getName().substring(1)) + "**__");
                for (BaseCommand baseCommand : category.getCommands()) {
                    builder.append("➥ `").append(baseCommand.getName()).append("`\n");
                }
                description.add(builder.toString());
            }
            embedBuilder.setFooter(
                    "Si necesitas información detallada de un comando usa "
                            + CommandManager.INSTANCE.getPrefix()
                            + "help [comando]");
            embedBuilder.setDescription(String.join("\n", description));
            textChannel.sendMessage(embedBuilder.build()).queue();
            return;
        }
        String label = args[0];
        BaseCommand baseCommand = CommandManager.INSTANCE.getCommandByNameOrAlias(label);
        if (baseCommand != null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId()).getColorColored());
            embedBuilder.setTitle("**" + label + " Info**");
            embedBuilder.addField("Uso:", baseCommand.usage(), false);
            embedBuilder.addField(
                    "Aliases: ",
                    (baseCommand.aliases().isEmpty()
                            ? "No hay ningun aliase"
                            : String.join(", ", baseCommand.aliases())),
                    false);
            command.getMessage().reply(embedBuilder.build()).queue();
        } else {
            command.reply("No se ha encontrado ningun comando con " + label);
        }
    }

    @Override
    public String usage() {
        return "Get available commands";
    }

    @Override
    public List<String> aliases() {
        return Collections.singletonList("ayuda");
    }

    @Override
    public String getName() {
        return "help";
    }
}
