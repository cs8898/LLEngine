package tk.cs8898.ll.engine.parser.objects;

public class ElseIf extends If {
    public ElseIf(Story story, String variable, String operation, String equation) {
        super(story,variable, operation, equation);
    }

    public ElseIf(Story story, String variable, String operation, String equation, String logik, String variableB, String operationB, String equationB){
        super(story,variable, operation, equation, logik, variableB, operationB, equationB);
    }
}
