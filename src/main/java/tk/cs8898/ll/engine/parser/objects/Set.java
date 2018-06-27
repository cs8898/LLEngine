package tk.cs8898.ll.engine.parser.objects;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Set implements IStory {
    private Integer next;

    private String variable;
    private String operation;
    private String value;

    private int id;

    private static final String VARIABLE_REGEX = "\\$([^ ]+)";
    private static final Pattern variable_pattern = Pattern.compile(VARIABLE_REGEX);
    private final Matcher variable_matcher = variable_pattern.matcher("");


    public Set(String variable, String operation, String value){
        this.next = null;
        this.variable = variable;
        this.operation = operation;
        this.value = value;
    }

    @Override
    public IStory execute(IGame game) {
        String eval = evaluate(game);
        game.setVariable(this.variable, eval);
        return game.getStory().findNode(next);
    }

    private String evaluate(IGame game) {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
        if(this.operation.length()>1)
            scriptEngine.put("$"+this.variable, game.getVariable(this.variable));
        variable_matcher.reset(value);
        while(variable_matcher.find()){
            scriptEngine.put(variable_matcher.group(0),game.getVariable(variable_matcher.group(1)));
        }
        try {
            scriptEngine.eval("$"+this.variable + " " + this.operation + " "+ this.value +";");
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return String.valueOf(scriptEngine.get("$"+this.variable));
    }

    @Override
    public void setNext(int id) {
        this.next = id;
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
