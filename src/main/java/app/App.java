package app;

import app.struct.graph.GrafoException;

public class App {
    public static void main(String[] args) throws GrafoException {
        Maze maze = new Maze();
        AgentGraph agent = new AgentGraph(maze.map);
        agent.try_exit();
    }
}
