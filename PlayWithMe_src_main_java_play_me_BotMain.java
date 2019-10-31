package play.me;

//JDA Library Imports
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

//Exceptions
import javax.security.auth.login.LoginException;

public class BotMain {
    public static JDA jda;
    public static MessingWithMe m = new MessingWithMe();

    public static void main(String[] args) throws LoginException {

        jda = (new JDABuilder(AccountType.BOT)).setToken("Blanks for security purposes").build();
        jda.getPresence().setGame(Game.playing("with everyone :)"));
        jda.addEventListener(m);
    }
}
