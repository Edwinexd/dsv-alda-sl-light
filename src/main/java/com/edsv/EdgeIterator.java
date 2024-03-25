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

import java.util.Iterator;
import java.util.TreeSet;

public class EdgeIterator implements Iterator<Edge> {
    private TreeSet<Edge> edges;
    private Iterator<Edge> iterator;
    private Edge firstEdge;
    private int offsetRemainder;
    private boolean loopedAround;
    private Time startAfter;


    public EdgeIterator(TreeSet<Edge> edges, Time startAfter) {
        this.edges = edges;
        this.iterator = edges.iterator();
        this.startAfter = startAfter;
        calculateOffset();
    }

    private void calculateOffset() {
        int i = 0;
        while (iterator.hasNext()) {
            Edge edge = iterator.next();
            if (edge.getDeparture().getDepartureTime().compareTo(startAfter) >= 0) {
                offsetRemainder = i;
                firstEdge = edge;
                return;
            }
            i++;
        }
        // No offset needed, iter from beginning
        iterator = edges.iterator();
    }

    @Override
    public boolean hasNext() {
        if (firstEdge != null) {
            return true;
        }
        if (iterator.hasNext() && !loopedAround) {
            return true;
        }
        if (offsetRemainder > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Edge next() {
        if (firstEdge != null) {
            Edge returnValue = firstEdge;
            firstEdge = null;
            return returnValue;
        }
        if (offsetRemainder == 0 && loopedAround) {
            throw new IllegalStateException("No more elements");
        }
        if (iterator.hasNext()) {
            Edge edge = iterator.next();
            if (loopedAround) {
                offsetRemainder--;
            }
            return edge;
        }
        if (!loopedAround) {
            loopedAround = true;
            iterator = edges.iterator();
            offsetRemainder--;
            return iterator.next();
        }
        throw new IllegalStateException("No more elements");
    }
}
