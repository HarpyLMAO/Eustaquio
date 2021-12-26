package me.harpylmao.eustaquio;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import lombok.Getter;
import me.harpylmao.eustaquio.audio.MusicManager;
import me.harpylmao.eustaquio.listeners.*;
import me.harpylmao.eustaquio.managers.EustaquioManager;
import me.harpylmao.eustaquio.managers.car.CarManager;
import me.harpylmao.eustaquio.managers.mongo.MongoConnector;
import me.harpylmao.eustaquio.managers.user.UserManager;
import me.harpylmao.eustaquio.commands.command.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.awt.*;

@Getter
public class Bot {

  @Getter
  private static Bot instance;

  private JDA jda;

  private String eustaquioId = "6yY=@u3!x^j%e5&g!$=d+s%yvrwf%QB*z";

  private MongoConnector mongoConnector;

  private MusicManager guildAudioManager;
  private EustaquioManager eustaquioManager;
  private UserManager userManager;
  private CarManager carManager;

  private EventWaiter eventWaiter;

  private Bot() throws Exception {
    instance = this;

    this.mongoConnector = new MongoConnector(this);
    this.mongoConnector.load();

    this.eustaquioManager = new EustaquioManager(mongoConnector.getMongoDatabase());
    this.userManager = new UserManager(mongoConnector.getMongoDatabase());
    this.carManager = new CarManager(mongoConnector.getMongoDatabase());

    this.guildAudioManager = new MusicManager(this);


    JDABuilder jdaBuilder = JDABuilder.createDefault("OTE3ODYzODEwOTMxNTExMzM3.Ya-5SQ.Abiog6-O3Lbbb3MzPeHZZ4_FQmQ")
            .setActivity(Activity.playing("matar a agallas el perro cobarde"))
            .setChunkingFilter(ChunkingFilter.ALL)
            .enableCache(CacheFlag.VOICE_STATE)
            .setRawEventsEnabled(true)
            .enableIntents(
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_VOICE_STATES,
                    GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                    GatewayIntent.DIRECT_MESSAGE_TYPING,
                    GatewayIntent.DIRECT_MESSAGES,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_WEBHOOKS,
                    GatewayIntent.GUILD_MESSAGE_REACTIONS
            )
            .setMemberCachePolicy(MemberCachePolicy.ALL);

    this.eventWaiter = new EventWaiter();

    jdaBuilder.addEventListeners(
            eventWaiter,
            new ReadyListener(),
            new MemberJoinListener(),
            new ChatListeners(),
            new MusicListeners(this),
            new SelectionMenuListener()
    );

    this.jda = jdaBuilder.build().awaitReady();

    CommandManager commandManager = new CommandManager(jda, "e!");
    commandManager.registerCommands();
    commandManager.init();
  }

  public static void main(String[] args) throws Exception {
    new Bot();
  }

  public Color getColor(String string, Color def) {
    Color out = null;
    if (string == null)
      return def;
    if ("blue".equals(string))
      return Color.BLUE;
    if ("cyan".equals(string))
      return Color.CYAN;
    if ("green".equals(string))
      return Color.GREEN;
    if ("magenta".equals(string))
      return Color.MAGENTA;
    if ("orange".equals(string))
      return Color.ORANGE;
    if ("pink".equals(string))
      return Color.PINK;
    if ("black".equals(string))
      return Color.BLACK;
    if ("white".equals(string))
      return Color.WHITE;
    if ("grey".equals(string))
      return Color.GRAY;
    if ("yellow".equals(string))
      return Color.YELLOW;
    if ("light_grey".equals(string))
      return Color.LIGHT_GRAY;
    if ("red".equals(string))
      return Color.RED;

    out = Color.getColor(string);
    if (out == null)
      out = def;
    return out;
  }

}
