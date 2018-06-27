package tk.cs8898.ll.engine.parser.objects;

public interface IBlock {
    void add(IStory obj);
    IStory getTail();
}
