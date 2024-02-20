package com.edsv;

import java.util.HashMap;

public class Main {
    private HashMap<Long, Node> nodes = new HashMap<>();

    private void loadNodes() {
        DataLoader dl = new DataLoader();
        nodes = dl.load();
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.loadNodes();

        System.out.println(main.nodes.get(740012883L).getDestinations());

        System.out.println(AStarRouteFinder.findRoute(main.nodes, main.nodes.get(740012883L),
                main.nodes.get(740021696L), new Time(8, 0)).stream().map(n -> n.getStop().getName()).reduce("",
                        (a, b) -> a + " -> " + b));
    }
}