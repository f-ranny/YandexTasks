import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class Vertex {
    private int label;
    private boolean isInTree;

    public Vertex(int label) {
        this.label = label;
        this.isInTree = false;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(char label) {
        this.label = label;
    }

    public boolean isInTree() {
        return isInTree;
    }

    public void setInTree(boolean inTree) {
        isInTree = inTree;
    }
}


class Path {
    private int distance;
    private List<Integer> parentVertices;

    public Path(int distance) {
        this.distance = distance;
        this.parentVertices = new ArrayList<>();
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<Integer> getParentVertices() {
        return parentVertices;
    }

    public void setParentVertices(List<Integer> parentVertices) {
        this.parentVertices = parentVertices;
    }
}


class Graph {
    private final int MAX_VERTS = 10;
    private final int INFINITY = 30000;
    private Vertex vertexList[];
    private int relationMatrix[][];
    private int countOfVertices;
    private int countOfVertexInTree;
    private List<Path> shortestPaths;
    private int currentVertex;
    private int startToCurrent;

    public Graph() {
        vertexList = new Vertex[MAX_VERTS];
        relationMatrix = new int[MAX_VERTS][MAX_VERTS];
        countOfVertices = 0;
        countOfVertexInTree = 0;
        for (int i = 0; i < MAX_VERTS; i++) {
            for (int k = 0; k < MAX_VERTS; k++) {
                relationMatrix[i][k] = INFINITY;
                shortestPaths = new ArrayList<>();
            }
        }
    }

    public void addVertex(int lab) {
        vertexList[countOfVertices++] = new Vertex(lab);
    }

    public void addEdge(int start, int end, int weight) {
        relationMatrix[start][end] = weight;
        relationMatrix[end][start] = weight;
    }

    public void path(int end) {
        int startTree = 0;
        vertexList[startTree].setInTree(true);
        countOfVertexInTree = 1;

        for (int i = 0; i < countOfVertices; i++) {
            int tempDist = relationMatrix[startTree][i];
            Path path = new Path(tempDist);
            path.getParentVertices().add(0);
            shortestPaths.add(path);
        }

        while (countOfVertexInTree < countOfVertices) {
            int indexMin = getMin();
            int minDist = shortestPaths.get(indexMin).getDistance();

            if (minDist == INFINITY) {
                System.out.println("В графе присутствуют недостижимые вершины.");
                break;
            } else {
                currentVertex = indexMin;
                startToCurrent = shortestPaths.get(indexMin).getDistance();
            }

            vertexList[currentVertex].setInTree(true);
            countOfVertexInTree++;
            updateShortestPaths();
        }

        displayPaths(end);
    }

    public void clean() {
        countOfVertexInTree = 0;
        for (int i = 0; i < countOfVertices; i++) {
            vertexList[i].setInTree(false);
        }
    }

    private int getMin() {
        int minDist = INFINITY;
        int indexMin = 0;
        for (int i = 1; i < countOfVertices; i++) {
            if (!vertexList[i].isInTree() && shortestPaths.get(i).getDistance() < minDist) {
                minDist = shortestPaths.get(i).getDistance();
                indexMin = i;
            }
        }

        return indexMin;
    }

    private void updateShortestPaths() {
        int vertexIndex = 1;
        while (vertexIndex < countOfVertices) {
            if (vertexList[vertexIndex].isInTree()) {
                vertexIndex++;
                continue;
            }

            int currentToFringe = relationMatrix[currentVertex][vertexIndex];
            int startToFringe = startToCurrent + currentToFringe;
            int shortPathDistance = shortestPaths.get(vertexIndex).getDistance();

            if (startToFringe < shortPathDistance) {
                List<Integer> newParents = new ArrayList<>(shortestPaths.get(currentVertex).getParentVertices());
                newParents.add(currentVertex);
                shortestPaths.get(vertexIndex).setParentVertices(newParents);
                shortestPaths.get(vertexIndex).setDistance(startToFringe);
            }
            vertexIndex++;
        }
    }

    private void displayPaths(int e) {
        e--;
        int dist = shortestPaths.get(e).getDistance();
        List<Integer> parents = shortestPaths.get(e).getParentVertices();

        System.out.println(dist + " " + (parents.size() + 1));

        String result = "";

        for (int j = 0; j < parents.size(); j++) {
            result += vertexList[parents.get(j)].getLabel() + " ";
        }
        System.out.println(result + vertexList[e].getLabel());

    }
}


public class Program {
    public static void main(String[] args) {
        try {
            Graph graph = new Graph();

            Scanner in = new Scanner(System.in);

            int n = in.nextInt();
            int m = in.nextInt();
            int b = in.nextInt();
            int e = in.nextInt();

            int s, f, w;

            graph.addVertex(b);

            for (int j = 1; j < n + 1; j++) {
                if (b != j) {
                    graph.addVertex(j);
                }
            }

            for (int i = 0; i < m; i++) {
                s = in.nextInt();
                f = in.nextInt();
                w = in.nextInt();

                graph.addEdge(s - 1, f - 1, w);
            }

            graph.path(e);
            graph.clean();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}