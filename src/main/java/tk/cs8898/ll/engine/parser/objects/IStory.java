package tk.cs8898.ll.engine.parser.objects;

public interface IStory {
    IStory execute(IGame game);
    void setNext(int next);
    int getId();
    void setId(int id);
}
