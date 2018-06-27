package tk.cs8898.ll.engine.parser.objects;

public class MText extends Text{

    public MText(String text) {
        super(text);
    }

    @Override
    public IStory execute(IGame game) {
        game.outputSpecial(parseText(game));
        return game.getStory().findNode(next);
    }
}
