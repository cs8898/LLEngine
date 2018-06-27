package tk.cs8898.ll.engine.parser.objects;

public class Jump implements IStory {
    private String destination;
    private int id;

    public Jump(String destination) {
        this.destination = destination;
    }

    @Override
    public IStory execute(IGame game) {
        return game.getStory().findMark(this.destination);
    }

    @Override
    public void setNext(int obj) {

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
