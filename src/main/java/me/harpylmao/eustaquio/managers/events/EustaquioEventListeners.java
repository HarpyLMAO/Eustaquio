package me.harpylmao.eustaquio.managers.events;

import net.dv8tion.jda.api.hooks.EventListener;

public interface EustaquioEventListeners extends EventListener {

    void UserRankUpEvent(UserRankupEvent event);
}
