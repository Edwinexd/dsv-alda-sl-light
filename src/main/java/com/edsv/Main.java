/* 
dsv-alda-sl-light: A representation of the SL transit system using a graph with path finding capabilities using the A* algorithm, written in Java.
Copyright (C) 2024 Edwin Sundberg

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
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
