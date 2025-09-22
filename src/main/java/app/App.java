package app;

public class App {
    public static void main(String[] args) {
        Maze maze = new Maze();
        Agent agent = new Agent(maze.map);
        agent.try_exit();
    }
}
