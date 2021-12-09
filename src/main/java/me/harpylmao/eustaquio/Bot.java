package me.harpylmao.eustaquio;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Bot {

    private Bot() throws LoginException {

        JDA jda = new JDABuilder()
                .setToken(Config.get("token"))
                .setActivity(Activity.playing("matar a agallas el perro cobarde"))
                .build();


        jda.addEventListener(new Listener());
    }

    public static void main(String[] args) throws LoginException {
        new Bot();
    }

}
