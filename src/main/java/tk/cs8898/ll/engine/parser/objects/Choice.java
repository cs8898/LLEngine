package tk.cs8898.ll.engine.parser.objects;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class Choice implements IStory {
    private ArrayList<Entry<String, String>> choices;//MSG JUMP
    private int id;

    public Choice(){
        choices = new ArrayList<>();
    }

    public void addOption(String msg, String jump){
        choices.add(new AbstractMap.SimpleEntry<>(msg,jump));
    }
    public List<Entry<String,String>> getOptions(){
        return choices;
    }

    @Override
    public IStory execute(IGame game) {
        //TODO
        String result = game.outputChoice(this);
        return game.getStory().findMark(result);
    }

    @Override
    public void setNext(int obj) {
        //Noting to do
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getText(String jump){
        for(Entry<String, String> entry: choices){
            if(entry.getValue().equals(jump))
                return entry.getKey();
        }
        return null;
    }

    public String getJump(String text){
        for(Entry<String, String> entry: choices){
            if(entry.getKey().equals(text))
                return entry.getValue();
        }
        return null;
    }
}
