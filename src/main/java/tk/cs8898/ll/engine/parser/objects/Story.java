package tk.cs8898.ll.engine.parser.objects;

import java.util.ArrayList;
import java.util.HashMap;

public class Story implements IStory, IBlock {
    private Integer head, tail;
    private HashMap<String, Integer> marks;
    private ArrayList<IStory> nodes;

    public Story() {
        head = null;
        tail = null;
        marks = new HashMap<>();
        nodes = new ArrayList<>();
    }

    public IStory execute(IGame game) {
        return findNode(head);
    }

    @Override
    public void setNext(int next) {
        this.head = next;
    }

    @Override
    public int getId() {
        return -1;
    }

    @Override
    public void setId(int id) {
        //ignore
    }

    public void add(IStory obj) {
        obj.setId(nodes.size());
        if (obj instanceof Mark) {
            marks.put(((Mark) obj).getName(), obj.getId());
            //System.out.println ("Added Mark: "+((Mark) obj).getName());
        }
        if (getHead() == null)
            setHead(obj);
        if (getTail() != null)
            getTail().setNext(obj.getId());
        setTail(obj);
        nodes.add(obj);
    }

    public void add(IStory obj, boolean updaterefs) {
        obj.setId(nodes.size());
        if (obj instanceof Mark) {
            marks.put(((Mark) obj).getName(), obj.getId());
            //System.out.println ("Added Mark: "+((Mark) obj).getName());
        }
        if (updaterefs) {
            if (getHead() == null)
                setHead(obj);
            if (getTail() != null)
                getTail().setNext(obj.getId());
            setTail(obj);
        }
        nodes.add(obj);
    }

    public IStory getTail() {
        return findNode(tail);
    }

    private void setTail(IStory tail) {
        this.tail = tail.getId();
    }

    private void setHead(IStory head) {
        this.head = head.getId();
    }

    public IStory getHead() {
        return findNode(head);
    }

    public IStory findMark(String destination) {
        Integer id = marks.get(destination);
        if (id != null)
            return nodes.get(id);
        return null;
    }

    public IStory findNode(Integer id) {
        if (id==null || id < 0 || id >= nodes.size())
            return null;
        return nodes.get(id);
    }
}
