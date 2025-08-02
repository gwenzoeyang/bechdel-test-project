import java.util.ArrayList;
import java.util.LinkedList;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import javafoundations.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

/**
 * The HollywoodGraph class uses an undirected graph G with vertices representing movies 
 * and actors. Edges will reflect the relationship “actor played role in movie”. 
 * Every actor A that played in a movie M will result in an undirected edge (A, M).
 *
 * @author Audrey, Gwen, Suzy,and Aileen 
 * @version 05/01/24
 */
public class HollywoodGraph<T> implements Graph<T>
{
    private ArrayList<T> vertices;
    private ArrayList<LinkedList<T>> arcs;
    private int count; //the number of vertices
    private Hashtable<String, Integer> femaleMovies;
    private Hashtable<String, Integer> totalMovies;

    /**
     * Constructor for objects of class HollywoodGraph
     * 
     * @param inFileName The input file name 
     */
    public HollywoodGraph(String inFileName){
        count = 0;
        vertices = new ArrayList<T>();
        arcs = new ArrayList<LinkedList<T>>();
        femaleMovies = new Hashtable<String, Integer>();
        totalMovies = new Hashtable<String, Integer>();
        readFile(inFileName);
    }

    /**
     * Helper method to read data from file.
     * 
     * @param inFileName The input file name
     */
    private void readFile(String inFileName){ 
        try{
            Scanner reader = new Scanner(new File(inFileName));
            reader.useDelimiter(",|\\n|\\r");
            reader.nextLine();//skips the first line
            while(reader.hasNext()){
                String movie = reader.next();
                String actor = reader.next();

                T vertex1 = (T) movie.substring(1,movie.length()-1);//stripping the quotation mark
                T vertex2 = (T) actor.substring(1,actor.length()-1);

                for(int i=0; i<3;i++){//skips the next three token
                    reader.next();
                }
                String gender = reader.next();
                gender = gender.substring(1,gender.length()-1);
                if(gender.equals("Female")){  
                    int femaleCount = femaleMovies.getOrDefault(movie, 0);
                    femaleMovies.put(movie, femaleCount+1); //increase value of femaleCount
                }
                int count = totalMovies.getOrDefault(movie, 0); 
                totalMovies.put(movie, count+1);//increases value of totalPeople

                addVertex(vertex1);
                addVertex(vertex2);
                addEdge(vertex1, vertex2);
            }
            reader.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }

    /** 
     * Returns true if this graph is empty, false otherwise.
     * 
     * @return true if the graph is empty and false otherwise.
     */
    public boolean isEmpty() {
        return count==0;
    }

    /** 
     * Returns the number of vertices in this graph. 
     * 
     * @return the number of vertices in this graph
     */
    public int getNumVertices(){
        return count;
    }

    /** 
     * Returns the number of arcs in this graph. 
     * 
     * @return the number of arcs in this graph
     */
    public int getNumArcs(){
        int tot = 0;
        for (LinkedList l: arcs) {
            tot += l.size();
        }
        return tot;
    }

    /** 
     * Returns true iff a directed edge exists b/w given vertices
     * 
     * @param vertex1 The first vertex
     * @param vertex2 The second vertex
     * 
     * @return true iff there is a directed edge between the two vertices and false otherwise
     */
    public boolean isArc (T vertex1, T vertex2){
        int index = vertices.indexOf(vertex1);
        if(index == -1) return false; //when vertex1 is not in vertices
        return arcs.get(index).contains(vertex2);
    }

    /** 
     * Returns true iff an edge exists between two given vertices
     * which means that two corresponding arcs exist in the graph 
     * 
     * @param vertex1 The first vertex
     * @param vertex2 The second vertex
     * 
     * @return true iff an edge exists between two given vertices
     */
    public boolean isEdge (T vertex1, T vertex2){
        return isArc(vertex1,vertex2) && isArc(vertex2,vertex1);
    }

    /** 
     * Adds a vertex to this graph, associating object with vertex.
     * If the vertex already exists, nothing is inserted. 
     * 
     * @param vertex The new vertex to be added
     */
    public void addVertex (T vertex){
        if (!vertices.contains(vertex)) {//if not already in vertices
            vertices.add(vertex);
            count++;
            arcs.add(new LinkedList<T>());//create a new LinkedList for its corresponding arcs
        }
    }

    /** 
     * Removes a single vertex with the given value from this graph.
     * If the vertex does not exist, it does not change the graph. 
     * 
     * @param vertex The vertex to be removed
     */
    public void removeVertex (T vertex) {
        if (vertices.contains(vertex)) {
            LinkedList<T> verticesList = arcs.get(vertices.indexOf(vertex)); //finds vertices connected to vertex
            for(T vtx:verticesList){ //remove all edges conatining the vertex
                removeEdge(vtx,vertex); 
            }
            vertices.remove(vertex); 
            arcs.remove(verticesList);
        }
    }

    /** 
     * Inserts an arc between two vertices of this graph,
     * if the vertices exist. Else it does not change the graph. 
     * 
     * @param vertex1 The first vertex of the arc you want to add
     * @param vertex2 The seoncd vertext of the arc you want to add 
     */
    public void addArc (T vertex1, T vertex2) {
        if (vertices.contains(vertex1) && vertices.contains(vertex2) &&
        !isArc(vertex1, vertex2)) {
            int index1 = vertices.indexOf(vertex1);
            arcs.get(index1).add(vertex2);//adds vertex2 to the linkedList of vertex1
        }
    }

    /** 
     * Removes an arc between two vertices of this graph,
     * if the vertices exist. Else it does not change the graph. 
     * 
     * @param vertex1 The first vertex of the arc you want to remove
     * @param vertex2 The seoncd vertext of the arc you want to remove 
     */
    public void removeArc (T vertex1, T vertex2) {
        if (isArc(vertex1, vertex2)) {
            int index1 = vertices.indexOf(vertex1);
            arcs.get(index1).remove(vertex2);//removes vertex2 from the linkedList of vertex1
        }
    }

    /** 
     * Inserts an edge between two vertices of this graph,if the vertices exist. 
     * Else does not change the graph.
     * 
     * @param vertex1 The first vertex of the edge you want to add
     * @param vertex2 The seoncd vertext of the edge you want to add 
     */
    public void addEdge (T vertex1, T vertex2) {
        addArc(vertex1, vertex2);
        addArc(vertex2, vertex1);
    }

    /**
     * Removes an edge between two vertices of this graph, if the vertices exist, 
     * else does not change the graph. 
     * 
     * @param vertex1 The first vertex of the edge you want to remove
     * @param vertex2 The seoncd vertext of the edge you want to remove 
     */
    public void removeEdge (T vertex1, T vertex2) {
        removeArc(vertex1, vertex2);
        removeArc(vertex2, vertex1);
    }

    /** 
     * Saves the current graph into a .tgf file. 
     * If it cannot write the file, a message is printed. 
     * 
     * @param tgf_file_name The file name of the tgf file
     */
    public void saveTGF(String tgf_file_name) {
        try {
            PrintWriter writer = new PrintWriter(new File(tgf_file_name));
            //notice that indexing in the tgf format starts at 1 (not 0)

            //write vertices by iterating through vector "vertices"
            for (int i = 1; i <= vertices.size(); i++) {
                writer.print((i) + " " + vertices.get(i-1));
                writer.println("");
            }
            writer.println("#"); // # symbol separates the vertices from the arcs

            //write arcs by iterating through arcs vector
            int destinationIndex;
            for (int i = 0; i < arcs.size(); i++){ //for each adjacent list
                for (int j = 0; j<arcs.get(i).size(); j++) { //for each destination vertex in that list
                    destinationIndex = vertices.indexOf(arcs.get(i).get(j)); //find the index of that vertex
                    writer.println((i+1) + " " + (destinationIndex+1));
                }
            }
            writer.close(); 
        } catch (IOException ex) {
            System.out.println("***ERROR***" +  tgf_file_name + " could not be written");
        }
    }

    /**
     * Finds the actors that have acted in the movie
     * 
     * @param movie The movie name 
     * @return A list of actors that have acted in the move
     */
    public LinkedList<T> findActors(T movie){
        int index = vertices.indexOf(movie); 
        return arcs.get(index);
    }

    /**
     * Finds the movies that an actor has acted in
     * 
     * @param actor The ator name
     * @return A list of movies that an actor has acted in
     */
    public LinkedList<T> findMovies(T actor){
        int index = vertices.indexOf(actor);
        return arcs.get(index);
    }

    /**
     * Helper method to create a new path when adding a new vertex
     * 
     * @param originalPath The original path
     * @param vertex The new vertex that wants to be added to the original path  
     * 
     * @return path The new path with the new vertext added to the end
     */
    private ArrayList<Integer> createNewPath(ArrayList originalPath, int vertex){
        ArrayList<Integer> path = new ArrayList(); 
        path = (ArrayList<Integer>)originalPath.clone(); //clone the original path
        path.add(vertex); 
        return path;
    }

    /**
     * Finds the degree of separation between two actors using BFS. For example, if the actors 
     * have played in a movie together, their separation number would be 0. If the actors 
     * have not played in a movie together, but  a1 played in a movie m1 with another 
     * actor a, who played in a movie m2 with a2, then this number would be 1.
     * 
     * @param actor1 The name of the first actor
     * @param actor2 The name of the second actor
     * 
     * @return The degree of separation between two actors 
     */
    public int findSeparation(String actor1,String actor2){
        int a1 = vertices.indexOf(actor1);
        int a2 = vertices.indexOf(actor2);

        if(a1 == -1||a2 == -1){
            System.out.println("Actors not found.");
            return -1;
        }
        else{
            boolean[] visited = new boolean[vertices.size()]; //to check if a vertext has been visited
            visited[a1] = true;
            boolean found = false;

            //using arrayList to store path
            ArrayQueue<ArrayList<Integer>> pathQueue = new ArrayQueue<ArrayList<Integer>>(); //vertices are stored as corresponding index
            ArrayList<Integer> path = new ArrayList<Integer>(); 
            pathQueue.enqueue(createNewPath(path,a1)); //enqueue path a1 to begin 

            while(!pathQueue.isEmpty()){
                ArrayList<Integer> currentPath = pathQueue.dequeue();
                int vertex = currentPath.get(currentPath.size()-1); //gets the last vertex
                LinkedList<T> neighbors = arcs.get(vertex); //gets its neighbor vertices

                //forms extended paths and enqueue them
                for (int i = 0; i < neighbors.size(); i++) { //iterates through the neighbours
                    int number = vertices.indexOf(neighbors.get(i)); //gets the index of the neighbour vertex
                    if(!visited[number]){
                        pathQueue.enqueue(createNewPath(currentPath, number));//enqueue the extended path
                        visited[number] = true; 
                        if(number == a2) { 
                            found = true;
                            break; //breaks through the for loop                    
                        }
                    }
                } 
                if(found)break; //breaks through the while loop
            }
            ArrayList<Integer> foundPath = pathQueue.getLast();//method in the API so we defined it in ArrayList 

            int pathSize = foundPath.size();
            int separation  = (pathSize-3)/2;

            return separation;
        }
    }

    /**
     * Generates a new Bechdel test that asks the user for a percentage, and checks whether
     * the movies have over that percentage of women in its cast. 
     * 
     * @return A list of movies that passed the new Bechdel test
     */
    public LinkedList findNewBechdelValue(){
        //Asks for a double value that is converted to a percentage decimal format
        Scanner scan = new Scanner(System.in);
        System.out.println("Input the % of women in the cast that passes the test (ex. 48, 23): ");
        double percentage = (scan.nextDouble())/100;
        LinkedList<String> passedMovies = new LinkedList<String>();
        LinkedList<String> failedMovies = new LinkedList<String>();
        //Iterates through movies with female actors
        for(String movie : femaleMovies.keySet()){
            //Finding the number of femaleActors and totalActors in a movie
            int femaleActors = femaleMovies.get(movie);
            int totalActors = totalMovies.get(movie);
            double wPercent = (double)femaleActors/totalActors;
            if (wPercent >= percentage){
                passedMovies.add(movie); //If percentage of femaleActors is above percentage, adds movie to passedMovies
            }
            else{
                failedMovies.add(movie); //If percentage of femaleActors is above percentage, adds movie to passedMovies
            }
        }
        return passedMovies; 
    }
    /**
     *  Returns a string representation of the graph.
     *
     *  @return String a string representation of this graph
     */
    public String toString() {
        if (vertices.size() == 0) return "Graph is empty";
        String result = "Vertices: \n";
        result = result + vertices;
        result = result + "\n\nArcs: \n";
        for (int i=0; i< vertices.size(); i++){
            result = result + "from " + vertices.get(i) + ": "  + arcs.get(i) + "\n";
        }
        return result;
    }

    /**
     * Main method for testing 
     */
    public static void main (String args[]) {
        //Testing for task 1
        HollywoodGraph dummy1 = new HollywoodGraph("small_castGender.txt");
        System.out.println("The original adjacency list graph.");
        System.out.println("Number of vertices: "+dummy1.getNumVertices());
        System.out.println("Number of arcs: "+dummy1.getNumArcs());
        System.out.println(dummy1);

        System.out.println("\nThe adjacency list graph after adding new vertex Aileen and an edge between Aileen and Beta.");
        dummy1.addVertex("Aileen");
        dummy1.addEdge("Aileen", "Beta");
        System.out.println(dummy1);

        System.out.println("Expected: false. Result: "+dummy1.isEmpty());
        System.out.println("Expected: true. Result: "+ dummy1.isEdge("Aileen","Beta"));
        dummy1.removeEdge("Aileen","Beta");
        System.out.println("Expected: false. Result: "+ dummy1.isEdge("Aileen","Beta"));
        dummy1.removeVertex("Stella");
        System.out.println("\nThe adjacency list graph after removing Stella and the edge between Aileen and Beta."); 
        System.out.println(dummy1);

        //Testing for task 2
        HollywoodGraph dummy2 = new HollywoodGraph("small_castGender.txt");
        dummy2.saveTGF("small.tgf");
        System.out.println(dummy2);

        System.out.println("Expected seprataion is 0. Result: " + dummy2.findSeparation("Tyler Perry","Cassi Davis"));
        System.out.println("Expected seprataion is 1. Result: " + dummy2.findSeparation("Stella","Takis"));
        System.out.println("Expected separation is -1, means Actors not found. Result: "+dummy2.findSeparation("Aaron Ly","Lori Cline"));

        System.out.println("Movies that passed the new Bechdel Test: "+dummy2.findNewBechdelValue());

        HollywoodGraph dummy3 = new HollywoodGraph("nextBechdel_castGender.txt");
        dummy3.saveTGF("HollywoodGraph.tgf");

        System.out.println("\nThe movies that Jennifer Lawrence has acted in: "+dummy3.findMovies("Jennifer Lawrence")); 
        System.out.println("\nThe actors in The Jungle Book: "+dummy3.findActors("The Jungle Book")); 

        System.out.println("\nThe separation between Megan Fox and Tyler Perry is: " + dummy3.findSeparation("Megan Fox","Tyler Perry"));
        System.out.println("The separation between Nick Arapoglou and Tyler Perry is: " + dummy3.findSeparation("Nick Arapoglou","Tyler Perry"));

        System.out.println("\nExpected seprataion is 0. Result: " + dummy3.findSeparation("Lori Cline","Aida Manassy"));
        System.out.println("Expected seprataion is 1. Result: " + dummy3.findSeparation("Aaron Ly","Coburn Goss"));
        System.out.println("Expected separation is 2. Result: "+dummy3.findSeparation("Henry Lloyd-Hughes","Arinze Kene"));
        System.out.println("Expected separation is 3. Result: "+dummy3.findSeparation("Aaron Ly","Willow Nash"));
        System.out.println("Expected separation is 4. Result: "+dummy3.findSeparation("Aaron Ly","Owen Walters"));

        System.out.println("Movies that passed the new Bechdel Test: "+dummy3.findNewBechdelValue());
    }
}
