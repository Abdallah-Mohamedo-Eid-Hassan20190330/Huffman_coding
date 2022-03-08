import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Huffman {

    public static double ratio= 0;
    public static int n=0;

    public static class Node {
        int freq ;
        char value ;
        Node left ;
        Node right ;

    }

    static class CompareFreq implements Comparator<Node> // copied from the Internet ;
    {
        public int compare(Node x,Node y)
        {
            return x.freq - y.freq;
        }
    }
    // end of the copied section ;

    public static void write (Node root , String s , FileWriter fw)
    {
        if (root==null) return;
        if(root.right==null && root.left==null)
        {
            ratio+= root.freq * s.length() ;
            n+= root.freq;
            //System.out.println(root.value + '\t' + s);//////////////////////
            try {
                fw.write(root.value + "   " + s + '\n'+ "   ");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        write (root.left , s+"0" , fw ) ;
        write (root.right, s+"1" , fw ) ;
    }



    public static void main(String[] args) {
        // TODO Auto-generated method stub

        File doc = new File("doc.txt");

        File comp = new File("dictionary.txt") ;


        try (FileReader fr = new FileReader(doc) ) //create file reader object to read from doc file
        {
            FileWriter fwc = new FileWriter("dictionary.txt");

            comp.createNewFile();
            int c;
            int freq[] = new int [256] ;
            for (int i = 0 ; i < freq.length ; i ++) freq[i] = 0 ;

            while ( (c= fr.read()) != -1 ) freq[c]++;


            PriorityQueue <Node> q = new PriorityQueue<>(256, new CompareFreq());
            for(int i=0; i<256; i++)
            {
                int num=freq[i];
                if(i==10)  num--;
                if(num>0)
                {
                    Node nd=new Node();
                    nd.freq= num;
                    nd.value= (char) i ;
                    nd.right= null;
                    nd.left= null;
                    q.add(nd);
                }
            }
            Node root=null;

            while( q.size() > 1)
            {
                Node left = q.poll() ;
                Node right = q.poll() ;
                Node parent = new Node () ;
                parent.freq = left.freq + right.freq ;
                parent.value= '-';
                parent.left=left;
                parent.right=right;
                root = parent;
                q.add(parent);
            }



            write (root , "",  fwc);

            fwc.close();
            String dictionaryData = "" ;
            File reader = new File (System.getProperty("user.dir") + "\\" +"dictionary.txt");
            Scanner scan = new Scanner (reader) ;
            while (scan.hasNextLine())
            {
                dictionaryData = dictionaryData.concat(scan.nextLine()) ;
            }
            System.out.println ("Dictionary: " + dictionaryData) ;

            String arr[] = dictionaryData.split("   ");
            // test the array ;
//          for (int i = 0 ; i < arr.length ; i++)
//          {
//        	  System.out.println(arr[i]) ; 
//          }
            HashMap <String , String > map = new HashMap <> () ;
            for (int i = 0 ; i < arr.length ; i += 2 )
            {
                map.put(arr[i], arr[i+1]) ;
            }
            System.out.println("char=its_code: " + map) ;

            String source = "" ;
            File readSource = new File (System.getProperty("user.dir") + "\\" + "doc.txt") ;
            Scanner scan2 = new Scanner (readSource) ;
            source = scan2.nextLine() ;
            System.out.println("Before compression: "+source) ;
            String sourceArr [] = source.split("") ;
            String compressed ="" ;
            String compressed2 = "" ;
            //System.out.println (map.get("a")) ;
            HashMap <String , String > mapDecomp = new HashMap <>() ;

            for (int i = 0 ; i < source.length() ; i ++ )
            {
                // String a = source[i] ;
                compressed = compressed.concat(map.get(sourceArr[i]))  ;
                compressed2 = compressed2.concat(map.get(sourceArr[i]) + "-") ;
                mapDecomp.put(map.get(sourceArr[i]), sourceArr[i]) ;

            }
            System.out.println("After compression: " + compressed2) ;
            //System.out.println(compressed);

            String compressedArr[] = compressed2.split("-") ;
            String decompress = "" ;

            // do for loop after prey ;
            for (int i = 0 ; i < compressedArr.length ; i ++ )
            {
                decompress = decompress.concat(mapDecomp.get(compressedArr[i]))  ;
            }
            File decompFile = new File ("NewFolder/decomp.text") ;
            decompFile.createNewFile();
            FileWriter decompressed = new FileWriter ("NewFolder/decomp.text") ;
            decompressed.write(decompress) ;
            decompressed.close() ;


            FileWriter output = new FileWriter ("compressed.txt") ;
            output.write(compressed);
            output.close();
            ratio= ratio/(n*8);
            System.out.println("ratio: "+ ratio);
        }catch (IOException e) {
            System.out.println("Error") ;
            e.printStackTrace();
            System.out.println(e);
        }

        System.out.println("finished");
    }

}