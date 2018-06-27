LLEngine  
========  

This is an recreation of the LifeLine Engine/Parser.  
The Story is provided as a TXT file.  

Useage  
------

Parse ans Launch a ConsoleGame  
```
Parser parser = new Parser(path);
parser.parse();

Story story = parser.getStory();

IGame game = new ConsoleGame(story);
game.play();
```

Implement the IGame inteface to create your own GameLogic  
```
public class SimpleGameSkeleton implements IGame {
    private Story story;
    private HashMap<String, String> variables;

    public ConsoleGame(Story story){
        this.story = story;
        this.variables = new HashMap<>();
    }

    public Story getStory(){
        return story;
    }

    @Override
    public void setVariable(String variable, String val) {
        this.variables.put(variable, val);
    }

    @Override
    public String getVariable(String variable) {
        return this.variables.get(variable);
    }

    @Override
    public void outputNormal(String text) {
        //TODO
    }

    @Override
    public void outputSpecial(String text) {
        //TODO
    }

    @Override
    public void delay(long delay) {
        //TODO
    }

    @Override
    public String outputChoice(Choice choice) {
        //TODO
    }

    @Override
    public void play() {
        IStory obj = story;
        while(obj != null){
            obj = obj.execute(this);
        }
    }
}
```


Script Syntax  
------  

Examples can be found in `src/main/resources/assets`  

* Normal Text  
```
This is some Normal Text
And Here is some variable <<$var>>
```


* Special Text  
```
[Some Special Text]
[This does also work with <<$var>>]
```


* IF  
```
<<if $var eq "bar">>
<<elseif $bar geq 1>>
<<elseif $foo = 2 and $bar = "foo">>
<<else>>
<<endif>>
```


* SET Variable  
```
<<set $var = "foo">>
<<set $bar = 1>>
<<set $bar += 2>>
<<set $bar = $bar - 3>>
```

TODO  
----  
* Implement IF as pure Script Evaluation  
  * Possible to use ($var + $var2 >= 10)  
  * Define Logical Operations  
