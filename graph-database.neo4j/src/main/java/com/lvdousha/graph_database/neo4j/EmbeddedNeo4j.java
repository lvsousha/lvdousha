package com.lvdousha.graph_database.neo4j;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.Paths;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.graphdb.traversal.Uniqueness;
import org.neo4j.io.fs.FileUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class EmbeddedNeo4j {
	// Embedded Neo4j会在本地产生一个文件夹(类似于Mysql的数据库)
	private static final String DB_PATH = "target/neo4j/db/";
	private static EmbeddedNeo4j instance;
	private static GraphDatabaseService graphDb;
	public static Logger log = LoggerFactory.getLogger(EmbeddedNeo4j.class);
	
	
	public static void main(final String[] args) throws IOException {
//		FileUtils.deleteRecursively(new File(DB_PATH));
//		EmbeddedNeo4j embeddedNeo4j = EmbeddedNeo4j.getInstance();
//		embeddedNeo4j.search();
//		embeddedNeo4j.removeData();
//		embeddedNeo4j.shutDown();
	}
	
	private EmbeddedNeo4j() {
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(DB_PATH));
		registerShutdownHook(graphDb);
	}

	public static EmbeddedNeo4j getInstance() {
		if (instance == null) {
			instance = new EmbeddedNeo4j();
		}
		if(graphDb == null){
			graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(DB_PATH));
			registerShutdownHook(graphDb);
		}
		return instance;
	}
	
	public void createNode(Map<String,Object> model, String label) {
		try (Transaction tx = graphDb.beginTx()) {
			Index<Node> nodeIndex = graphDb.index().forNodes(label);
			Node node = graphDb.createNode();
			node.addLabel(Label.label(label));
			for(String key : model.keySet()){
				node.setProperty(key, model.get(key));
			}
			nodeIndex.add(node, label , model.get(label));
			tx.success();
		}
	}
	
	public void createIndex(String label, String key){
		IndexDefinition indexDefinition;
		try (Transaction tx = graphDb.beginTx()){
		    Schema schema = graphDb.schema();
		    indexDefinition = schema.indexFor( Label.label(label) )
		            .on(key)
		            .create();
		    tx.success();
		}
		try (Transaction tx = graphDb.beginTx()){
		    Schema schema = graphDb.schema();
		    schema.awaitIndexOnline( indexDefinition, 10, TimeUnit.SECONDS );
		}
	}
	
	public void createNodeAccordingToIndex(Map<String,Object> model, String label){
		try (Transaction tx = graphDb.beginTx()){
	        Node node = graphDb.createNode(Label.label(label));
	        for(String key : model.keySet()){
				node.setProperty(key, model.get(key));
			}
		    tx.success();
		}
	}
	
	public void createRelationship(Node start, Node end, RelTypes reltype, Map<String,Object> properties){
		try (Transaction tx = graphDb.beginTx()) {
			Relationship relationship = start.createRelationshipTo(end, reltype);
			for(String key : properties.keySet()){
				relationship.setProperty(key, properties.get(key));
			}
			tx.success();
		}
	}
	
	void search() throws IOException {
		try (Transaction tx = graphDb.beginTx()) {
			Index<Node> nodeIndex = graphDb.index().forNodes( "nodes" );
			
			Node user = nodeIndex.get( "name", "Joe" ).getSingle();
			if(user != null){
				log.info(user.getProperty("name").toString());
			}
			TraversalDescription td = graphDb.traversalDescription()
		            .breadthFirst()
		            .relationships( RelTypes.TRANSFER, Direction.OUTGOING )
		            .evaluator( new Evaluator(){
		                @Override
		                public Evaluation evaluate( final Path path ){
		                    if ( path.length() == 0 ){
		                        return Evaluation.EXCLUDE_AND_CONTINUE;
		                    }
		                    boolean included = path.length() <= 2;
		                    boolean continued = path.length() < 3;
		                    return Evaluation.of( included, continued );
		                }
		            })
		            .uniqueness( Uniqueness.NODE_PATH );
			Traverser tranversal = td.traverse(user);
			PathPrinter pathPrinter = new PathPrinter( "name" );
			for (Path path : tranversal){
				log.info(Paths.pathToString(path, pathPrinter ));
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
		graphDb.shutdown();
	}

	// 为Neo4j 实例注册一个关闭的hook，当VM被强制退出时，Neo4j 实例能够正常关闭
	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}
}
