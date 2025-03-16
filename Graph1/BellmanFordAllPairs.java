/******************************************************************************
 *  % java BellmanFordAllPairs "tinyEWDn.txt"
 ******************************************************************************/

public class BellmanFordAllPairs {
    private double[][] distTo;               // distTo[i][v] = distance  of shortest i->v path
    private DirectedEdge[][] edgeTo;         // edgeTo[i][v] = last edge on shortest i->v path
    private boolean[][] onQueue;             // onQueue[i][v] = is v currently on the queue?
    private Queue<Integer> queue;          // queue of vertices to relax
    private int cost;                      // number of calls to relax()
    private Iterable<DirectedEdge> cycle;  // negative cycle (or null if no such cycle)
    public BellmanFordAllPairs(EdgeWeightedDigraph G) {
        distTo  = new double[G.V()][G.V()];
        edgeTo  = new DirectedEdge[G.V()][G.V()];
        onQueue = new boolean[G.V()][G.V()];
        
        for (int i = 0; i < G.V(); i++){
            for (int j = 0; j < G.V(); j++){
                distTo[i][j] = Double.POSITIVE_INFINITY;
                if (i == j) distTo[i][j] = 0.0;
            }
        }
        // Bellman-Ford algorithm
        
        int V = G.V() * G.V();
        queue = new Queue<Integer>();
        for (int i = 0; i < G.V(); i++){
            queue.enqueue(i);
            onQueue[i][i] = true;
            while (!queue.isEmpty() && !hasNegativeCycle()) {
                int j = queue.dequeue();
                onQueue[i][j] = false;
                relax(G, i, j);
            }
    
            assert check(G, i);
        }
    }

    // relax vertex v and put other endpoints on queue if changed
    private void relax(EdgeWeightedDigraph G, int i, int v) {
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if (distTo[i][w] > distTo[i][v] + e.weight()) {
                distTo[i][w] = distTo[i][v] + e.weight();
                edgeTo[i][w] = e;
                if (!onQueue[i][w]) {
                    queue.enqueue(w);
                    onQueue[i][w] = true;
                }
            }
            if (cost++ % G.V() == 0) {
                findNegativeCycle(i);
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
    private void findNegativeCycle(int i) {
        int V = edgeTo.length;
        EdgeWeightedDigraph spt = new EdgeWeightedDigraph(V);
        for (int v = 0; v < V; v++)
            if (edgeTo[i][v] != null)
                spt.addEdge(edgeTo[i][v]);

        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(spt);
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
    public double distTo(int i, int v) {
        validateVertex(i);
        validateVertex(v);
        if (hasNegativeCycle())
            throw new UnsupportedOperationException("Negative cost cycle exists");
        return distTo[i][v];
    }

    /**
     * Is there a path from the source {@code s} to vertex {@code v}?
     * @param  v the destination vertex
     * @return {@code true} if there is a path from the source vertex
     *         {@code s} to vertex {@code v}, and {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean hasPathTo(int i, int v) {
        validateVertex(i);
        validateVertex(v);
        return distTo[i][v] < Double.POSITIVE_INFINITY;
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
    public Iterable<DirectedEdge> pathTo(int i, int v) {
        validateVertex(i);
        validateVertex(v);
        if (hasNegativeCycle())
            throw new UnsupportedOperationException("Negative cost cycle exists");
        if (!hasPathTo(i, v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[i][v]; e != null; e = edgeTo[i][e.from()]) {
            path.push(e);
        }
        return path;
    }

    // check optimality conditions: either 
    // (i) there exists a negative cycle reacheable from s
    //     or 
    // (ii)  for all edges e = v->w:            distTo[w] <= distTo[v] + e.weight()
    // (ii') for all edges e = v->w on the SPT: distTo[w] == distTo[v] + e.weight()
    private boolean check(EdgeWeightedDigraph G, int s) {

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

            // check that distTo[s][v] and edgeTo[s]v] are consistent
            if (distTo[s][s] != 0.0 || edgeTo[s][s] != null) {
                System.err.println("distanceTo[s][s] and edgeTo[s][s] inconsistent");
                return false;
            }
            for (int v = 0; v < G.V(); v++) {
                if (v == s) continue;
                if (edgeTo[s][v] == null && distTo[s][v] != Double.POSITIVE_INFINITY) {
                    System.err.println("distTo[][] and edgeTo[][] inconsistent");
                    return false;
                }
            }

            // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
            for (int v = 0; v < G.V(); v++) {
                for (DirectedEdge e : G.adj(v)) {
                    int w = e.to();
                    if (distTo[s][v] + e.weight() < distTo[s][w]) {
                        System.err.println("edge " + e + " not relaxed");
                        return false;
                    }
                }
            }

            // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
            for (int w = 0; w < G.V(); w++) {
                if (edgeTo[s][w] == null) continue;
                DirectedEdge e = edgeTo[s][w];
                int v = e.from();
                if (w != e.to()) return false;
                if (distTo[s][v] + e.weight() != distTo[s][w]) {
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
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Unit tests the {@code BellmanFordAllPairs} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);

        BellmanFordAllPairs sp = new BellmanFordAllPairs(G);

        // print negative cycle
        for(int s = 0; s < G.V(); s++){
            if (sp.hasNegativeCycle()) {
                for (DirectedEdge e : sp.negativeCycle())
                    StdOut.println(e);
            }
    
            // print shortest paths
            else {
                for (int v = 0; v < G.V(); v++) {
                    if (sp.hasPathTo(s, v)) {
                        StdOut.printf("%d to %d (%5.2f)  ", s, v, sp.distTo(s, v));
                        for (DirectedEdge e : sp.pathTo(s, v)) {
                            StdOut.print(e + "   ");
                        }
                        StdOut.println();
                    }
                    else {
                        StdOut.printf("%d to %d           no path\n", s, v);
                    }
                }
            }
        }
    }
}