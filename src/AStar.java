
import java.util.PriorityQueue;

/**
 * @author Lewis Shemery
 */

public class AStar {

    // sqrt(10^2 + 10^2) = 14.14
    public static final int D_COST = 14; //diagonal cost
    public static final int V_H_COST = 10; //linear cost

    static int startI, startJ;
    static int endI, endJ;

    // lambda expression used as comparator for priority queue
    static PriorityQueue<Node> openSet = new PriorityQueue<>((Node1, Node2) -> {
        Node n1 = Node1;
        Node n2 = Node2;

        return n1.fScore < n2.fScore ? -1
                : n1.fScore > n2.fScore ? 1 : 0;
    });

    public static void main(String[] args) {
        // initializing 2D Array of type Node
        Node[][] grid = new Node[10][10];

        // setting indexes of starting and ending nodes
        startNode(0, 0);
        endNode(9,9);

        // random function used to generate null nodes which are used as walls in the grid
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (Math.random() < 0.2) {
                    grid[i][j] = null;
                } else {
                    grid[i][j] = new Node(i, j);
                }
            }
        }
        
        // making sure start and end arent null
        grid[startI][startJ] = new Node(startI, startJ);
        grid[endI][endJ] = new Node(endI, endJ);
        
        if (runAStar(grid)) {
            System.out.println("We did it!");
            Node current = grid[endI][endJ];
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (i == startI && j == startJ) {
                        System.out.print("ST    "); //Start
                    } else if (i == endI && j == endJ) {
                        System.out.print("END");
                    } else if (grid[i][j] != null) {
                        System.out.printf("%-5s ", "_");
                    } else {
                        System.out.print("O     ");
                    }
                }
                System.out.println();
                System.out.println();
            }
            System.out.println();
            
            for (int i = endI; i >= 0; i--) {
                for (int j = endJ; j >= 0; j--) {
                if (current == grid[i][j]) {
                    System.out.print("X     ");
                    current = current.parent;    
                    } else if (grid[i][j] != null){
                        System.out.printf("%-5s ", "_");
                    }
                else {
                        System.out.print("O     ");
                    }
                }
                System.out.println();
                System.out.println();    
                }
        } else {
            System.out.println("No possible solution.");
        }
    }

    // the bread and butter baby!
    public static boolean runAStar(Node[][] grid) {
        //adding the starting node to the open set
        openSet.add(grid[startI][startJ]);

        Node current;
        Node neighbor;

        while (!openSet.isEmpty()) {
            current = openSet.poll(); // .poll() retrieves and removes the head of the priorty queue
            current.visited = true; //showing the node has been visited
            if (current.equals(grid[endI][endJ])) {
                return true;
            }

            /*
                The next chunk of if statements check all the neighbors of the parents node and then 
                calculates the fScore by passing the values into calculateScore().
            */

            // east node
            if (current.i + 1 < grid.length) {
                neighbor = grid[current.i + 1][current.j];
                calculateScore(current, neighbor, current.fScore + V_H_COST);
                // north east node
                if (current.j - 1 >= 0) {
                    neighbor = grid[current.i + 1][current.j - 1];
                    calculateScore(current, neighbor, current.fScore + D_COST);
                }
                //south east node
                if (current.j + 1 < grid[0].length) {
                    neighbor = grid[current.i + 1][current.j + 1];
                    calculateScore(current, neighbor, current.fScore + D_COST);
                }
            }
            
            // west node
            if (current.i - 1 >= 0) {
                neighbor = grid[current.i - 1][current.j];
                calculateScore(current, neighbor, current.fScore + V_H_COST);
                // north west node
                if (current.j - 1 >= 0) {
                    neighbor = grid[current.i - 1][current.j - 1];
                    calculateScore(current, neighbor, current.fScore + D_COST);
                }
                // south west node
                if (current.j + 1 < grid[0].length) {
                    neighbor = grid[current.i - 1][current.j + 1];
                    calculateScore(current, neighbor, current.fScore + D_COST);
                }
            }
            // north node
            if (current.j - 1 >= 0) {
                neighbor = grid[current.i][current.j - 1];
                calculateScore(current, neighbor, current.fScore + V_H_COST);
            }
            // south node
            if (current.j + 1 < grid[0].length) {
                neighbor = grid[current.i][current.j + 1];
                calculateScore(current, neighbor, current.fScore + V_H_COST);
            }
        }
        return false;
    }
    
    public static void calculateScore(Node current, Node neighbor, int cost){
        if(neighbor.visited){
            return;
        }
        // Calculating the heuristic using the diagonal distance
        int dx = Math.abs(neighbor.i - endI);
        int dy = Math.abs(neighbor.j - endJ);
        if (dx > dy) {
            neighbor.heuristic = D_COST * dy + V_H_COST * (dx - dy);
        } else {
            neighbor.heuristic = D_COST * dx + V_H_COST * (dy - dx);
        }
        
        int totalScore = neighbor.heuristic + cost;
        if(totalScore < neighbor.fScore || !openSet.contains(neighbor)){
                neighbor.fScore = totalScore;
                neighbor.parent = current;
            if(!openSet.contains(neighbor)){
                openSet.add(neighbor);
            }
       }
    }

    public static void startNode(int i, int j) {
        startI = i;
        startJ = j;
    }

    public static void endNode(int i, int j) {
        endI = i;
        endJ = j;
    }

    public static class Node {

        int heuristic = 0; // distsance from current node to end node
        int fScore = 0; // total cost (g + h)
        int gScore = 0; // distance from starting node to current node
        int i, j; // x and y position in grid
        boolean visited = false;
        Node parent; //used to display optimal path

        Node(int i, int j) {
            this.i = i;
            this.j = j;
        }

    }
}
