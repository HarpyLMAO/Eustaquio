package me.harpylmao.eustaquio.commands.admin;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.managers.Eustaquio;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.math.NumberUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AdminCommand implements BaseCommand {

    @Override
    public void execute(CommandEvent command, TextChannel textChannel, Member member, String[] args, GuildMessageReceivedEvent event) {
        Eustaquio eustaquio = Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId());
        if (args.length <= 0) {
            command.reply("**" + this.usage() + "**");
            return;
        } else {
            switch (args[0]) {
                case "setcolor":
                    if (args.length == 2) {
                        Color color = Bot.getInstance().getColor(args[1], null);
                        if (color != null) {
                            eustaquio.setColor(args[1]);
                            Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().save(eustaquio);
                            command.reply("Successfully operation, color changed.");
                        } else {
                            command.reply("**ERROR:** Please provide a valid color.");
                        }
                    }
                    break;
                case "color":
                    command.reply("Current color is: " + eustaquio.getColor());
                    break;
                case "setuptickets":
                    EmbedBuilder ticketsEmbed = new EmbedBuilder()
                            .setColor(eustaquio.getColorColored())
                            .setTitle("**Support Ticket 📩**")
                            .setDescription(
                                    "Para poder abrir un ticket\n" +
                                    "necesitas reaccionar a este mensaje\n" +
                                    "con el emoji. (📤)"
                            )
                            .setThumbnail("https://www.freepnglogos.com/uploads/letter-png/letter-png-transparent-letter-images-pluspng-17.png");
                    textChannel.sendMessage(ticketsEmbed.build()).queue(message -> {
                        eustaquio.setTicketMessageId(message.getId());
                        eustaquio.setTicketChannelId(message.getTextChannel().getId());
                        Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().save(eustaquio);
                        message.addReaction("📤").queue();
                    });
                    break;
                case "deletetickets":
                    try {
                        command.getGuild().getTextChannelById(eustaquio.getTicketChannelId()).retrieveMessageById(eustaquio.getTicketMessageId()).queue(message -> message.delete().queue());
                        eustaquio.setTicketMessageId(null);
                        eustaquio.setTicketChannelId(null);

                        command.getGuild().getCategoryById(eustaquio.getTicketCategoryId()).delete().queue();
                        eustaquio.setTicketCategoryId(null);

                        eustaquio.getTicketCloseMessageIds().removeAll(eustaquio.getTicketCloseMessageIds());

                        Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().save(eustaquio);

                        command.reply("Operation successfully, ticket system removed.");
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        command.reply("**ERROR:** An error ocurred.");
                        return;
                    }
                    break;
                case "setupverify":
                    if (args.length == 4) {
                        String rankName = args[1];
                        Color rankColor = Bot.getInstance().getColor(args[2], null);
                        boolean mentionable = Boolean.parseBoolean(args[3]);

                        if (rankColor != null || rankName != null || isBoolean(args[3])) {
                            command.getMessage().delete().queue();
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setColor(eustaquio.getColorColored());
                            embedBuilder.setTitle("**User verification**");
                            embedBuilder.setDescription("Para poder completar el paso\n" +
                                    "necesitas reaccionar a este mensaje\n" +
                                    "con el emoji. (✅)\n" +
                                    "\n" +
                                    "Con este paso podrás acceder al \n" +
                                    "servidor sin limitaciones y con tu propio rango.");
                            embedBuilder.setImage("https://c.tenor.com/dS4HmzuhKkoAAAAd/verify-aesthetic.gif");
                            textChannel.sendMessage(embedBuilder.build()).queue(message -> {
                                message.addReaction("✅").queue();
                                eustaquio.setVerifyMessageId(message.getId());
                                Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().save(eustaquio);
                            });

                            command.getGuild().createRole()
                                    .setName(rankName)
                                    .setColor(rankColor)
                                    .setMentionable(mentionable)
                                    .queue(role -> {
                                        eustaquio.setVerifyGroupId(role.getId());
                                    });
                        } else {
                            command.reply("**ERROR In Usage:** /admin setupverify [rank name] [rank color] [mentionable (true/false)]");
                            return;
                        }
                    } else {
                        command.reply("**ERROR In Usage:** /admin setupverify [rank name] [rank color] [mentionable (true/false)]");
                        return;
                    }
                    break;
                case "deleteverify":
                    command.getMessage().delete().queue();
                    textChannel.retrieveMessageById(eustaquio.getVerifyMessageId()).queue(message -> {
                        if (message != null) {
                            for (MessageReaction reactionEmote : message.getReactions()) {
                                reactionEmote.removeReaction();
                            }
                            message.delete().queue();
                            command.getGuild().getRoleById(eustaquio.getVerifyGroupId()).delete().queue();

                            eustaquio.setVerifyGroupId("");
                            eustaquio.setVerifyMessageId("");

                            Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().save(eustaquio);
                        }
                    });
                    break;
                case "clearmessages":
                    if (args.length < 2) {
                        EmbedBuilder usage = new EmbedBuilder();
                        usage.setColor(eustaquio.getColorColored());
                        usage.setTitle("Specify amount to delete");
                        usage.setDescription("Usage: /admin clearmessages [amount of messages]");
                        command.getChannel().sendMessage(usage.build()).queue();
                    } else {
                        if (NumberUtils.isParsable(args[1])) {
                            if (Integer.parseInt(args[1]) > 100 || Integer.parseInt(args[1]) < 2) {
                                command.reply("**ERROR:** Limit is between 2 and 100 messages. No more, no less.");
                                return;
                            }
                            List<Message> messagesAmount = command.getChannel().getHistory().retrievePast(Integer.parseInt(args[1])).complete();
                            command.getChannel().deleteMessages(messagesAmount).queue();
                            command.getChannel().sendMessage("Successfully operation, messages removed.").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
                        } else {
                            command.reply("**ERROR:** the amount is not valid, try another time writing a number.");
                        }
                    }
                    break;
                default:
                    command.reply("**" + this.usage() + "**");
                    break;
            }
        }
    }

    @Override
    public String category() {
        return "administrators";
    }

    private boolean isBoolean(String value) {
        return value != null && Arrays.stream(new String[]{"true", "false", "1", "0"})
                .anyMatch(b -> b.equalsIgnoreCase(value));
    }

    @Override
    public String usage() {
        return "USAGE: \n" +
                " - /admin setuptickets\n" +
                " - /admin setupverify [rank name] [rank color] [mentionable (true/false)]\n" +
                " - /admin deleteverify\n" +
                " - /admin clearmessages [amount]";
    }

    @Override
    public String getName() {
        return "admin";
    }
}
