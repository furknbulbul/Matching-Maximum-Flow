import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;



public class project4main {
	public static void main(String[] args) throws IOException  { 
		
		 String fileName = args[0];
	     ArrayList<String> inputLines = new ArrayList<String>();
	     try (FileReader reader = new FileReader(fileName);
	          BufferedReader bufferedReader = new BufferedReader((reader))) {
	         String line;
	         while ((line = bufferedReader.readLine()) != null) {
	             inputLines.add(line);
	         }
	     } catch (IOException e) {
	         e.printStackTrace();
	     }
	     Iterator<String> iterator= inputLines.iterator();
	     String tmp;
	     
	     int nGreenTrain=Integer.parseInt(iterator.next());
	     tmp=iterator.next();
	     String[] greenTrainCapacities=tmp.split(" ");
	     
	     int nRedTrain=Integer.parseInt(iterator.next());
	     tmp=iterator.next();
	     String[] redTrainCapacities=tmp.split(" ");
	     
	     int nGreenReindeer=Integer.parseInt(iterator.next());
	     tmp=iterator.next();
	     String[] greenReindeerCapacities=tmp.split(" ");
	     
	     int nRedReindeer=Integer.parseInt(iterator.next());
	     tmp=iterator.next();
	     String[] redReindeerCapacities=tmp.split(" ");
	     
	     
	     //handle bags
	     int nBags=Integer.parseInt(iterator.next());
	     int nGifts=0;
	     String[] bags;
	     if(nBags!=0) {
	    	 tmp=iterator.next();
	    	 bags=tmp.split(" ");
	     }else  {
	    	  bags= new String[0];
	     }

	     //merge non-a bags
	     bags=mergeNodes(bags);
	     nBags=bags.length/2;
	     String[] bagTypes=new String[nBags];
	     int[] bagCap=new int[nBags];
	
	     int totalVertices=nGreenReindeer+nRedReindeer+nRedTrain+nGreenTrain+nBags+2;
	     Graph graph=new Graph(totalVertices);
	     
	     //update graph from source to bags
	     for(int i=0;i<nBags;i++) {
	    	 bagTypes[i]=bags[2*i];
	    	 bagCap[i]=Integer.parseInt(bags[2*i+1]);
	    	 nGifts+=bagCap[i];
	    	 
	    	 graph.addEdge(0,i+1,Integer.parseInt(bags[2*i+1]));//add edges from source to bags
	     }
	     //update graph from vehicles to sink
	     //green trains 
	     for(int i=0;i<nGreenTrain;i++) {
	    	 
	    	 graph.addEdge(nBags+1+i,totalVertices-1,Integer.parseInt(greenTrainCapacities[i]));
	     }
	     //red trains
	     for(int i=0;i<nRedTrain;i++) {
	    	  
	    	 graph.addEdge(nBags+nGreenTrain+1+i,totalVertices-1,Integer.parseInt(redTrainCapacities[i]));
	     }
	     //green reindeer 
	     for(int i=0;i<nGreenReindeer;i++) {
	    	 
	    	 graph.addEdge(nBags+nGreenTrain+nRedTrain+1+i,totalVertices-1,Integer.parseInt(greenReindeerCapacities[i]));
	     }
	     //red reindeer
	     for(int i=0;i<nRedReindeer;i++) {
	    	 
	    	 graph.addEdge(nBags+nGreenTrain+nRedTrain+nGreenReindeer+1+i,totalVertices-1,Integer.parseInt(redReindeerCapacities[i]));
	     }
	     //creates edges between bags and vehicles
	     findEdges(bagTypes,bagCap,graph,nBags,nGreenTrain,nRedTrain,nGreenReindeer,nRedReindeer);
	     
	     
	     int maxFlow=graph.Dinic();
	     try { 
		        File outputFile=new File(args[1]);
		        if(!outputFile.exists()) {
		        	outputFile.createNewFile();
		        }
		        PrintStream output = new PrintStream(args[1]);
		        output.print(nGifts-maxFlow);
		        output.close();
	     }
	     catch(Exception e) {
	    	 e.getStackTrace();
	     }
	     

	     
	}
	
	
	//find edges from bags to vehicles
	static void findEdges(String[] bagArr,int[] bagCap, Graph graph, int nBags, int nGreenTrain, int nRedTrain, int nGreenReindeer, int nRedReindeer) {
		int indexStartGreenTrain=nBags+1;int indexStartRedTrain=indexStartGreenTrain+nGreenTrain;
		int indexStartGreenReindeer=indexStartRedTrain+nRedTrain;int indexStartRedReindeer=indexStartGreenReindeer+nGreenReindeer;
		
		for(int i=0;i<nBags;i++) {
			String str=bagArr[i];
			int cap=bagCap[i];
			
			if(str.equals("a")) {
				for(int j=indexStartGreenTrain;j<graph.nNode-1;j++) {
					graph.addEdge(i+1,j,1);
				}
				
			}
			if(str.equals("ab")) {
				for(int j=indexStartGreenTrain;j<indexStartRedTrain;j++) {
					graph.addEdge(i+1,j,1);
				}
				for(int j=indexStartGreenReindeer;j<indexStartRedReindeer;j++) {
					graph.addEdge(i+1,j,1);
				}
				
			}
			if(str.equals("ac")) {
				for(int j=indexStartRedTrain;j<indexStartGreenReindeer;j++) {
					graph.addEdge(i+1,j,1);
				}
				for(int j=indexStartRedReindeer;j<indexStartRedReindeer+nRedReindeer;j++) {
					graph.addEdge(i+1,j,1);
				}
				
			}
			if(str.equals("ad")) {
				for(int j=indexStartGreenTrain;j<indexStartGreenTrain+nGreenTrain+nRedTrain;j++) {
					graph.addEdge(i+1,j,1);
				}
				
			}
			if(str.equals("ae")) {
				for(int j=indexStartGreenReindeer;j<indexStartGreenReindeer+nGreenReindeer+nRedReindeer;j++) {
					graph.addEdge(i+1,j,1);
				}
			}
			if(str.equals("abd")) {
				for(int j=indexStartGreenTrain;j<indexStartRedTrain;j++) {
					graph.addEdge(i+1,j,1);
				}
				
			}
			if(str.equals("abe")) {
				for(int j=indexStartGreenReindeer;j<indexStartRedReindeer;j++) {
					graph.addEdge(i+1,j,1);
				}
				
			}
			if(str.equals("acd")) {
				for(int j=indexStartRedTrain;j<indexStartGreenReindeer;j++) {
					graph.addEdge(i+1,j,1);
				}
				
			}
			if(str.equals("ace")) {
				for(int j=indexStartRedReindeer;j<indexStartRedReindeer+nRedReindeer;j++) {
					graph.addEdge(i+1,j,1);
				}
			}
			if(str.equals("b")) {
				for(int j=indexStartGreenTrain;j<indexStartRedTrain;j++) {
					graph.addEdge(i+1,j,cap);
					
				}
				for(int j=indexStartGreenReindeer;j<indexStartRedReindeer;j++) {
					graph.addEdge(i+1,j,cap);
				}
				
			}
			if(str.equals("bd")) {
				
				for(int j=indexStartGreenTrain;j<indexStartRedTrain;j++) {
					graph.addEdge(i+1,j,cap);
				}
			}
			if(str.equals("be")) {
				
				for(int j=indexStartGreenReindeer;j<indexStartRedReindeer;j++) {
					graph.addEdge(i+1,j,cap);
				}
			}
			if(str.equals("c")) {
				
				for(int j=indexStartRedTrain;j<indexStartGreenReindeer;j++) {
					graph.addEdge(i+1,j,cap);
				}
				for(int j=indexStartRedReindeer;j<indexStartRedReindeer+nRedReindeer;j++) {
					graph.addEdge(i+1,j,cap);
				}
			}
			if(str.equals("cd")) {		
				
				for(int j=indexStartRedTrain;j<indexStartGreenReindeer;j++) {
					graph.addEdge(i+1,j,cap);
				}
				
			}
			if(str.equals("ce")) {
				
				for(int j=indexStartRedReindeer;j<indexStartRedReindeer+nRedReindeer;j++) {
					graph.addEdge(i+1,j,cap);
				}
			}
			if(str.equals("d")) {
				for(int j=indexStartGreenTrain;j<indexStartRedTrain;j++) {
					graph.addEdge(i+1,j,cap);
				}
				for(int j=indexStartRedTrain;j<indexStartGreenReindeer;j++) {
					graph.addEdge(i+1,j,cap);
				}
			}
			if(str.equals("e")) {
				for(int j=indexStartGreenReindeer;j<indexStartRedReindeer;j++) {
					graph.addEdge(i+1,j,cap);
				}
				for(int j=indexStartRedReindeer;j<indexStartRedReindeer+nRedReindeer;j++) {
					
					graph.addEdge(i+1,j,cap);
					
				}
			}
		}
		
		
	}
	//merge bags that do not have "a"
	public static String[] mergeNodes(String[] bags ) {
		
		String[] arr= {"b","bd","be","c","cd","ce","d","e"};
		int[]  caps= new int[8];
		Arrays.fill(caps,0);
		
		
		ArrayList<String> al= new ArrayList<String>();
	
	
		for(int i=0;i<bags.length;i+=2) {
	
			String tmp=bags[i];
			
			int cap=Integer.parseInt(bags[i+1]);
			
			if(tmp.equals("b")) {
				
				caps[0]+=cap;
			}
			else if(tmp.equals("bd")) {
				
				caps[1]+=cap;
			}
			else if(tmp.equals("be")) {
				
				caps[2]+=cap;
				
			}
			else if(tmp.equals("c")) {

				caps[3]+=cap;
				
			}
			else if(tmp.equals("cd")) {

				caps[4]+=cap;
				
			}
			else if(tmp.equals("ce")) {

				caps[5]+=cap;
				
			}
			else if(tmp.equals("d")) {

				caps[6]+=cap;
			}
			else if(tmp.equals("e")) {

				caps[7]+=cap;
				
			}
			else {
				al.add(tmp);
				al.add(Integer.toString(cap));
			}
		}
		
		for(int i=0;i<8;i++) {
			al.add(arr[i]);
			al.add(Integer.toString(caps[i]));
		}
		int index=0;String[] resultBags=new String[al.size()];
		for(String s: al) {
			resultBags[index]=s;
			index++;
		}
		return resultBags;
			
	}

	
	
}