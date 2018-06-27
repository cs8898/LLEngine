package tk.cs8898.ll.engine.parser;

import tk.cs8898.ll.engine.parser.objects.*;
import tk.cs8898.ll.engine.parser.objects.If.STATES;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static final String MARK_REGEX = "^:: (.+)";
    private static final Pattern mark_pattern = Pattern.compile(MARK_REGEX);
    private final Matcher mark_matcher = mark_pattern.matcher("");

    private static final String JUMP_REGEX = "^\\[\\[([^\\|\\]]+)\\]\\]";
    private static final Pattern jump_pattern = Pattern.compile(JUMP_REGEX);
    private final Matcher jump_matcher = jump_pattern.matcher("");

    private static final String CHOICE_REGEX = "^(?:<<choice |)\\[\\[(?!delay )([^|]+)\\|([^\\]]+)]](?:>>|)(?: \\| |)";
    private static final Pattern choice_pattern = Pattern.compile(CHOICE_REGEX);
    private final Matcher choice_matcher = choice_pattern.matcher("");

    //private static final String TEXT_REGEX = "^(?![:<]{2}|\\[)([^\\[]+)";
    private static final String TEXT_REGEX = "^(?!\\[|<<)(?:((?:<<\\$|(?!<<))[^\\[])+)";
    private static final Pattern text_pattern = Pattern.compile(TEXT_REGEX);
    private final Matcher text_matcher = text_pattern.matcher("");

    private static final String M_TEXT_REGEX = "^\\[([^\\[]+)\\]";
    private static final Pattern m_text_pattern = Pattern.compile(M_TEXT_REGEX);
    private final Matcher m_text_matcher = m_text_pattern.matcher("");

    private static final String DELAY_REGEX = "^\\[\\[delay (\\d+)(\\w)\\|([^\\]]+)\\]\\]";
    private static final Pattern delay_pattern = Pattern.compile(DELAY_REGEX);
    private final Matcher delay_matcher = delay_pattern.matcher("");

    private static final String SET_REGEX = "^<<set \\$(\\w+) ([\\+\\-\\=]+) ([^>]+)>>";
    private static final Pattern set_pattern = Pattern.compile(SET_REGEX);
    private final Matcher set_matcher = set_pattern.matcher("");

    //private static final String IF_REGEX = "^<<if \\$([^ ]+) ([^ ]+) ([^ \\]]+)>>";
    private static final String IF_REGEX = "^<<if \\$([^ ]+) ([^ ]+) ([^ \\]>]+)(?:(?: ([^ ]+) \\$([^ ]+) ([^ ]+) ([^ \\]]+)>>)|>>)";
    private static final Pattern if_pattern = Pattern.compile(IF_REGEX);
    private final Matcher if_matcher = if_pattern.matcher("");

    private static final String ELSEIF_REGEX = "^<<elseif \\$([^ ]+) ([^ ]+) ([^ \\]>]+)(?:(?: ([^ ]+) \\$([^ ]+) ([^ ]+) ([^ \\]]+)>>)|>>)";
    private static final Pattern elseif_pattern = Pattern.compile(ELSEIF_REGEX);
    private final Matcher elseif_matcher = elseif_pattern.matcher("");

    private static final String ELSE_REGEX = "^<<else>>";
    private static final Pattern else_pattern = Pattern.compile(ELSE_REGEX);
    private final Matcher else_matcher = else_pattern.matcher("");

    private static final String ENDIF_REGEX = "^<<endif>>";
    private static final Pattern endif_pattern = Pattern.compile(ENDIF_REGEX);
    private final Matcher endif_matcher = endif_pattern.matcher("");

    private static final String SILENTLY_REGEX = "^<<(?:end|)silently>>";
    private static final Pattern silently_pattern = Pattern.compile(SILENTLY_REGEX);
    private final Matcher silently_matcher = silently_pattern.matcher("");

    private Path file;
    private Story story;

    private Stack<IBlock> iStack;

    public Parser(Path file){
        this.file = file;
        this.iStack = new Stack<>();
        this.story = new Story();
        this.iStack.push(story);
    }

    public boolean parse(){
        try{
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                processLine(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Story getStory(){
        return story;
    }

    private void processLine(String s) {
        if(s.length() == 0)
            return;
        if(s.contains("//")) {
            //System.err.println(s);
            return;
        }

        /*if(!iStack.empty() && iStack.peek() instanceof If && ((If) iStack.peek()).getState()==STATES.ENDIF){
            iStack.pop();
        }*/

        //SILENTLY
        silently_matcher.reset(s);
        if(silently_matcher.find()){
            if (silently_matcher.end() < s.length() - 1) {
                s = s.substring(silently_matcher.end());
                //System.err.println("SILENTLY there is more: "+s);
                processLine(s);
            }
            return;
        }

        //IF
        if_matcher.reset(s);
        if(if_matcher.find()){
            If ifBlock;
            if(if_matcher.groupCount()<=4 || if_matcher.group(4) == null) {
                ifBlock = new If(story,if_matcher.group(1), if_matcher.group(2), if_matcher.group(3));
            }else{
                ifBlock = new If(story,if_matcher.group(1), if_matcher.group(2), if_matcher.group(3),if_matcher.group(4), if_matcher.group(5), if_matcher.group(6), if_matcher.group(7));
            }
            if(!iStack.empty()){
                iStack.peek().add(ifBlock);
            }
            iStack.push(ifBlock);
            if (if_matcher.end() < s.length() - 1) {
                s = s.substring(if_matcher.end());
                //System.err.println("IF there is more: "+s);
                processLine(s);
            }
            return;
        }
        //ELSEIF
        elseif_matcher.reset(s);
        if(elseif_matcher.find()){
            ElseIf elseIfBlock;
            if(elseif_matcher.groupCount()<=4 || elseif_matcher.group(4) == null) {
                elseIfBlock= new ElseIf(story,elseif_matcher.group(1), elseif_matcher.group(2), elseif_matcher.group(3));
            }else{
                elseIfBlock = new ElseIf(story,elseif_matcher.group(1), elseif_matcher.group(2), elseif_matcher.group(3),elseif_matcher.group(4), elseif_matcher.group(5), elseif_matcher.group(6), elseif_matcher.group(7));
            }
            if(!iStack.empty() && (iStack.peek() instanceof If) && ((If)iStack.peek()).getState() == STATES.IF){
                iStack.peek().add(elseIfBlock);
                iStack.push(elseIfBlock);
            }
            if (elseif_matcher.end() < s.length() - 1) {
                s = s.substring(elseif_matcher.end());
                //System.err.println("ELSEIF there is more: "+s);
                processLine(s);
            }
            return;
        }
        //ELSE
        else_matcher.reset(s);
        if(else_matcher.find()){
            Else elseBlock = new Else(story);
            if(!iStack.empty() && ((If)iStack.peek()).getState().ordinal()>=STATES.IF.ordinal()){
                iStack.peek().add(elseBlock);
            }
            if (else_matcher.end() < s.length() - 1) {
                s = s.substring(else_matcher.end());
                //System.err.println("ELSE there is more: "+s);
                processLine(s);
            }
            return;
        }
        //ENDIF
        endif_matcher.reset(s);
        if(endif_matcher.find()){
            EndIf endIfBlock = new EndIf();
            if(!iStack.empty() && iStack.peek() instanceof If && ((If)iStack.peek()).getState().ordinal()>=STATES.IF.ordinal()) {
                while (iStack.peek() instanceof ElseIf){
                    iStack.pop().add(endIfBlock);
                }
                iStack.pop().add(endIfBlock);
            }
            if (endif_matcher.end() < s.length() - 1) {
                s = s.substring(endif_matcher.end());
                //System.err.println("ENDIF there is more: "+s);
                processLine(s);
            }
            return;
        }

        //Mark
        mark_matcher.reset(s);
        if(mark_matcher.find()){
            iStack.peek().add(new Mark(mark_matcher.group(1)));
            if (mark_matcher.end() < s.length() - 1) {
                s = s.substring(mark_matcher.end());
                //System.err.println("MARK there is more: "+s);
                processLine(s);
            }
            return;
        }

        //Jump
        jump_matcher.reset(s);
        if(jump_matcher.find()){
            iStack.peek().add(new Jump(jump_matcher.group(1)));
            if (jump_matcher.end() < s.length() - 1) {
                s = s.substring(jump_matcher.end());
                //System.err.println("JUMP there is more: "+s);
                processLine(s);
            }
            return;
        }

        //DELAY
        delay_matcher.reset(s);
        if(delay_matcher.find()){
            iStack.peek().add(new Delay(Integer.parseInt(delay_matcher.group(1)), delay_matcher.group(2), delay_matcher.group(3)));
            if (delay_matcher.end() < s.length() - 1) {
                s = s.substring(delay_matcher.end());
                //System.err.println("DELAY there is more: "+s);
                processLine(s);
            }
            return;
        }

        //M_TEXT
        m_text_matcher.reset(s);
        if(m_text_matcher.find()){
            iStack.peek().add(new MText(m_text_matcher.group(1)));
            if (m_text_matcher.end() < s.length() - 1) {
                s = s.substring(m_text_matcher.end());
                //System.err.println("M_TEXT there is more: "+s);
                processLine(s);
            }
            return;
        }

        //TEXT
        text_matcher.reset(s);
        if(text_matcher.find()){
            iStack.peek().add(new Text(text_matcher.group(0)));
            if (text_matcher.end() < s.length() - 1) {
                s = s.substring(text_matcher.end());
                //System.err.println("TEXT there is more: "+s);
                processLine(s);
            }
            return;
        }

        //Choice
        choice_matcher.reset(s);
        if(choice_matcher.find()){
            Choice choice = new Choice();
            do {
                choice.addOption(choice_matcher.group(1), choice_matcher.group(2));
                s=s.substring(choice_matcher.end());
                choice_matcher.reset(s);
            }while(choice_matcher.find());
            iStack.peek().add(choice);
            if(s.length()>0){
                //System.err.println("CHOICE there is more: "+s);
                processLine(s);
            }
            return;
        }

        //SET
        set_matcher.reset(s);
        if(set_matcher.find()){
            iStack.peek().add(new Set(set_matcher.group(1), set_matcher.group(2), set_matcher.group(3)));
            if (set_matcher.end() < s.length() - 1) {
                s = s.substring(set_matcher.end());
                //System.err.println("SET there is more: "+s);
                processLine(s);
            }
            return;
        }

        System.err.println("UNKNOWN LINE: " + s);
    }
}
