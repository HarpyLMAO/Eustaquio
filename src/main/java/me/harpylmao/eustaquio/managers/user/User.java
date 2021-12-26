package me.harpylmao.eustaquio.managers.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import me.harpylmao.eustaquio.managers.Eustaquio;
import me.harpylmao.eustaquio.managers.car.Car;
import me.harpylmao.eustaquio.managers.model.Model;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HarpyLMAO
 * at 09/10/2021 20:25
 * all credits reserved
 */

@Getter
@Setter
public class User implements Model {

  private final String id;
  private final String name;

  private boolean staff;

  private List<Car> cars = new ArrayList<>();

  private int level;
  private int experience;
  private int balance;

  public User(String id, String name) {
    this.id = id;
    this.name = name;
  }

  @ConstructorProperties({
          "id",
          "name",
          "staff",
          "cars",
          "level",
          "experience",
          "balance"
  })
  public User(
          String id,
          String name,
          boolean staff,
          List<Car> cars,
          int level,
          int experience,
          int balance
  ) {
    this(id, name);
    this.staff = staff;
    this.cars = cars;
    this.level = level;
    this.experience = experience;
    this.balance = balance;
  }

  @Override
  public String getId() { return id; }

  @JsonIgnore
  public void emitRankUpMessage(JDA jda, User user, Eustaquio eustaquio, net.dv8tion.jda.api.entities.User discordUser) {
    TextChannel channel = jda.getTextChannelById(eustaquio.getLevelChannel());
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(eustaquio.getColorColored());
    embedBuilder.setTitle("**A member was rank up**");
    embedBuilder.setDescription("Felicidades has subido de rango maricón.");
    embedBuilder.setThumbnail(discordUser.getEffectiveAvatarUrl());
    channel.sendMessage(discordUser.getAsMention()).queue();
    channel.sendMessageEmbeds(embedBuilder.build()).queue();
  }
}
