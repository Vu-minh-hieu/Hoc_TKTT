/******************************************************************************
 *  % java BellmanFordSD "tinyEWDn.txt", "0"
 *  0 to 0 ( 0.00)  
    1 to 0 (-0.59)  6->0 -1,40   3->6  0,52   1->3  0,29   
    2 to 0 (-0.15)  6->0 -1,40   3->6  0,52   7->3  0,39   2->7  0,34   
    3 to 0 (-0.88)  6->0 -1,40   3->6  0,52   
    4 to 0 (-0.12)  6->0 -1,40   3->6  0,52   7->3  0,39   4->7  0,37   
    5 to 0 (-0.27)  6->0 -1,40   3->6  0,52   1->3  0,29   5->1  0,32   
    6 to 0 (-1.40)  6->0 -1,40   
    7 to 0 (-0.49)  6->0 -1,40   3->6  0,52   7->3  0,39    
 *
 *  % java BellmanFordSD "tinyEWDnc.txt", "0"
 *  4->5  0.35
 *  5->4 -0.66
 *
 *
 ******************************************************************************/

public class BellmanFordSD {
    private double[] distFrom;               // distFrom[v] = distance  of shortest v->s path
    private DirectedEdge[] edgeFrom;         // edgeFrom[v] = last edge on shortest v->s path
    private boolean[] onQueue;             // onQueue[v] = is v currently on the queue?
    private Queue<Integer> queue;          // queue of vertices to relax
    private int cost;                      // number of calls to relax()
    private Iterable<DirectedEdge> cycle;  // negative cycle (or null if no such cycle)

    /**
     * Computes a shortest paths tree from {@code s} to every other vertex in
     * the edge-weighted digraph {@code G}.
     * @param G the acyclic digraph
     * @param s the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public BellmanFordSD(ReverseEdgeWeightedDigraph G, int s) {
        distFrom  = new double[G.V()];
        edgeFrom  = new DirectedEdge[G.V()];
        onQueue = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            distFrom[v] = Double.POSITIVE_INFINITY;
        distFrom[s] = 0.0;

        // Bellman-Ford algorithm
        queue = new Queue<Integer>();
        queue.enqueue(s);
        onQueue[s] = true;
        while (!queue.isEmpty() && !hasNegativeCycle()) {
            int v = queue.dequeue();
            onQueue[v] = false;
            relax(G, v);
        }

        assert check(G, s);
    }

    // relax vertex v and put other endpoints on queue if changed
    private void relax(ReverseEdgeWeightedDigraph G, int v) {
        for (DirectedEdge e : G.adj(v)) {
            int w = e.from();
            if (distFrom[w] > distFrom[v] + e.weight()) {
                distFrom[w] = distFrom[v] + e.weight();
                edgeFrom[w] = e;
                if (!onQueue[w]) {
                    queue.enqueue(w);
                    onQueue[w] = true;
                }
            }
            if (cost++ % G.V() == 0) {
                findNegativeCycle();
                if (hasNegativeCycle()) return;  // found a negative cycle
            }
        }
    }

    /**
     * Is there a negative cycle reachable from the source vertex {@code s}?
     * @return {@code true} if there is a negative cycle reachable from the
     *    source vertex {@code s}, and {@code false} otherwise
     */
    public boolean hasNegativeCycle() {
        return cycle != null;
    }

    /**
     * Returns a negative cycle reachable from the source vertex {@code s}, or {@code null}
     * if there is no such cycle.
     * @return a negative cycle reachable from the soruce vertex {@code s} 
     *    as an iterable of edges, and {@code null} if there is no such cycle
     */
    public Iterable<DirectedEdge> negativeCycle() {
        return cycle;
    }

    // by finding a cycle in predecessor graph
    private void findNegativeCycle() {
        int V = edgeFrom.length;
        ReverseEdgeWeightedDigraph spt = new ReverseEdgeWeightedDigraph(V);
        for (int v = 0; v < V; v++)
            if (edgeFrom[v] != null)
                spt.addEdge(edgeFrom[v]);

        EdgeWeightedDirectedCycle_T finder = new EdgeWeightedDirectedCycle_T(spt);
        cycle = finder.cycle();
    }

    /**
     * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
     * @param  v the destination vertex
     * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
     *         {@code Double.POSITIVE_INFINITY} if no such path
     * @throws UnsupportedOperationException if there is a negative cost cycle reachable
     *         from the source vertex {@code s}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public double distFrom(int v) {
        validateVertex(v);
        if (hasNegativeCycle())
            throw new UnsupportedOperationException("Negative cost cycle exists");
        return distFrom[v];
    }

    /**
     * Is there a path from the source {@code s} to vertex {@code v}?
     * @param  v the destination vertex
     * @return {@code true} if there is a path from the source vertex
     *         {@code s} to vertex {@code v}, and {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean hasPathFrom(int v) {
        validateVertex(v);
        return distFrom[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source {@code s} to vertex {@code v}.
     * @param  v the destination vertex
     * @return a shortest path from the source {@code s} to vertex {@code v}
     *         as an iterable of edges, and {@code null} if no such path
     * @throws UnsupportedOperationException if there is a negative cost cycle reachable
     *         from the source vertex {@code s}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<DirectedEdge> pathFrom(int v) {
        validateVertex(v);
        if (hasNegativeCycle())
            throw new UnsupportedOperationException("Negative cost cycle exists");
        if (!hasPathFrom(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeFrom[v]; e != null; e = edgeFrom[e.to()]) {
            path.push(e);
        }
        return path;
    }

    // check optimality conditions: either 
    // (i) there exists a negative cycle reacheable from s
    //     or 
    // (ii)  for all edges e = v->w:            distFrom[w] <= distFrom[v] + e.weight()
    // (ii') for all edges e = v->w on the SPT: distFrom[w] == distFrom[v] + e.weight()
    private boolean check(ReverseEdgeWeightedDigraph G, int s) {

        // has a negative cycle
        if (hasNegativeCycle()) {
            double weight = 0.0;
            for (DirectedEdge e : negativeCycle()) {
                weight += e.weight();
            }
            if (weight >= 0.0) {
                System.err.println("error: weight of negative cycle = " + weight);
                return false;
            }
        }

        // no negative cycle reachable from source
        else {

            // check that distFrom[v] and edgeFrom[v] are consistent
            if (distFrom[s] != 0.0 || edgeFrom[s] != null) {
                System.err.println("distanceFrom[s] and edgeFrom[s] inconsistent");
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
        }

        StdOut.println("Satisfies optimality conditions");
        StdOut.println();
        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = distFrom.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Unit tests the {@code BellmanFordSD} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        int s = Integer.parseInt(args[1]);
        ReverseEdgeWeightedDigraph G = new ReverseEdgeWeightedDigraph(in);

        BellmanFordSD sp = new BellmanFordSD(G, s);

        // print negative cycle
        if (sp.hasNegativeCycle()) {
            for (DirectedEdge e : sp.negativeCycle())
                StdOut.println(e);
        }

        // print shortest paths
        else {
            for (int v = 0; v < G.V(); v++) {
                if (sp.hasPathFrom(v)) {
                    StdOut.printf("%d to %d (%5.2f)  ", v, s, sp.distFrom(v));
                    for (DirectedEdge e : sp.pathFrom(v)) {
                        StdOut.print(e + "   ");
                    }
                    StdOut.println();
                }
                else {
                    StdOut.printf("%d to %d           no path\n", v, s);
                }
            }
        }

    }

}  