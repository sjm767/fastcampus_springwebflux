package example.reactorpattern.reactorhttp;

public class ReactorPatternMain {

    public static void main(String[] args) {
        Reactor reactor = new Reactor(8080);
        reactor.run();
    }
}
