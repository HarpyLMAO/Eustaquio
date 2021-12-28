package me.harpylmao.eustaquio.managers.car.race;

import lombok.Getter;
import lombok.Setter;
import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.managers.Eustaquio;
import me.harpylmao.eustaquio.managers.car.Car;
import me.harpylmao.eustaquio.managers.user.User;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.*;
import java.util.stream.Collectors;

/*
    Author: HarpyLMAO.
    All rights reserved.
    Project: Eustaquio
    26/12/2021 18
*/

@Getter
@Setter
public class Race {

    @Getter
    private static List<Race> races = new ArrayList<>();

    private UUID raceUuid;

    private List<User> participants = new ArrayList<>();
    private List<Car> cars = new ArrayList<>();

    public Race(
            UUID raceUuid
    ) {
        this.raceUuid = raceUuid;
        races.add(this);
    }

    public static Race getRaceByUuid(UUID uuid) {
        return races.stream().filter(race -> race.getRaceUuid().equals(uuid)).findFirst().orElse(null);
    }

    public void prepareRace(TextChannel textChannel) {
        Eustaquio eustaquio = Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId());
        MessageAction messageAction = textChannel.sendMessage("Race starting in 1m...");

        messageAction.setActionRow(Button.success("join race", "Join Race")
                        .withEmoji(Emoji.fromUnicode("🏎")))
                .queue(message -> {
                    Bot.getInstance().getEventWaiter().waitForEvent(ButtonClickEvent.class, buttonClickEvent -> {

                        if (buttonClickEvent.getUser().isBot()) return false;
                        return buttonClickEvent.getMessageId().equalsIgnoreCase(message.getId());

                    }, buttonClickEvent -> {
                        buttonClickEvent.getInteraction().deferEdit().complete();
                        User user = Bot.getInstance().getUserManager().getUserObjectRepository().find(buttonClickEvent.getUser().getId());

                        if (user.getCars().size() == 0) {
                            buttonClickEvent.replyEmbeds(
                                            new EmbedBuilder()
                                                    .setColor(eustaquio.getColorColored())
                                                    .setDescription("You need at least 1 car to participe in the race.")
                                                    .build()
                                    )
                                    .setEphemeral(true)
                                    .queue();
                            return;
                        }
                        this.participants.add(user);

                        SelectionMenu.Builder selectionMenu = SelectionMenu.create("menu:user:cars")
                                .setPlaceholder("Select one of your cars")
                                .setRequiredRange(1, 1);

                        for (Car userCar : user.getCars()) {
                            selectionMenu.addOption(userCar.getName(), "car|" + userCar.getId());
                        }

                        MessageAction selectionMessage = buttonClickEvent.getMessage().reply("Select the car you want for the race.");

                        selectionMessage.setActionRow(selectionMenu.build()).queue(actionMessage -> {
                            Bot.getInstance().getEventWaiter().waitForEvent(SelectionMenuEvent.class, selectionMenuEvent -> {

                                if (selectionMenuEvent.getUser().isBot()) return false;
                                return selectionMenuEvent.getMessageId().equalsIgnoreCase(actionMessage.getId());

                            }, selectionMenuEvent -> {
                                selectionMenuEvent.getInteraction().deferEdit().complete();

                                String value = selectionMenuEvent.getValues().get(0);

                                String[] args = value.split("\\|");
                                Car userCar = Car.getCarById(args[1]);

                                if (userCar == null) return;

                                this.cars.add(userCar);

                            });
                        });

                    });

                });
    }

    public void startRace() {

    }

}
