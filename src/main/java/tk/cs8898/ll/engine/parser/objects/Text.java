package tk.cs8898.ll.engine.parser.objects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Text implements IStory {
    private static final String VARIABLE_REGEX = "<<\\$([^>]+)>>";
    private static final Pattern variable_pattern = Pattern.compile(VARIABLE_REGEX);
    private String text;
    protected Integer next;
    private int id;
    public Text(String text) {
        this.next = null;
        this.text = text;
    }

    protected String parseText(IGame game){
        String tmp = this.text;
        Matcher matcher = variable_pattern.matcher(tmp);
        while(matcher.find()){
            tmp = tmp.replace(matcher.group(0), game.getVariable(matcher.group(1)));
            matcher.reset(tmp);
        }
        return tmp;
    }

    @Override
    public IStory execute(IGame game) {
        game.outputNormal(parseText(game));
        return game.getStory().findNode(next);
    }

    @Override
    public void setNext(int id) {
        this.next = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
