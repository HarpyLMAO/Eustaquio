package me.harpylmao.eustaquio.commands.cars;

import com.sun.source.tree.Tree;
import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import me.harpylmao.eustaquio.managers.Eustaquio;
import me.harpylmao.eustaquio.managers.car.Car;
import me.harpylmao.eustaquio.managers.car.CarManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
    Author: HarpyLMAO.
    All rights reserved.
    Project: Eustaquio
    27/12/2021 02
*/
public class CarCommand implements BaseCommand {

    @Override
    public void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event) {
        Eustaquio eustaquio = Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId());
        if (args.length == 0) {
            command.reply(this.usage());
        } else {
            switch (args[0]) {
                case "create" -> {
                    String name = StringUtils.join(args, ' ', 1, args.length - 1);
                    int price = Integer.parseInt(args[args.length - 1]);

                    try {
                        Car car = new Car(UUID.randomUUID().toString(), name);
                        car.setPrice(price);
                        Bot.getInstance().getCarManager().getCarObjectRepository().save(car);
                        event.getMessage().reply(new EmbedBuilder()
                                        .setColor(eustaquio.getColorColored())
                                        .setDescription(
                                                "Car created (Name: " + car.getName() + ", Price: " + car.getPrice() + ")\n" +
                                                        "Later you need to set the horsepower and speed peak."
                                        ).build())
                                .queue();
                        Bot.getInstance().getCarManager().save(car);
                    } catch (NumberFormatException exception) {
                        exception.printStackTrace();
                    }
                }
                case "horsepower" -> {
                    if (args.length == 3) {
                        String carName = args[1];

                        if (carName != null) {
                            int horsepower = Integer.parseInt(args[2]);
                            try {
                                Car car = Car.getCarByName(carName);
                                if (car != null) {
                                    car.setHorsepower(horsepower);
                                    event.getMessage().reply(new EmbedBuilder()
                                                    .setColor(eustaquio.getColorColored())
                                                    .setDescription(
                                                            "Car horsepower modified to " + horsepower + "."
                                                    )
                                                    .build())
                                            .queue();
                                    Bot.getInstance().getCarManager().save(car);
                                }
                            } catch (NumberFormatException exception) {
                                exception.printStackTrace();
                            }
                        } else {
                            command.reply("Car doesnt exists");
                        }
                    }
                }
                case "speedpeak" -> {
                    if (args.length == 3) {
                        String carName = args[1];

                        if (carName != null) {
                            int speedpeak = Integer.parseInt(args[2]);
                            try {
                                Car car = Car.getCarByName(carName);
                                if (car != null) {
                                    car.setHorsepower(speedpeak);
                                    event.getMessage().reply(new EmbedBuilder()
                                                    .setColor(eustaquio.getColorColored())
                                                    .setDescription(
                                                            "Car speedpeak modified to " + speedpeak + "."
                                                    )
                                                    .build())
                                            .queue();
                                    Bot.getInstance().getCarManager().save(car);
                                }
                            } catch (NumberFormatException exception) {
                                exception.printStackTrace();
                            }
                        } else {
                            command.reply("Car doesnt exists");
                        }
                    }
                }
                case "list" -> {
                    List<Car> cars = Car.getCars();
                    String car = cars.stream()
                            .distinct()
                            .map(s -> "> " + s.getName()).collect(Collectors.joining("\n"));
                    event.getMessage().reply(new EmbedBuilder()
                                    .setColor(eustaquio.getColorColored())
                                    .setDescription(car)
                                    .build())
                            .queue();
                }
                default -> {
                    command.reply(this.usage());
                }
            }
        }
    }

    @Override
    public String usage() {
        return """
                USAGE:
                /car create <name> <price>
                /car horsepower <horsepower>
                /car speedpeak <speedpeak>""";
    }

    @Override
    public String getName() {
        return "car";
    }
}
