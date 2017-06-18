package org.trndy.graph;

import org.mastodon.graph.ref.AbstractEdgePool;
import org.mastodon.graph.ref.AbstractListenableEdgePool;
import org.mastodon.pool.ByteMappedElement;
import org.mastodon.pool.ByteMappedElementArray;
import org.mastodon.pool.SingleArrayMemPool;

public class SubsetEdgePool extends AbstractListenableEdgePool< SubsetEdge, SegmentVertex, ByteMappedElement >
{
	SubsetEdgePool( final int initialCapacity, final SegmentVertexPool vertexPool )
	{
		super( initialCapacity, AbstractEdgePool.layout, SubsetEdge.class, SingleArrayMemPool.factory( ByteMappedElementArray.factory ), vertexPool );
	}

	@Override
	protected SubsetEdge createEmptyRef()
	{
		return new SubsetEdge( this );
	}
}
