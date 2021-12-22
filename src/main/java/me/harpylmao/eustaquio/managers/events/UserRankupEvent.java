package me.harpylmao.eustaquio.managers.events;

import me.harpylmao.eustaquio.managers.user.User;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.Event;
import org.jetbrains.annotations.NotNull;

public class UserRankupEvent extends Event {

    private final User user;

    public UserRankupEvent(@NotNull JDA api, long responseNumber, User user) {
        super(api, responseNumber);
        this.user = user;
    }

    public User getUser() { return this.user; }
}
