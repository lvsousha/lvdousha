package com.lvdousha.graph_database.neo4j;

import java.io.File;
import java.io.IOException;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.Paths.PathDescriptor;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.graphdb.traversal.Uniqueness;
import org.neo4j.io.fs.FileUtils;

public class EmbeddedNeo4j {
	// Embedded Neo4j会在本地产生一个文件夹(类似于Mysql的数据库)
	private static final String DB_PATH = "target/neo4j-hello-db";
	private static EmbeddedNeo4j instance;

	public String greeting;

	// START SNIPPET: vars
	private static GraphDatabaseService graphDb;
	
	
	
	private EmbeddedNeo4j() {
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(DB_PATH));
	}

	public static EmbeddedNeo4j getInstance() {
		if (instance == null) {
			instance = new EmbeddedNeo4j();
		}
		if(graphDb == null){
			graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(DB_PATH));
		}
		return instance;
	}

	public static void main(final String[] args) throws IOException {
		FileUtils.deleteRecursively(new File(DB_PATH));
		EmbeddedNeo4j hello = new EmbeddedNeo4j();
		hello.createNode();
		// 删除数据
//		hello.removeData();
		hello.shutDown();
	}
	
	void createNode() throws IOException {
		registerShutdownHook(graphDb);
		// Embedded Neo4j基本上所有的操作都需要在事务内执行
		try (Transaction tx = graphDb.beginTx()) {
			Index<Node> nodeIndex = graphDb.index().forNodes( "nodes" );
			Node firstNode;
			Node secondNode;
			Node thirdNode;
			Node fourthNode;
			Node fifthNode;
			firstNode = graphDb.createNode();
			firstNode.setProperty("name", "Joe");
			secondNode = graphDb.createNode();
			secondNode.setProperty("name", "Tom");
			thirdNode = graphDb.createNode();
			thirdNode.setProperty("name", "Jerry");
			fourthNode = graphDb.createNode();
			fourthNode.setProperty("name", "Lisa");
			fifthNode = graphDb.createNode();
			fifthNode.setProperty("name", "Kaly");
			
			nodeIndex.add( firstNode, "name", "Joe" );
			nodeIndex.add( secondNode, "name", "Tom" );
			nodeIndex.add( thirdNode, "name", "Jerry" );
			nodeIndex.add( fourthNode, "name", "Lisa" );
			nodeIndex.add( fifthNode, "name", "Kaly" );

			Relationship relationship1 = firstNode.createRelationshipTo(secondNode, RelationshipType.withName("TRANSFER"));
			relationship1.setProperty("amount", 100);
			Relationship relationship2 = secondNode.createRelationshipTo(thirdNode, RelTypes.TRANSFER);
			relationship2.setProperty("amount", 200);
			Relationship relationship3 = thirdNode.createRelationshipTo(fourthNode, RelTypes.TRANSFER);
			relationship3.setProperty("amount", 300);
			Relationship relationship4 = firstNode.createRelationshipTo(fifthNode, RelTypes.TRANSFER);
			relationship4.setProperty("amount", 400);
			
			TraversalDescription td = graphDb.traversalDescription()
		            .breadthFirst()
		            .relationships( RelTypes.TRANSFER, Direction.OUTGOING )
		            .evaluator( new Evaluator(){
		                @Override
		                public Evaluation evaluate( final Path path )
		                {
		                    if ( path.length() == 0 )
		                    {
		                        return Evaluation.EXCLUDE_AND_CONTINUE;
		                    }
		                    boolean included = path.length() <= 2;
		                    boolean continued = path.length() < 3;
		                    return Evaluation.of( included, continued );
		                }
		            } )
		            .uniqueness( Uniqueness.NODE_PATH );
			Traverser tranversal = td.traverse(firstNode);
			PathPrinter pathPrinter = new PathPrinter( "name" );
			for ( Path path : tranversal ){
//				String p = "";
//				for(Relationship relationship : path.relationships()){
//					Node end = relationship.getEndNode();
//					Node start = relationship.getStartNode();
//					String type = relationship.getType().toString();
//					p += start.getProperty("name")+"--["+type+"]-->";
//					
//				}
				System.out.println(path.toString());
				System.out.println(org.neo4j.graphdb.traversal.Paths.pathToString(path, pathPrinter ));
			}
			
			Node user = nodeIndex.get( "name", "lll" ).getSingle();
			if(user != null){
				System.out.println(user.getProperty("name"));
			}
			
			tx.success();
		}
	}

	// 移除新建的数据
//	void removeData() {
//		try (Transaction tx = graphDb.beginTx()) {
//			firstNode.getSingleRelationship(RelTypes.KNOWS, Direction.OUTGOING).delete();
//			firstNode.delete();
//			secondNode.delete();
//			// END SNIPPET: removingData
//
//			tx.success();
//		}
//	}

	// 关闭Neo4j 数据库
	void shutDown() {
		System.out.println("Shutting down database ...");
		// START SNIPPET: shutdownServer
		graphDb.shutdown();
		// END SNIPPET: shutdownServer
	}

	// 为Neo4j 实例注册一个关闭的hook，当VM被强制退出时，Neo4j 实例能够正常关闭
	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running application).
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}
}
