package me.harpylmao.eustaquio.listeners;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.commands.command.interfaces.BaseCommand;
import me.harpylmao.eustaquio.commands.command.objects.Category;
import me.harpylmao.eustaquio.managers.Eustaquio;
import me.harpylmao.eustaquio.managers.EustaquioManager;
import me.harpylmao.eustaquio.managers.repository.ObjectRepository;
import me.harpylmao.eustaquio.managers.user.User;
import me.harpylmao.eustaquio.managers.user.UserManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatListeners extends ListenerAdapter {

  private final UserManager userManager;
  private final ObjectRepository<User> userObjectRepository;
  private final EustaquioManager eustaquioManager;
  private final ObjectRepository<Eustaquio> eustaquioObjectRepository;

  public ChatListeners() {
    this.userManager = Bot.getInstance().getUserManager();
    this.userObjectRepository = userManager.getUserObjectRepository();
    this.eustaquioManager = Bot.getInstance().getEustaquioManager();
    this.eustaquioObjectRepository = eustaquioManager.getEustaquioObjectRepository();
  }

  @Override
  public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
    User user = this.userObjectRepository.find(event.getMember().getId());
    Eustaquio eustaquio = this.eustaquioObjectRepository.find(Bot.getInstance().getEustaquioId());
    Member member = event.getMember();

    if (!member.getUser().isBot()) {
      if (event.getMessage().getTextChannel().getName().startsWith("ticket-")) {
        String[] args = event.getMessage().getTextChannel().getName().split("(?<=-)");
        if (args[1].replace("-", "").equalsIgnoreCase(member.getUser().getName()) && args[2].equalsIgnoreCase(member.getUser().getDiscriminator())) {
          File log = new File(member.getUser().getName() + member.getUser().getDiscriminator() + ".txt");
          try {
            if (log.exists() == false) {
              System.out.println("We had to make a new file.");
              log.createNewFile();
            }
            PrintWriter out = new PrintWriter(new FileWriter(log, true));
            out.append(member.getUser().getName() + ": " + event.getMessage().getContentRaw() + "\n");
            out.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

      int numberOfLetters = (int) event.getMessage().getContentRaw().chars().filter(ch -> ch != ' ').count();

      if (numberOfLetters <= 40) {
        user.setExperience(user.getExperience() + 1);
      } else if (numberOfLetters <= 50) {
        user.setExperience(user.getExperience() + 2);
      } else if (numberOfLetters <= 60) {
        user.setExperience(user.getExperience() + 3);
      } else if (numberOfLetters <= 70) {
        user.setExperience(user.getExperience() + 4);
      } else if (numberOfLetters <= 80) {
        user.setExperience(user.getExperience() + 5);
      } else if (numberOfLetters <= 90) {
        user.setExperience(user.getExperience() + 6);
      } else if (numberOfLetters <= 140) {
        user.setExperience(user.getExperience() + 9);
      } else if (numberOfLetters <= 200) {
        user.setExperience(user.getExperience() + 15);
      } else if (numberOfLetters <= 750) {
        user.setExperience(user.getExperience() + 25);
      } else if (numberOfLetters <= 1500) {
        user.setExperience(user.getExperience() + 45);
      } else if (numberOfLetters <= 4007) {
        user.setExperience(user.getExperience() + 50);
      }

      if (user.getExperience() >= 100) {
        user.setExperience(0);
        user.setLevel(user.getLevel() + 1);
        user.emitRankUpMessage(event.getJDA(), user, eustaquio, member.getUser());
      }

      userObjectRepository.save(user);

    }
  }

  @Override
  public void onMessageReactionAdd(MessageReactionAddEvent event) {
    Eustaquio eustaquio = this.eustaquioObjectRepository.find(Bot.getInstance().getEustaquioId());
    if (!event.getMember().getUser().isBot()) {
      if (event.getMessageId().equalsIgnoreCase(eustaquio.getVerifyMessageId()) && event.getGuild().getRoleById(eustaquio.getVerifyGroupId()) != null) {
        if (event.getReactionEmote().getEmoji().equals("✅")) {
          Member member = event.getMember();

          Role role = event.getGuild().getRoleById(eustaquio.getVerifyGroupId());

          AuditableRestAction<Void> action = event
                  .getGuild()
                  .addRoleToMember(member, role);

          action.queue();
        }
      } else if (event.getMessageId().equalsIgnoreCase(eustaquio.getTicketMessageId())) {
        if (event.getGuild().getCategories().stream().filter(category -> category.getName().equalsIgnoreCase("tickets")).findFirst().orElse(null) == null) {
          event.getGuild().createCategory("tickets").queue(category -> {
            eustaquio.setTicketCategoryId(category.getId());
            Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().save(eustaquio);
          });
        }

        Runnable categoryRunnable = () -> {
          event.getGuild().getCategoryById(eustaquio.getTicketCategoryId())
                  .createTextChannel("ticket-" + event.getMember().getUser().getName() + "-" + event.getMember().getUser().getDiscriminator()).queue(textChannel -> {
                    for (Member member : event.getGuild().getMembers()) {
                      User user = this.userObjectRepository.find(member.getId());
                      assert user != null;
                      if (user.isStaff() || member.getPermissions().contains(Permission.ADMINISTRATOR) || member.getUser().isBot()) {
                        textChannel.createPermissionOverride(member)
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
                      } else {
                        textChannel.createPermissionOverride(member)
                                .setDeny(
                                        Permission.VIEW_CHANNEL,
                                        Permission.MESSAGE_WRITE,
                                        Permission.MESSAGE_READ,
                                        Permission.MESSAGE_HISTORY,
                                        Permission.MESSAGE_EMBED_LINKS,
                                        Permission.MESSAGE_ATTACH_FILES,
                                        Permission.MESSAGE_ADD_REACTION,
                                        Permission.MESSAGE_EXT_EMOJI
                                ).queue();
                      }
                    }

                    new File(event.getMember().getUser().getName() + event.getUser().getDiscriminator() + ".txt");

                    textChannel.createPermissionOverride(event.getMember())
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

                    textChannel.sendMessageEmbeds(new EmbedBuilder()
                            .setColor(eustaquio.getColorColored())
                            .setTitle("Ticket Settings ⚙")
                            .setDescription(
                                    "If you want close\n" +
                                            " the ticket react to: ❌")
                            .build()
                    ).queue(message -> {
                      message.addReaction("❌").queue();
                      String idPattern = message.getId() + "|" + textChannel.getId();
                      eustaquio.getTicketCloseMessageIds().add(idPattern);
                      Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().save(eustaquio);
                    });
                  });
        };

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(categoryRunnable, 1, TimeUnit.SECONDS);
        executor.shutdown();
      }

      for (String id : eustaquio.getTicketCloseMessageIds()) {
        String[] args = id.split("\\|");
        assert args != null;
        if (event.getMessageId().equalsIgnoreCase(args[0])) {
          TextChannel textChannel = event.getGuild().getTextChannelById(args[1]);
          assert textChannel != null;

          File file = new File(event.getUser().getName() + event.getUser().getDiscriminator() + ".txt");
          if (file.exists()) {
            TextChannel logsChannel = event.getGuild().getTextChannelById(eustaquio.getTicketLogsChannelId());
            RestAction<Message> messageRestAction = logsChannel
                    .sendFile(
                            file,
                            event.getMember().getUser().getName() + ".txt"
                    );
            messageRestAction.queue();
            Runnable deleteFile = () -> {
              file.delete();
            };

            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            service.schedule(deleteFile, 5, TimeUnit.SECONDS);
            service.shutdown();
          }

          EmbedBuilder embedBuilder = new EmbedBuilder().setColor(eustaquio.getColorColored()).setDescription("Closing ticket in 5 seconds...");

          textChannel.sendMessageEmbeds(embedBuilder.build()).queue(message -> {

            AtomicInteger task = new AtomicInteger(5);

            Runnable helloRunnable = new Runnable() {
              @Override
              public void run() {
                task.getAndDecrement();
                if (task.get() == 0) task.set(0);
                switch (task.get()) {
                  case 5:
                    embedBuilder.setDescription("Closing ticket in 5 seconds...").build();
                    message.editMessageEmbeds(embedBuilder.build()).queue();
                    break;
                  case 4:
                    embedBuilder.setDescription("Closing ticket in 4 seconds...").build();
                    message.editMessageEmbeds(embedBuilder.build()).queue();
                    break;
                  case 3:
                    embedBuilder.setDescription("Closing ticket in 3 seconds...").build();
                    message.editMessageEmbeds(embedBuilder.build()).queue();
                    break;
                  case 2:
                    embedBuilder.setDescription("Closing ticket in 2 seconds...").build();
                    message.editMessageEmbeds(embedBuilder.build()).queue();
                    break;
                  case 1:
                    embedBuilder.setDescription("Closing ticket in 1 seconds...").build();
                    message.editMessageEmbeds(embedBuilder.build()).queue();
                    break;
                  case 0:
                    embedBuilder.setDescription("Closing ticket in 0 seconds...").build();
                    message.editMessageEmbeds(embedBuilder.build()).queue();
                    break;
                  default:
                    break;
                }
              }
            };

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleWithFixedDelay(helloRunnable, 0, 1, TimeUnit.SECONDS);

            Runnable shutdownRunnable = () -> {
              executor.shutdown();
            };

            ScheduledExecutorService shutdownService = Executors.newSingleThreadScheduledExecutor();
            executor.schedule(shutdownRunnable, 5, TimeUnit.SECONDS);
            shutdownService.shutdown();

          });

          Runnable runnable = () -> {
            textChannel.delete().queue(deletingChannel -> {
              eustaquio.getTicketCloseMessageIds().remove(eustaquio.getTicketCloseMessageIds().stream().filter(deletingId -> deletingId.equalsIgnoreCase(id)).findFirst().orElse(null));
              Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().save(eustaquio);
              TextChannel reactionChannel = event.getGuild().getTextChannelById(eustaquio.getTicketChannelId());
              reactionChannel.retrieveMessageById(eustaquio.getTicketMessageId()).queue(message -> {
                if (message == null) return;
                RestAction<Void> action = reactionChannel
                        .removeReactionById(
                                eustaquio.getTicketMessageId(),
                                "📤",
                                event.getUser()
                        );

                action.queue();
              });
            });
          };

          ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
          executor.schedule(runnable, 5, TimeUnit.SECONDS);
          executor.shutdown();

        }
      }
    }
  }

  @Override
  public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
    Eustaquio eustaquio = this.eustaquioObjectRepository.find(Bot.getInstance().getEustaquioId());
    if (event.getMessageId().equals(eustaquio.getVerifyMessageId()) && event.getGuild().getRoleById(eustaquio.getVerifyGroupId()) != null) {
      if (event.getReactionEmote().getEmoji().equals("✅")) {
        Member member = event.getMember();

        Role role = event.getGuild().getRoleById(eustaquio.getVerifyGroupId());

        AuditableRestAction<Void> action = event
                .getGuild()
                .removeRoleFromMember(member, role);

        action.queue();
      }
    }
  }
}
