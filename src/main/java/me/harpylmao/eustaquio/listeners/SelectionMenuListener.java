package me.harpylmao.eustaquio.listeners;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.managers.Eustaquio;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Locale;

/*
    Author: HarpyLMAO.
    All rights reserved.
    Project: Eustaquio
    26/12/2021 01
*/
public class SelectionMenuListener extends ListenerAdapter {

    @Override
    public void onSelectionMenu(SelectionMenuEvent event) {
        Eustaquio eustaquio = Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId());
        if (event.getComponentId().equals("menu:help")) {
            String value = event.getValues().get(0);
            switch (value.toLowerCase(Locale.ROOT)) {
                default:
                    break;
                case "main":
                    /*EmbedBuilder helpBuilder = new EmbedBuilder();
                    List<String> description = new ArrayList<>();
                    for (Category category : Category.getCategories()) {
                        StringBuilder builder = new StringBuilder();
                        description.add("__** " + category.getName() + "**__");
                        for (BaseCommand baseCommand : category.getCommands()) {
                            if (!category.getName().equals("general") || !category.getName().equals("users"))
                                return;
                            builder.append("➥ `").append(baseCommand.getName()).append("` ");
                        }
                        description.add(builder.toString());
                    }
                    helpBuilder.setTitle("Main help menu");
                    helpBuilder.setDescription(String.join("\n", description));
                    helpBuilder.setFooter("Requested by" + event.getUser().getName(), event.getUser().getEffectiveAvatarUrl());*/
                    event.getTextChannel().sendMessage(
                                    new EmbedBuilder()
                                            .setColor(eustaquio.getColorColored())
                                            .setTitle("Main help commands.")
                                            .setDescription("")
                                            .setFooter("Requested by" + event.getUser().getName(), event.getUser().getEffectiveAvatarUrl())
                                            .build())
                            .queue();
                    break;
            }
        }
    }
}
