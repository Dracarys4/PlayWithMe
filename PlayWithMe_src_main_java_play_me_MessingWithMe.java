package play.me;

// JDA library Imports
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

// General Java Imports
import java.util.ArrayList;

//Exceptions
import java.io.IOException;
import java.security.GeneralSecurityException;

public class MessingWithMe extends ListenerAdapter{

    private int flag;
    private GuildMessageReceivedEvent event;

    public MessingWithMe() {
        flag = 0;
    }

    private ArrayList<String> splits(String msg){
        ArrayList<String> message = new ArrayList<>();
        int counter = 0;
        for(int i = 0; i < msg.length(); i++) {
            if(msg.charAt(i) == ' ') {
                message.add(msg.substring(counter, i));
                counter = i + 1;
            }
            else if(i == msg.length() - 1) {
                message.add(msg.substring(counter));
            }
        }
        return message;
    }

    private String[] makesArray(ArrayList<String> arr) {
        String[] str = new String[arr.size()];
        for(int i = 0; i < str.length; i++) {
            str[i] = arr.get(i);
        }
        return str;
    }

    public int returnFlag() {
        return flag;
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent message){
        this.event = message;
        String msg = event.getMessage().getContentRaw();
        ArrayList<String> messg = splits(msg);
        String[] newMes = makesArray(messg);

        if(newMes[0].equalsIgnoreCase("!rtnVal")) {
            if(message.getAuthor().isBot()){
                return;
            }

            flag = 1;

            try {
                MainReRoute.main(newMes);
            }
            catch(IOException io){
                System.out.println("IO exception");
            }
            catch(GeneralSecurityException gen){
                System.out.println("GSE exception");
            }
        }

        if(newMes[0].equalsIgnoreCase("!chngVal")){
            if(message.getAuthor().isBot()){
                return;
            }

            try {
                MainReRoute.main(newMes);
            }
            catch(IOException io){
                io.printStackTrace();
            }
            catch(GeneralSecurityException gen){
                gen.printStackTrace();
            }
        }
    }

    public void sendValues(String param) {
        this.event.getChannel().sendMessage(param).queue();
        flag = 0;

    }

    public void nothing() {
        this.event.getChannel().sendMessage("No values to return, sorry :').");
        flag = 0;
    }
}
