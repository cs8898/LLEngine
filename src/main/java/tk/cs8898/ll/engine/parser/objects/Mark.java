package tk.cs8898.ll.engine.parser.objects;

public class Mark implements IStory {
    private String name;
    private Integer next;
    private int id;

    public Mark(String name) {
        this.next = null;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public IStory execute(IGame game) {
        return game.getStory().findNode(next);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public void setNext(int next) {
        this.next = next;
    }
}
