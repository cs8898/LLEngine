package tk.cs8898.ll.engine.parser.objects;

public interface IGame {
    Story getStory();

    void setVariable(String variable, String eval);

    String getVariable(String group);

    void outputNormal(String text);

    void outputSpecial(String s);

    String outputChoice(Choice choice);

    void delay(long delay);

    void play();
}
