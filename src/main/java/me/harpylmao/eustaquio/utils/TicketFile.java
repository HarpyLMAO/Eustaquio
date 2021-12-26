package me.harpylmao.eustaquio.utils;

import lombok.Getter;
import lombok.Setter;
import me.harpylmao.eustaquio.Bot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TicketFile {

  //private static List<TicketFile> ticketFiles = new ArrayList<>();

  private Bot bot;
  private JDA jda;
  private Guild guild;

  private OutputStreamWriter osw;

  public TicketFile(Bot bot, Guild guild, JDA jda, String ticketUsername) throws IOException {
    this.bot = bot;
    this.guild = guild;
    this.jda = jda;
    //ticketFiles.add(this);
    File fout = new File(ticketUsername + ".txt");
    FileOutputStream fos = new FileOutputStream(fout);
    osw = new OutputStreamWriter(fos);
  }

  public void write(String message) throws IOException {
    osw.write("test");
    osw.write(message + "\n");
  }

  public void close() throws IOException {
    osw.close();
  }

  /*public static TicketFile getTicketFile(String ticketId) {
    return ticketFiles.stream().filter(ticketFile -> ticketFile.getTicketId().equalsIgnoreCase(ticketId)).findFirst().orElse(null);
  }*/

}
