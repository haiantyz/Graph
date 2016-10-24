import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by tyz on 2016/10/19.
 */
public class Test {
    static Vertice[] vertices = new Vertice[62];
    static Server[] servers = new Server[4];
    static HashSet boundaryVertice = new HashSet();
    static HashSet firstboundary = new HashSet();
    static HashSet secondboundary = new HashSet();
    static HashSet thirdboundary = new HashSet();
    static HashSet fourthboundary = new HashSet();
    static MigrationCost[][] serververticesort = new MigrationCost[4][];
    static HashSet[] serverboundaryvertice = new HashSet[4];


    public static StringBuilder readFromFile(String filename) throws IOException {
        StringBuilder s = new StringBuilder();
        BufferedInputStream bufferedinput = null;
        byte[] buffer = new byte[1024];
        try{
            bufferedinput = new BufferedInputStream(new FileInputStream(filename));
            int bytesRead = 0;
            while((bytesRead = bufferedinput.read(buffer))!=-1){
                String chunk = new String(buffer,0,bytesRead);
                s.append(chunk);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bufferedinput != null)
                bufferedinput.close();

        }
        return s;
    }
    public static void getVerticeNeighbor(StringBuilder stringBuilder) throws IOException {
        String[] line = readFromFile(stringBuilder.toString()).toString().split("\n");
        String[] sourceId = new String[line.length];
        String[] targetId = new String[line.length];
        for (int i = 0;i<line.length;i++){
            String[] line33 = line[i].split(" ");
            sourceId[i] = line33[0];
            targetId[i] = line33[1];
            NeighborVertice neighborVertice = new NeighborVertice(Integer.parseInt(targetId[i]),1);
            vertices[Integer.parseInt(sourceId[i])-1].neighbor.add(neighborVertice);
            NeighborVertice neighborVertice1 = new NeighborVertice(Integer.parseInt(sourceId[i]),1);
            vertices[Integer.parseInt(targetId[i])-1].neighbor.add(neighborVertice1);
        }
    }
    public static void ServerGetVertice(StringBuilder stringBuilder) throws IOException {
        String[] verticeserverid = readFromFile(stringBuilder.toString()).toString().split("\n");
        for (int i=0;i<4;i++){
            servers[i] = new Server();
        }
        for (int i=0;i<verticeserverid.length;i++){
            servers[Integer.parseInt(verticeserverid[i])].verticeServerSet.add(vertices[i]);
            vertices[i].setServerId(Integer.parseInt(verticeserverid[i]));

        }

    }
    public static MigrationCost GetVerticeGain(int verticeId){
        Iterator iterator = vertices[verticeId].neighbor.iterator();
        HashSet[] bod = new HashSet[4];
        int targetid=0;
        for (int i=0;i<4;i++){
            bod[i] = new HashSet();
        }
        while (iterator.hasNext()){
            NeighborVertice vertice = (NeighborVertice) iterator.next();
            bod[vertices[vertice.getNeighborverticeId()-1].getServerId()].add(vertice);
        }
        int[] gain = new int[4];
        for (int i = 0;i<4;i++){
            gain[i] = bod[i].size() - bod[vertices[verticeId].getServerId()].size();
            //System.out.println(gain[i]);
        }
        Arrays.sort(gain);
        for (int i = 0;i<4;i++){
            if (gain[3]==bod[i].size() - bod[vertices[verticeId].getServerId()].size()){
                targetid = i;
                break;
            }
        }
        return new MigrationCost(gain[3],targetid,verticeId);
    }
    public static MigrationCost GetBalanceVertice(int verticeId){
        Iterator iterator = vertices[verticeId].neighbor.iterator();
        HashSet[] bod = new HashSet[4];
        int targetid=0;
        for (int i=0;i<4;i++){
            bod[i] = new HashSet();
        }
        while (iterator.hasNext()){
            NeighborVertice vertice = (NeighborVertice) iterator.next();
            bod[vertices[vertice.getNeighborverticeId()-1].getServerId()].add(vertice);
        }
        int[] gain = new int[4];
        for (int i = 0;i<4;i++){
            gain[i] =Math.abs(bod[i].size() - bod[vertices[verticeId].getServerId()].size());
            //System.out.println(gain[i]);
        }
        Arrays.sort(gain);
        for (int i = 0;i<4;i++){
            if (gain[1]==Math.abs(bod[i].size() - bod[vertices[verticeId].getServerId()].size())){
                targetid = i;
                break;
            }
        }
        return new MigrationCost(gain[1],targetid,verticeId);
    }




