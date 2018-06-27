package tk.cs8898.ll.engine.parser.objects;

public class Delay extends Jump{
    private long delay;
    public Delay(int delay, String unit, String destination) {
        super(destination);
        this.delay = 1000*delay;
        switch (unit.charAt(0)){
            case 'H':
            case 'h':
                this.delay *= 60;
            case 'M':
            case 'm':
                this.delay *= 60;
            case 'S':
            case 's':
                break;
        }
    }

    @Override
    public IStory execute(IGame game) {
        game.delay(delay);
        return super.execute(game);
    }
}
