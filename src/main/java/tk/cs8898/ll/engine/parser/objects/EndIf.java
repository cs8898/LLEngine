package tk.cs8898.ll.engine.parser.objects;

public class EndIf implements IStory{

    @Override
    public IStory execute(IGame game) {
        return null;
    }

    @Override
    public void setNext(int next) {

    }

    @Override
    public int getId() {
        return Integer.MIN_VALUE;
    }

    @Override
    public void setId(int id) {
    }
}
