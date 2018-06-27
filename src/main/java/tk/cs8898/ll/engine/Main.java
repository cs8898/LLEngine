package tk.cs8898.ll.engine;

import tk.cs8898.ll.engine.parser.Parser;
import tk.cs8898.ll.engine.parser.objects.IGame;
import tk.cs8898.ll.engine.parser.objects.Story;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args){
        Path file = null;
        try {
            file = Paths.get(Main.class.getResource("/assets/StoryData_de.txt").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Parser parser = new Parser(file);
        parser.parse();

        Story story = parser.getStory();

        IGame game = new ConsoleGame(story);
        game.play();
    }
}
