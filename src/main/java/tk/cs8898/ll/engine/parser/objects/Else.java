package tk.cs8898.ll.engine.parser.objects;

public class Else implements IStory, IBlock{
    private final Story story;
    //IStory next;
    private Integer head;
    private Integer tail;
    private int id;

    public Else(Story story){
        head = null;
        tail = null;

        this.story = story;
    }

    public void add(IStory obj){
        if(obj.getId()>0){
            System.err.println("Else Tried to add a duplicate!!!");
        }
        story.add(obj,false);
        if(head == null)
            setHead(obj.getId());
        if(tail != null)
            getTail().setNext(obj.getId());
        setTail(obj.getId());
    }

    private void setTail(int tail) {
        this.tail = tail;
    }

    @Override
    public IStory getTail() {
        if (this.tail != null)
            return this.story.findNode(this.tail);
        return null;
    }

    public IStory getHead() {
        if (this.head != null)
            return this.story.findNode(this.head);
        return null;
    }

    private void setHead(int head) {
        this.head = head;
    }

    @Override
    public IStory execute(IGame game) {
        return game.getStory().findNode(head);
    }

    @Override
    public void setNext(int id) {
        /*this.next = obj;*/ //NOT NEEDED?!
        getTail().setNext(id);
        /*this.next = obj;*/
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
