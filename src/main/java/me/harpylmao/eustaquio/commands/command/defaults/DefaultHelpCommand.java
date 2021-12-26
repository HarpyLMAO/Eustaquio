package me.harpylmao.eustaquio.commands.command.defaults;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.Category;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import me.harpylmao.eustaquio.managers.Eustaquio;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DefaultHelpCommand implements BaseCommand {

    @Override
    public void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event) {
        Eustaquio eustaquio = Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId());
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(eustaquio.getColorColored())
                .setTitle("Eustaquio Help Menus")
                .setDescription("\n" +
                        "**Main help:** If you want know all main commands\n" +
                        "please select _Main help_ in the options.\n" +
                        "\n" +
                        "**Music help:** If you want know all about music bot\n" +
                        "choose in the menu _Music help_.\n" +
                        "\n" +
                        "**Cars help:** If you want know about races & cars select\n" +
                        "_Cars help_ in the options of the menu.\n")
                .setFooter("Information requested by " + member.getUser().getName(), member.getUser().getEffectiveAvatarUrl());
        textChannel.sendMessageEmbeds(embedBuilder.build())
                .setActionRow(getSelectionMenu())
                .queue();
    }

    private SelectionMenu getSelectionMenu() {
        return SelectionMenu.create("menu:help")
                .setPlaceholder("Choose what help you need")
                .setRequiredRange(1, 1)
                .addOption("Main help", "main", "Shows you all main help of the bot")
                .addOption("Music help", "music", "Shows you all commands of the music bot")
                .addOption("User help", "user", "State: IN DEVELOPMENT")
                .build();
    }

    /*private void handleSelect(Message message, Member author) {
        Bot.getInstance().getEventWaiter().waitForEvent(SelectionMenuEvent.class, event -> {
            if (event.getUser().isBot()) return false;
            if (event.getMember() == null) return false;
            if (!event.getComponentId().equalsIgnoreCase("menu:help")) return false;
            if (event.isAcknowledged()) event.deferEdit().queue();
            if (event.getValues().size() > 1) return false;

            return event.getMessageId().equals(message.getId());
        }, event -> {
            TextChannel textChannel = event.getTextChannel();
            String value = event.getValues().get(0);

            System.out.println(value);

            switch (value.toLowerCase(Locale.ROOT)) {
                case "main":
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    List<String> description = new ArrayList<>();
                    for (Category category : Category.getCategories()) {
                        StringBuilder builder = new StringBuilder();
                        description.add("__** " + category.getName() + "**__");
                        for (BaseCommand baseCommand : category.getCommands()) {
                            if (!category.getName().equals("general") || !category.getName().equals("users")) return;
                            builder.append("➥ `").append(baseCommand.getName()).append("` ");
                        }
                        description.add(builder.toString());
                    }
                    embedBuilder.setTitle("Main help menu");
                    embedBuilder.setDescription(String.join("\n", description));
                    embedBuilder.setFooter("Requested by" + author.getUser().getName(), author.getUser().getEffectiveAvatarUrl());
                    textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
                    break;
            }
        }, 1, TimeUnit.SECONDS, () -> {

        });
    }*/

    @Override
    public String usage() {
        return "usage";
    }

    @Override
    public String getName() {
        return "help";
    }
}
