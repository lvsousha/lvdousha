package com.lvdousha.graph_database.neo4j;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;

@SuppressWarnings("rawtypes")
public class DepthAwareExpander implements PathExpander {
	// 使用Map存储要跟踪的遍历深度和关系类型之间的映射
	private final Map<Integer, List<RelationshipType>> relationshipToDepthMapping;

	public DepthAwareExpander(Map<Integer, List<RelationshipType>> relationshipToDepthMapping) {
		this.relationshipToDepthMapping = relationshipToDepthMapping;
	}

	@Override
	public Iterable<Relationship> expand(Path path, BranchState branchState) {
		// 查找遍历的当前深度
		int depth = path.length();
		System.out.println(depth+"==="+path.toString());
		// 在当前的深度查找要跟踪的关系
		List<RelationshipType> relationshipTypes = relationshipToDepthMapping.get(depth);
		// 扩展当前节点配置过的类型的所有关系
		RelationshipType[] relationshipTypeArray = new RelationshipType[0];
		RelationshipType[] relationshipTypesArray = relationshipTypes.toArray(relationshipTypeArray);
//		for(Relationship rel : path.endNode().getRelationships()){
//			System.out.println(rel.toString());
//		}
		return path.endNode().getRelationships(relationshipTypesArray);
	}

	@Override
	public PathExpander reverse() {
		return null;
	}

	public static void main(final String[] args) throws IOException {
    	File file = new File("target/test/db/");
    	 GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(file);

    	try(Transaction tx = graphDB.beginTx()){
    	//配置深度、关系映射
    	 Map<Integer, List<RelationshipType>> mappings = new HashMap<>();
    	 mappings.put(0,
    	 Arrays.asList(new RelationshipType[]{
    			 RelTypes.IS_FRIEND_OF,
    			 RelTypes.WORK_WITH})
    	 );
    	 mappings.put(1, Arrays.asList(new RelationshipType[]{RelTypes.LIKES}));

    	 Node userJohn = graphDB.getNodeById(0);

    	 TraversalDescription traversalDescription = graphDB.traversalDescription()
    	 .expand(new DepthAwareExpander(mappings))
    	 .evaluator(Evaluators.atDepth(2));

    	//从节点john开始遍历
    	 Iterable<Node> nodes = traversalDescription.traverse(userJohn).nodes();
    	for(Node n : nodes){
    		System.out.println(n.getProperty("title") +" -> ");
    	 }
    	for(Path path : traversalDescription.traverse(userJohn)){
    		System.out.println(path.toString());
    	}

    	 tx.success();
    	 }

    	 graphDB.shutdown();
	}

	/**
	 
		create(john:PERSON{name : 'John'})-[:LIKES]->(topGun:FILM{title : 'Top Gun'}),
 (john)-[:IS_FRIEND_OF]->(kate:PERSON{name : 'KATE'}),
 (john)-[:WORK_WITH]->(emma:PERSON{name : 'EMMA'}),
 (kate)-[:LIKES]->(fargo:FILM{title : 'Fargo'}),
 (kate)-[:WORK_WITH]->(alex:PERSON{name : 'Alex'}),
 (kate)-[:WORK_WITH]->(jack:PERSON{name : 'JACK'}),
 (emma)-[:WORK_WITH]->(jack),
 (emma)-[:LIKES]->(alien:FILM{title : 'Alien'}),
 (alex)-[:LIKES]->(godfather:FILM{title : 'Godfather'}),
 (jack)-[:LIKES]->(godfather),
 (jack)-[:LIKES]->(great:FILM{title : 'Great'});
	 */
	
	
}
