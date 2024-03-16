package com.edsv;

import java.util.HashMap;
import java.util.LinkedList;

public class Main {
    private HashMap<Long, Node> nodes = new HashMap<>();

    private void loadNodes() {
        nodes = DataLoader.load();
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.loadNodes();


        // to Fruängen T-bana
        LinkedList<Edge> routeA = AStarRouteFinder.findRoute(main.nodes, main.nodes.get(740012883L),
                main.nodes.get(740021719L), new Time(8, 0));
        System.out.println(routeA);// .stream().map(n -> n.getStop().getName()).reduce("",
        // (a, b) -> a + " -> " + b));
        System.out.println(routeA.stream().map(e -> e.getArrival().getTripId()).distinct().count());
        // print time till arrival
        System.out.println(routeA.getLast().getArrival().getArrivalTime().getDifference(new Time(8, 0)));

        // to Rågsved
        LinkedList<Edge> routeB = AStarRouteFinder.findRoute(main.nodes, main.nodes.get(740012883L),
                main.nodes.get(740021713L), new Time(8, 0));
        // .stream().map(n -> n.getStop().getName()).reduce("",
        // (a, b) -> a + " -> " + b));
        System.out.println(routeB);
        System.out.println(routeB.stream().map(e -> e.getArrival().getTripId()).distinct().count());
        System.out.println(routeB.getLast().getArrival().getArrivalTime().getDifference(new Time(8, 0)));
    }
}
