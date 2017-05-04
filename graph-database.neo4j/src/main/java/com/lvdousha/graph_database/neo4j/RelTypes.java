package com.lvdousha.graph_database.neo4j;

import org.neo4j.graphdb.RelationshipType;

public enum RelTypes implements RelationshipType{
	TRANSFER,
	IS_FRIEND_OF,
	WORK_WITH,
	LIKES
}
