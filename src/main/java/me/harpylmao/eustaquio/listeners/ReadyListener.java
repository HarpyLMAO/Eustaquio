package me.harpylmao.eustaquio.listeners;

import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.managers.Eustaquio;
import me.harpylmao.eustaquio.managers.EustaquioManager;
import me.harpylmao.eustaquio.managers.repository.ObjectRepository;
import me.harpylmao.eustaquio.managers.user.User;
import me.harpylmao.eustaquio.managers.user.UserManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter {

    private final UserManager userManager;
    private final ObjectRepository<User> userObjectRepository;
    private final EustaquioManager eustaquioManager;
    private final ObjectRepository<Eustaquio> eustaquioObjectRepository;

    public ReadyListener() {
        this.userManager = Bot.getInstance().getUserManager();
        this.userObjectRepository = userManager.getUserObjectRepository();
        this.eustaquioManager = Bot.getInstance().getEustaquioManager();
        this.eustaquioObjectRepository = eustaquioManager.getEustaquioObjectRepository();
    }

    @Override
    public void onReady(ReadyEvent event) {
        String id = "6yY=@u3!x^j%e5&g!$=d+s%yvrwf%QB*z";
        Eustaquio eustaquio = this.eustaquioObjectRepository.find(id);

        event.getJDA().getUsers().forEach(users -> {
            event.getJDA().retrieveUserById(users.getId()).queue(discordUser -> {
                User user = this.userObjectRepository.find(discordUser.getId());

                if (user == null) {
                    user = new User(discordUser.getId(), discordUser.getName());
                    this.userObjectRepository.save(user);
                    System.out.printf("user created: " + user.getName());
                }
            });
        });

        if (eustaquio == null) {
            eustaquio = new Eustaquio(id);
            eustaquioObjectRepository.save(eustaquio);
        }
    }
}
