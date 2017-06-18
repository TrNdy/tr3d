package org.trndy.graph;

import org.mastodon.io.properties.StringPropertyMapSerializer;
import org.mastodon.pool.ByteMappedElement;
import org.mastodon.revised.model.AbstractModelGraph;

import com.indago.data.segmentation.LabelData;

public class SegmentGraph extends AbstractModelGraph< SegmentGraph, SegmentVertexPool, SubsetEdgePool, SegmentVertex, SubsetEdge, ByteMappedElement >
{

	public SegmentGraph()
	{
		this( 1000 );
	}

	public SegmentGraph( final int initialCapacity )
	{
		super( new SubsetEdgePool( initialCapacity, new SegmentVertexPool( initialCapacity ) ) );

		vertexPropertySerializers.put( "name", new StringPropertyMapSerializer<>( vertexPool.vertexName ) );
		// TODO Add serialization for the labelData property and the
		// labelDataMap.
	}

	public SegmentVertex getVertexForLabel( final LabelData labelData, final SegmentVertex ref )
	{
		final int id = vertexPool.labelDataMap.get( labelData );
		return ( id < 0 ) ? null : vertexPool.getObject( id, ref );
	}

}
