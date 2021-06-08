package de.primeapi.primeplugins.spigotapi.messaging;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * @author Lukas S. PrimeAPI
 * created on 14.05.2021
 * crated for PrimePlugins
 */
@Getter @Setter
public class PluginMessage {

    String channel;
    String subChannel;
    String[] data;

    public static PluginMessage build(String channel, String subChannel, String... data){
        return new PluginMessage(channel, subChannel, data);
    }
    public static PluginMessage build(PluginCommand command, String... data){
        return new PluginMessage("prime:primemessaging", command.toString(), data);
    }

    private PluginMessage(String channel, String subChannel, String[] data) {
        this.channel = channel;
        this.subChannel = subChannel;
        this.data = data;
    }

    public void send(){
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF(subChannel);
            for (String in : data) {
                out.writeUTF(in);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrimeCore.getInstance().getServer().sendPluginMessage(PrimeCore.getInstance(), channel, b.toByteArray());
    }

    public enum PluginCommand {
        sudoPlayer, //playername, command
    }

}
