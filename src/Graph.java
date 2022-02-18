import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

//graph representation
public class Graph{
	int nNode;
	
	List<ArrayList<Edge>> adj=new ArrayList<ArrayList<Edge>>();
	int[] level;
	public Graph(int nNode) {
		this.nNode=nNode;
		
		for(int i=0;i<nNode;i++ ) {
			adj.add(new ArrayList<Edge>());
		}
		level=new int[nNode];

	}
	//add edge
	public void addEdge(int u,int v, int capacity) {//add edge from u to v
		Edge e1=new Edge(u,v,capacity,adj.get(v).size());
		Edge e2=new Edge(v,u,0,adj.get(u).size());	
		adj.get(u).add(e1);
		adj.get(v).add(e2);
		
	}

	//bfs to create levels in graph
	public boolean BFS() {
		
		
		Queue<Integer> bfsQueue=new LinkedList<Integer>();
		
		for(int i=0;i<nNode;i++) {
			level[i]=-1;
		}
		//add source to queue
		bfsQueue.add(0);
		level[0]=0;
		
		while(!bfsQueue.isEmpty()) {
			
			int node=bfsQueue.poll();	
			
			for(Edge edge:adj.get(node)) {
				
				if(level[edge.destination]==-1&&edge.flow<edge.capacity) {
					level[edge.destination]=level[node]+1;
					bfsQueue.add(edge.destination);	
				}
				
			}
		
		}
		//return false if sink is not reachable
		return level[nNode-1]!=-1;
	}
	//dfs to send flow on level graph
	public long DFS(int u,  int t,long flow, int[] next) {
		
		if (u==t) {
			return flow;
		}
		for(; next[u]<adj.get(u).size();next[u]++) {
			Edge edge =adj.get(u).get(next[u]);
		
			if(level[edge.destination]==level[edge.source]+1 && edge.flow<edge.capacity) {
				
				long tmpFlow=DFS(edge.destination,t,Math.min(flow, edge.capacity-edge.flow),next);
				if(tmpFlow>0) {
				
					edge.flow+=tmpFlow;
					adj.get(edge.destination).get(edge.rev).flow-=tmpFlow;
					
					return tmpFlow;
				}
				
				
			}
	
		}
		return 0;
	}
	//dinic algo to find max flow
	public int Dinic() {
		int result=0;
		int[] next= new int[nNode];
		while(BFS()) {
			Arrays.fill(next,0);
			while(true) {
				long flow=DFS(0,nNode-1,Integer.MAX_VALUE,next);
				if(flow==0 ) {
					break;
				}
				result+=flow;	
			}
		}
		return result;
	}
	public void printGraph() {
		for(ArrayList<Edge> al: adj) {
			for(Edge edge :al) {
				System.out.println(edge.source+"-->"+edge.destination+" ["+edge.capacity+"] "+level[edge.source]);
			}
		}
	}
	public void printLevel() {
		for(int i=0;i<this.level.length;i++) {
			System.out.println(i+"  "+level[i]);
		}
	}
	
	

}
//edge structure
class Edge{
	int destination;
	int source;
	int capacity;
	int flow;
	int rev;

	public Edge(int source,int destination,int capacity, int rev) {
		this.source=source;
		this.destination=destination;
		this.capacity=capacity;
		this.flow=0;
		this.rev=rev;
		
	}
	

	
	
	
}




