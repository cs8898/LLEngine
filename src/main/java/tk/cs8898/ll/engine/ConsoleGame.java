package tk.cs8898.ll.engine;

import tk.cs8898.ll.engine.parser.objects.Choice;
import tk.cs8898.ll.engine.parser.objects.IGame;
import tk.cs8898.ll.engine.parser.objects.IStory;
import tk.cs8898.ll.engine.parser.objects.Story;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class ConsoleGame implements IGame {
    private Story story;
    private HashMap<String, String> variables;
    private Scanner reader;
    private int currentID;

    public ConsoleGame(Story story){
        this.story = story;
        this.variables = new HashMap<>();
        this.reader = new Scanner(System.in);
    }

    public Story getStory(){
        return story;
    }

    @Override
    public void setVariable(String variable, String val) {
        this.variables.put(variable, val);
    }

    @Override
    public String getVariable(String variable) {
        return this.variables.get(variable);
    }

    @Override
    public void outputNormal(String text) {
        System.out.println("#"+this.currentID);
        System.out.println(text);
    }

    @Override
    public void outputSpecial(String text) {
        System.out.println("#"+this.currentID);
        System.out.print("        ");
        System.out.println(text);
    }

    @Override
    public void delay(long delay) {
        System.err.println("DELAY not implemented ("+delay+")");
    }

    @Override
    public String outputChoice(Choice choice) {
        int i = 0;
        System.out.println("#"+this.currentID);
        for (Entry<String, String> entry : choice.getOptions()) {
            System.out.printf("  [%d]    %s\n", i++, entry.getKey());
        }

        System.out.print("> ");
        int selection = reader.nextInt();
        return choice.getOptions().get(selection).getValue();
    }

    @Override
    public void play() {
        IStory obj = story;
        while(obj != null){
            this.currentID = obj.getId();
            obj = obj.execute(this);
        }
    }
}
