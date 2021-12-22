package me.harpylmao.eustaquio.listeners;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.managers.Eustaquio;
import me.harpylmao.eustaquio.managers.repository.ObjectRepository;
import me.harpylmao.eustaquio.managers.user.User;
import me.harpylmao.eustaquio.managers.user.UserManager;
import me.harpylmao.eustaquio.utils.Configuration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Date;

public class MemberJoinListener extends ListenerAdapter {

    private final UserManager userManager;
    private final ObjectRepository<User> userObjectRepository;

    public MemberJoinListener() {
        this.userManager = Bot.getInstance().getUserManager();
        this.userObjectRepository = userManager.getUserObjectRepository();
    }

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        Eustaquio eustaquio = Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId());
        String id = event.getMember().getUser().getId();
        User user = this.userObjectRepository.find(id);

        if (user == null) {
            user = new User(id, event.getMember().getUser().getName());
            userObjectRepository.save(user);
        }

        this.userManager.getUsers().put(id, user);

        if (eustaquio.getWelcomeChannel() != null && eustaquio.isWelcomeMessages()) {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            String mention = event.getUser().getName() + "#" + event.getUser().getDiscriminator();

            embedBuilder.setColor(eustaquio.getColorColored());
            embedBuilder.setAuthor(mention, null, event.getMember().getUser().getEffectiveAvatarUrl());
            embedBuilder.setDescription("✦₊ ︵︵ഒ ˚₊\n" +
                    "\n" +
                    "bienvenido a prueba de maricones! \n" +
                    "⊱ ━━━━.⋅ εïз ⋅.━━━━ ⊰\n" +
                    " mira estos canales!\n" +
                    "⚜︎  : ❀ #lol\n" +
                    "⚜︎  : ❀ #lil\n" +
                    "⚜︎  : ❀ #lal\n");
            embedBuilder.setThumbnail("https://c.tenor.com/nz2nnGCEYH8AAAAj/micacalala-michi.gif");
            embedBuilder.setImage("https://images-ext-1.discordapp.net/external/5Fj3BcsqGou6oCvI3323XF0nqvO704yMPcXPArm1MPw/https/media.discordapp.net/attachments/818946669319290890/834251907781492776/line_divider_2.jpg");
            embedBuilder.setFooter("\u200E" + Configuration.TIMESTAMP.format(new Date()));
            event.getGuild().getTextChannelById(eustaquio.getWelcomeChannel()).sendMessage(embedBuilder.build()).queue();
        }
    }

    /*@Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Eustaquio eustaquio = Bot.getInstance().getEustaquioManager().getEustaquioObjectRepository().find(Bot.getInstance().getEustaquioId());

        if (eustaquio.getWelcomeChannel() != null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setDescription(
                    "**Bienvenido a " + event.getGuild().getName() + " <@" + event.getAuthor().getId() + "> **\n" +
                            "" +
                            "Si necesitas ayuda con cualquier cosa puedes crear un ticket en #asdf" +
                            "");
            embedBuilder.setImage("https://pm1.narvii.com/6354/ddb4ee8f0a5cd819812fa83274a9234eb9224b29_hq.jpg");
            event.getJDA().getTextChannelById(eustaquio.getWelcomeChannel()).sendMessage(embedBuilder.build()).queue();
        }
    }*/
}
