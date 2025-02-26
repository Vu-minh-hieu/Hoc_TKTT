/******************************************************************************
 *  Compilation:  javac DijkstraSD.java
 *  Execution:    java DijkstraSD input.txt s
 *  Dependencies: ReverseEdgeWeightedDigraph.java IndexMinPQ.java Stack.java DirectedEdge.java
 *  Data files:   https://algs4.cs.princeton.edu/44sp/tinyEWD.txt
 *                https://algs4.cs.princeton.edu/44sp/mediumEWD.txt
 *                https://algs4.cs.princeton.edu/44sp/largeEWD.txt
 *
 *  Dijkstra's algorithm. Computes the shortest path tree.
 *  Assumes all weights are nonnegative.
 *
 *  % java DijkstraSD "tinyEWD.txt", "0"
 * 0 to 0 (0.00)  
 * 1 to 0 (1.39)  6->0  0,58   3->6  0,52   1->3  0,29   
 * 2 to 0 (1.83)  6->0  0,58   3->6  0,52   7->3  0,39   2->7  0,34   
 * 3 to 0 (1.10)  6->0  0,58   3->6  0,52   
 * 4 to 0 (1.86)  6->0  0,58   3->6  0,52   7->3  0,39   4->7  0,37   
 * 5 to 0 (1.71)  6->0  0,58   3->6  0,52   1->3  0,29   5->1  0,32   
 * 6 to 0 (0.58)  6->0  0,58   
 * 7 to 0 (1.49)  6->0  0,58   3->6  0,52   7->3  0,39
 *
 *  % java DijkstraSD mediumEWD.txt 0
 *  0 to 0 (0.00)  
 *  0 to 1 (0.71)  0->44  0.06   44->93  0.07   ...  107->1  0.07   
 *  0 to 2 (0.65)  0->44  0.06   44->231  0.10  ...  42->2  0.11   
 *  0 to 3 (0.46)  0->97  0.08   97->248  0.09  ...  45->3  0.12   
 *  0 to 4 (0.42)  0->44  0.06   44->93  0.07   ...  77->4  0.11   
 *  ...
 * tu moi dinh den 1 dinh
 ******************************************************************************/

 


/**
 *  The {@code DijkstraSD} class represents a data type for solving the
 *  single-source shortest paths problem in edge-weighted digraphs
 *  where the edge weights are nonnegative.
 *  <p>
 *  This implementation uses Dijkstra's algorithm with a binary heap.
 *  The constructor takes time proportional to <em>E</em> log <em>V</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Afterwards, the {@code distFrom()} and {@code hasPathFrom()} methods take
 *  constant time and the {@code pathFrom()} method takes time proportional to the
 *  number of edges in the shortest path returned.
 *  <p>
 *  For additional documentation,    
 *  see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of    
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne. 
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class DijkstraSD {
    private double[] distFrom;          // distFrom[v] = distance  of shortest v->s path
    private DirectedEdge[] edgeFrom;    // edgeFrom[v] = last edge on shortest v->s path
    private IndexMinPQ<Double> pq;    // priority queue of vertices

    /**
     * Computes a shortest-paths tree from the source vertex {@code s} to every other
     * vertex in the edge-weighted digraph {@code G}.
     *
     * @param  G the edge-weighted digraph
     * @param  s the source vertex
     * @throws IllegalArgumentException if an edge weight is negative
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public DijkstraSD(ReverseEdgeWeightedDigraph G, int s) {
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        distFrom = new double[G.V()];
        edgeFrom = new DirectedEdge[G.V()];

        validateVertex(s);

        for (int v = 0; v < G.V(); v++){
            distFrom[v] = Double.POSITIVE_INFINITY;
        }
        distFrom[s] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(s, distFrom[s]);

        // Bo sung vong while chon phan tu min trong PQ .......
        while(!pq.isEmpty()){
            int u = pq.delMin();
            for (DirectedEdge e : G.adj(u)){
                relax(e);
            }
        }

        // check optimality conditions
        assert check(G, s);
    }

    // relax edge e and update pq if changed
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distFrom[v] > distFrom[w] + e.weight()) {
            distFrom[v] = distFrom[w] + e.weight();
            edgeFrom[v] = e;
            if (pq.contains(v)) pq.decreaseKey(v, distFrom[v]);
            else                pq.insert(v, distFrom[v]);
        }
    }

    /**
     * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
     * @param  v the destination vertex
     * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
     *         {@code Double.POSITIVE_INFINITY} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public double distFrom(int v) {
        validateVertex(v);
        return distFrom[v];
    }

    /**
     * Returns true if there is a path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return {@code true} if there is a path from the source vertex
     *         {@code s} to vertex {@code v}; {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean hasPathFrom(int v) {
        validateVertex(v);
        return distFrom[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return a shortest path from the source vertex {@code s} to vertex {@code v}
     *         as an iterable of edges, and {@code null} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<DirectedEdge> pathFrom(int v) {
        validateVertex(v);
        if (!hasPathFrom(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeFrom[v]; e != null; e = edgeFrom[e.to()]) {
            path.push(e);
        }
        Stack<DirectedEdge> rpath = new Stack<DirectedEdge>();
        while(!path.isEmpty()){
            rpath.push(path.pop());
        }
        return rpath;
    }


    // check optimality conditions:
    // (i) for all edges e:            distFrom[e.to()] <= distFrom[e.from()] + e.weight()
    // (ii) for all edge e on the SPT: distFrom[e.to()] == distFrom[e.from()] + e.weight()
    private boolean check(ReverseEdgeWeightedDigraph G, int s) {

        // check that edge weights are nonnegative
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        // check that distFrom[v] and edgeFrom[v] are consistent
        if (distFrom[s] != 0.0 || edgeFrom[s] != null) {
            System.err.println("distFrom[s] and edgeFrom[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeFrom[v] == null && distFrom[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distFrom[] and edgeFrom[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy distFrom[w] <= distFrom[v] + e.weight()
        for (int v = 0; v < G.V(); v++) {
            for (DirectedEdge e : G.adj(v)) {
                int w = e.from();
                if (distFrom[v] + e.weight() < distFrom[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy distFrom[w] == distFrom[v] + e.weight()
        for (int w = 0; w < G.V(); w++) {
            if (edgeFrom[w] == null) continue;
            DirectedEdge e = edgeFrom[w];
            int v = e.to();
            if (w != e.from()) return false;
            if (distFrom[v] + e.weight() != distFrom[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = distFrom.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Unit tests the {@code DijkstraSD} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        ReverseEdgeWeightedDigraph G = new ReverseEdgeWeightedDigraph(in);
        int s = Integer.parseInt(args[1]);

        // compute shortest paths
        DijkstraSD sp = new DijkstraSD(G, s);

        // print shortest path
        for (int t = 0; t < G.V(); t++) {
            if (sp.hasPathFrom(t)) {
                StdOut.printf("%d to %d (%.2f)  ", t, s, sp.distFrom(t));
                for (DirectedEdge e : sp.pathFrom(t)) {
                    StdOut.print(e + "   ");
                }
                StdOut.println();
            }
            else {
                StdOut.printf("%d to %d         no path\n", t, s);
            }
        }
    }

}

/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,    
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
