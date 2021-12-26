package me.harpylmao.eustaquio.managers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.managers.model.Model;
import net.dv8tion.jda.api.entities.Message;

import java.awt.*;
import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Eustaquio implements Model {

    private final String id;

    private boolean welcomeMessages;
    private boolean levelMessages;

    private String welcomeChannel;
    private String levelChannel;

    private String ticketMessageId;
    private String ticketChannelId;
    private String ticketCategoryId;
    private String ticketLogsChannelId;
    private List<String> ticketCloseMessageIds = new ArrayList<>();

    private String verifyMessageId;
    private String verifyGroupId;

    private String mutedRoleId;

    private String color;

    public Eustaquio(String id) {
        this.id = id;
    }

    @ConstructorProperties({
            "id",
            "welcomeChannel",
            "levelChannel",
            "welcomeMessages",
            "levelMessages",
            "verifyMessageId",
            "verifyGroupId",
            "color",
            "mutedRoleId",
            "ticketMessageId",
            "ticketChannelId",
            "ticketCategoryId",
            "ticketLogsChannelId",
            "ticketCloseMessageIds"
    })
    public Eustaquio(
            String id,
            String welcomeChannel,
            String levelChannel,
            boolean welcomeMessages,
            boolean levelMessages,
            String verifyMessageId,
            String verifyGroupId,
            String color,
            String mutedRoleId,
            String ticketMessageId,
            String ticketChannelId,
            String ticketCategoryId,
            String ticketLogsChannelId,
            List<String> ticketCloseMessageIds
    ) {
        this(id);
        this.welcomeChannel = welcomeChannel;
        this.levelChannel = levelChannel;
        this.welcomeMessages = welcomeMessages;
        this.levelMessages = levelMessages;
        this.verifyMessageId = verifyMessageId;
        this.verifyGroupId = verifyGroupId;
        this.color = color;
        this.mutedRoleId = mutedRoleId;
        this.ticketMessageId = ticketMessageId;
        this.ticketChannelId = ticketChannelId;
        this.ticketCategoryId = ticketCategoryId;
        this.ticketLogsChannelId = ticketLogsChannelId;
        this.ticketCloseMessageIds = ticketCloseMessageIds;
    }

    @JsonIgnore
    public Color getColorColored() {
        return Bot.getInstance().getColor(this.color, null);
    }

    @Override
    public String getId() { return id; }
}
