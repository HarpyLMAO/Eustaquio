package me.harpylmao.eustaquio.commands.races;

import kotlin.collections.EmptyList;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import me.harpylmao.eustaquio.managers.car.Car;
import me.harpylmao.eustaquio.managers.car.race.Race;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/*
    Author: HarpyLMAO.
    All rights reserved.
    Project: Eustaquio
    26/12/2021 19
*/
public class RaceCommand implements BaseCommand {
    @Override
    public void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event) {
        if (args.length == 0) {
            Race race = new Race(UUID.randomUUID());
            race.prepareRace(textChannel);
        }
    }

    @Override
    public String getName() {
        return "race";
    }
}