    public static void GetBoundaryVertice(int verticeId){
            Iterator iterator33 = vertices[verticeId].neighbor.iterator();
            while (iterator33.hasNext()){
                NeighborVertice neighborVertice;
                neighborVertice = (NeighborVertice) iterator33.next();
                if (vertices[verticeId].getServerId() != vertices[neighborVertice.getNeighborverticeId()-1].getServerId()){
                    vertices[verticeId].setIs_boundaryV(true);
                    boundaryVertice.add(vertices[verticeId]);

                }

            }

    }

    public static void GetEachBoundaryVertice(){

        Iterator iterator1 = boundaryVertice.iterator();
        while (iterator1.hasNext()){
            Vertice vertice = (Vertice) iterator1.next();
            if (vertice.getServerId()==0)
                firstboundary.add(vertice);
            else if (vertice.getServerId()==1)
                secondboundary.add(vertice);
            else if (vertice.getServerId()==2)
                thirdboundary.add(vertice);
            else
                fourthboundary.add(vertice);
        }

    }
    public static void firstServerBoundarySort(int balancenumber){

        int i=0;
        MigrationCost[] migrationCost = new MigrationCost[firstboundary.size()];
        Iterator it = firstboundary.iterator();
        while (it.hasNext()){
            Vertice vertice = (Vertice) it.next();
            migrationCost[i] = GetBalanceVertice(vertice.getVerticeid()-1);
            i++;
        }
        Arrays.sort(migrationCost,new ByGainComparator());
        //HashSet mm = new HashSet();

//        for (int j=0;j<balancenumber;j++){
//            vertices[migrationCost[j].getId()].setServerId(migrationCost[j].getTargetId());
//        }
        for (int j=0;j<balancenumber;j++){
            vertices[migrationCost[j].getId()].setServerId(migrationCost[j].getTargetId());
        }

    }


    public static void secondServerBoundarySort(int balancenumber){

        int i=0;
        MigrationCost[] migrationCost = new MigrationCost[secondboundary.size()];
        Iterator it = secondboundary.iterator();
        while (it.hasNext()){
            Vertice vertice = (Vertice) it.next();
            migrationCost[i] = GetBalanceVertice(vertice.getVerticeid()-1);
            i++;
        }
        Arrays.sort(migrationCost,new ByGainComparator());
        //HashSet mm = new HashSet();

//        for (int j=0;j<balancenumber;j++){
//            vertices[migrationCost[j].getId()].setServerId(migrationCost[j].getTargetId());
//        }
        for (int j=0;j<balancenumber;j++){
            vertices[migrationCost[j].getId()].setServerId(migrationCost[j].getTargetId());
        }

    }

    public static void thirdServerBoundarySort(int balancenumber){

        int i=0;
        MigrationCost[] migrationCost = new MigrationCost[thirdboundary.size()];
        Iterator it = thirdboundary.iterator();
        while (it.hasNext()){
            Vertice vertice = (Vertice) it.next();
            migrationCost[i] = GetBalanceVertice(vertice.getVerticeid()-1);
            i++;
        }
        Arrays.sort(migrationCost,new ByGainComparator());
        //HashSet mm = new HashSet();

//        for (int j=0;j<balancenumber;j++){
//            vertices[migrationCost[j].getId()].setServerId(migrationCost[j].getTargetId());
//        }
        for (int j=0;j<balancenumber;j++){
            vertices[migrationCost[j].getId()].setServerId(migrationCost[j].getTargetId());
        }

    }


