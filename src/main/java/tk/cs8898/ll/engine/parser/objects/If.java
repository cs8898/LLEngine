package tk.cs8898.ll.engine.parser.objects;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class If implements IBlock, IStory {
    private Integer next;


    private Integer head;
    private Integer tail;
    /*private IStory tailIf;
    private IStory tailElseIf;
    private IStory tailElse;*/

    private Integer anElse;
    private STATES state;

    private String variable;
    private String operation;
    private String equation;

    private String logic;

    private String variableB;
    private String operationB;
    private String equationB;

    private int id;
    private Story story;


    private static final String VARIABLE_REGEX = "\\$([^ ]+)";
    private static final Pattern variable_pattern = Pattern.compile(VARIABLE_REGEX);
    private final Matcher variable_matcher = variable_pattern.matcher("");


    public enum STATES {
        IF,
        ELSEIF,
        ELSE,
        ENDIF,
        FINISHED
    }

    public If(Story story, String variable, String operation, String equation) {
        next = null;
        anElse = null;
        head = null;
        tail = null;
        state = STATES.IF;
        this.variable = variable;
        this.operation = operation;
        this.equation = equation;
        this.logic = null;

        this.story = story;
        //System.out.printf("v: %s o: %s e: %s\n", variable, operation, equation);
    }

    public If(Story story, String variable, String operation, String equation, String logic, String variableB, String operationB, String equationB) {
        this(story, variable, operation, equation);
        this.logic = logic;
        this.variableB = variableB;
        this.operationB = operationB;
        this.equationB = equationB;
        /*System.out.printf(" v: %s  o: %s  e: %s\n", variable, operation, equation);
        System.out.printf("\tlogik: %s\n",logic);
        System.out.printf("vB: %s oB: %s eB: %s\n", variableB, operationB, equationB);
        */
    }

    @Override
    public IStory execute(IGame game) {
        //EVALUATE...
        if (evaluate(game)) {
            return game.getStory().findNode(head);
        } else if (hasElse()) {
            return game.getStory().findNode(anElse);
        }

        //System.err.println("No Else Blocks THERE");
        return game.getStory().findNode(next);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    protected boolean hasElse() {
        return this.anElse != null;
    }

    protected boolean evaluate(IGame game) {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");

        String tmp = "($" + variable + " " + normalizeOperation(operation) + " " + equation + ")";
        if (logic != null) {
            tmp += normalizeLogic(logic) + "($" + variableB + " " + normalizeOperation(operationB) + " " + equationB + ")";
        }

        variable_matcher.reset(tmp);
        while (variable_matcher.find()) {
            scriptEngine.put(variable_matcher.group(0), game.getVariable(variable_matcher.group(1)));
        }
        boolean ret = false;
        try {
            ret = (boolean) scriptEngine.eval(tmp);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private String normalizeOperation(String op) {
        switch (op) {
            case "is":
            case "eq":
                return "==";
            case "geq":
                return ">=";
            case "leq":
                return "<=";
            default:
                System.err.println("UNKNOWN operation: " + op);
                return "==";
        }
    }

    private String normalizeLogic(String log) {
        switch (log) {
            case "and":
                return "&&";
            case "or":
                return "||";
            default:
                System.err.println("UNKNOWN logic: " + log);
                return "==";
        }
    }

    @Override
    public void setNext(int id) {
        this.next = id;
        this.getTail().setNext(id);
        if (this.anElse != null) {
            Else anElse = (Else) story.findNode(this.anElse);
            if (anElse.getHead() instanceof If) {
                anElse.getHead().setNext(id);
            }
            anElse.getTail().setNext(id);
        }
        this.state = STATES.FINISHED;
    }

    public void add(IStory obj) {
        if (obj instanceof Mark) {
            System.err.println("Mark in IF block(" + this.getState() + "): " + ((Mark) obj).getName());
        }
        if (obj instanceof ElseIf) {
            this.state = STATES.ELSEIF;
            Else anElse = new Else(story);
            story.add(anElse, false);

            this.anElse = anElse.getId();
            anElse.add(obj);
            return;
        } else if (obj instanceof Else) {
            this.state = STATES.ELSE;
            story.add(obj, false);
            this.anElse = obj.getId();
            return;
        } else if (obj instanceof EndIf) {
            /*if(anElse!=null)
             this.tailElse = tail;*/
            this.state = STATES.ENDIF;
            return;
        }

        switch (this.state) {
            case IF:
                story.add(obj, false);
                if (getHead() == null)
                    this.head = obj.getId();
                if (getTail() != null) {
                    getTail().setNext(obj.getId());
                }
                this.tail = obj.getId();
                break;
            case ELSE:
                ((Else) this.story.findNode(this.anElse)).add(obj);
                break;
            case ENDIF://NOT USED?!
                story.add(obj, false);
                /*this.setNext(block);
                this.getTail().setNext(block);
                if(this.anElseIf!=null)
                    this.anElseIf.getTail().setNext(block);
                if(this.anElse != null)
                    this.anElse.getTail().setNext(block);*/
                this.state = STATES.FINISHED;
        }
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

    public STATES getState() {
        return state;
    }
}
