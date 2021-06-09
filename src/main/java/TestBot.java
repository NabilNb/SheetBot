import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.server.Server;

public class TestBot {
    public static void main(String[] args) {
        String token = "ODUxNTUyOTkzNjg5NjAwMDMw.YL58hg.55H11_zdgkV96YZpjrYt8VWVm24";

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        // Add a listener which answers with "Pong!" if someone writes "!ping"
        api.addMessageCreateListener(event -> {
            /*
            if (event.getMessageContent().equalsIgnoreCase("Shama")) {
                event.getChannel().sendMessage("Ekta Cutiepie");
            }
            if (event.getMessageContent().equalsIgnoreCase("Nabil")) {
                event.getChannel().sendMessage("Loves Shama a lot");
            }

             */


        });


        // Print the invite url of your bot
        //System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());
    }
}