    public static void fourthServerBoundarySort(int balancenumber){

        int i=0;
        MigrationCost[] migrationCost = new MigrationCost[fourthboundary.size()];
        Iterator it = fourthboundary.iterator();
        while (it.hasNext()){
            Vertice vertice = (Vertice) it.next();
            migrationCost[i] = GetBalanceVertice(vertice.getVerticeid()-1);
            i++;
        }
        Arrays.sort(migrationCost,new ByGainComparator());
        //HashSet mm = new HashSet();

//        for (int j=0;j<balancenumber;j++){
//            vertices[migrationCost[j].getId()].setServerId(migrationCost[j].getTargetId());
//        }
        for (int j=0;j<balancenumber;j++){
            vertices[migrationCost[j].getId()].setServerId(migrationCost[j].getTargetId());
        }

    }
    public static void MigrationVertice(int verticeId){
        MigrationCost migrationcost = GetVerticeGain(verticeId);
        vertices[verticeId].setServerId(migrationcost.getTargetId());
    }

    public static void CounterCommunication(StringBuilder stringBuilder) throws IOException {
        String[] line = readFromFile(stringBuilder.toString()).toString().split("\n");
        String[] sourceId = new String[line.length];
        String[] targetId = new String[line.length];
        for (int i=0;i<line.length;i++){
            String[] line33 = line[i].split(" ");
            sourceId[i] = line33[0];
            targetId[i] = line33[1];
            if (vertices[Integer.parseInt(sourceId[i])-1].getServerId()!=vertices[Integer.parseInt(targetId[i])-1].getServerId())
                System.out.println(1);
        }

    }

    public static void Planar(){

    }
    public static void main(String args[]) throws IOException {
        StringBuilder s = new StringBuilder("/Users/tyz/IdeaProjects/TGraph/src/data/dolphin");
        for (int i=0;i<62;i++){
            vertices[i] = new Vertice(i+1," ");

        }
        getVerticeNeighbor(s);
//        for (int i=0;i<62;i++){
//            Iterator iterator = vertices[i].neighbor.iterator();
//            NeighborVertice neighborvertice ;
//
//            while (iterator.hasNext()){
//                neighborvertice = (NeighborVertice) iterator.next();
//                System.out.print(neighborvertice.getNeighborverticeId()+" ");
//            }
//            System.out.println("");
//        }
        StringBuilder s2 = new StringBuilder("/Users/tyz/IdeaProjects/TGraph/src/data/repart");
        ServerGetVertice(s2);
        CounterCommunication(s);




//
//        for (int i=0;i<62;i++){
//        GetBoundaryVertice(i);
//        }
//        Iterator iterator = boundaryVertice.iterator();
//        while (iterator.hasNext()){
//            Vertice vertice = (Vertice) iterator.next();
//            MigrationVertice(vertice.getVerticeid()-1);
//        }
//        for (int i=0;i<62;i++){
//            GetBoundaryVertice(i);
//
//        }
////        Iterator iterator1 = boundaryVertice.iterator();
////        while (iterator1.hasNext()){
////            Vertice vertice = (Vertice) iterator1.next();
////            if (vertice.getServerId()==0)
////                firstboundary.add(vertice);
////            else if (vertice.getServerId()==1)
////                secondboundary.add(vertice);
////            else if (vertice.getServerId()==2)
////                thirdboundary.add(vertice);
////            else
////                fourthboundary.add(vertice);
////        }
//        GetEachBoundaryVertice();
//        System.out.println(boundaryVertice.size());
//        System.out.println(firstboundary.size());
//        System.out.println(secondboundary.size());
//        System.out.println(thirdboundary.size());
//        System.out.println(fourthboundary.size());
//        firstServerBoundarySort(4);
//        fourthServerBoundarySort(4);
//        for (int i=0;i<62;i++){
//            System.out.println(vertices[i].getServerId());
//        }
    }
}
