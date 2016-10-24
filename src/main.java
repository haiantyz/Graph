import sun.security.x509.SerialNumber;

import java.io.*;
import java.util.Iterator;

/**
 * Created by tyz on 2016/10/11.
 */
public class main {
    //这边需要设置是多少个服务器
    static int ServerNumber = 8;
    static Server[] servers = new Server[ServerNumber];
    static int VerticeNumber=16726,EdgeNumber=47594;



    static String[] verticeLabel = new String[VerticeNumber];
    static Vertice[] vertices = new Vertice[VerticeNumber];
    static int[] edgeWeight = new int[EdgeNumber];
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





    public static void loaddatatovertice(StringBuilder stringBuilder) throws IOException {


        String[] line = readFromFile(stringBuilder.toString()).toString().split("\n");
        int startlinenumber = 7;
        for(int i = 0;i<16726;i++){
            verticeLabel[i] = line[startlinenumber];
            startlinenumber+=5;
        }

        //生成点
        for (int i=0;i<16726;i++){
            vertices[i] = new Vertice(i+1,verticeLabel[i]);
        }


    }

    public static void getEdgeWeight(StringBuilder stringBuilder) throws IOException {
        String[] line = readFromFile(stringBuilder.toString()).toString().split("\n");
        int start = 0;
        for (int i =4;i<line.length;i+=6){
            String[] valueLine = line[i].split("_value_");
            edgeWeight[start] = valueLine[1].length();
            start++;
        }


    }




    public static void getVerticeNeighbor(StringBuilder stringBuilder) throws IOException {
        String[] line = readFromFile(stringBuilder.toString()).toString().split("\n");
        String[] sourceId = new String[line.length];
        String[] targetId = new String[line.length];
        for (int i = 0;i<line.length;i++){
            String[] line33 = line[i].split(" ");
            sourceId[i] = line33[0];
            targetId[i] = line33[1];
            NeighborVertice neighborVertice = new NeighborVertice(Integer.parseInt(targetId[i]),edgeWeight[i]);
            vertices[Integer.parseInt(sourceId[i])-1].neighbor.add(neighborVertice);
            NeighborVertice neighborVertice1 = new NeighborVertice(Integer.parseInt(sourceId[i]),edgeWeight[i]);
            vertices[Integer.parseInt(targetId[i])-1].neighbor.add(neighborVertice1);
        }
    }

    public static void writefile(String filename){
        BufferedOutputStream bufferedOutputStream = null;

        try{
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filename));
            for (int i = 0;i<vertices.length;i++){
                Iterator iterator = vertices[i].neighbor.iterator();
                String verticeweight = String.valueOf(vertices[i].getWeight());
                bufferedOutputStream.write(verticeweight.getBytes());
                bufferedOutputStream.write(" ".getBytes());
                while (iterator.hasNext()){
                    NeighborVertice neighborVertice = new NeighborVertice();
                    neighborVertice = (NeighborVertice) iterator.next();
                    String sneighborID = String.valueOf(neighborVertice.getNeighborverticeId());
                    bufferedOutputStream.write(sneighborID.getBytes());
                    bufferedOutputStream.write(" ".getBytes());
                    String sedgeweight = String.valueOf(neighborVertice.getEdgeWeight());
                    bufferedOutputStream.write(sedgeweight.getBytes());
                    bufferedOutputStream.write(" ".getBytes());

                }
                bufferedOutputStream.write("\n".getBytes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            if(bufferedOutputStream!=null){
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ServerGetVertice(StringBuilder stringBuilder) throws IOException {
        String[] verticeserverid = readFromFile(stringBuilder.toString()).toString().split("\n");
        for (int i=0;i<ServerNumber;i++){
            servers[i] = new Server();
        }
        for (int i=0;i<verticeserverid.length;i++){
            servers[Integer.parseInt(verticeserverid[i])].verticeServerSet.add(vertices[i]);
        }

    }


    public static void main(String args[]) throws IOException {
//        StringBuilder stringBuilder = new StringBuilder("/Users/tyz/IdeaProjects/TGraph/src/expri.data");
//        StringBuilder stringBuilder2 = new StringBuilder("/Users/tyz/IdeaProjects/TGraph/src/con-matedge.graph");
//        StringBuilder stringBuilder1 = new StringBuilder("/Users/tyz/IdeaProjects/TGraph/src/EdgeWeight");
//        StringBuilder stringBuilder3 = new StringBuilder("/Users/tyz/IdeaProjects/TGraph/src/Serverdata");
//        loaddatatovertice(stringBuilder);
//        getEdgeWeight(stringBuilder1);
//        getVerticeNeighbor(stringBuilder2);
//        ServerGetVertice(stringBuilder3);


    }


}
