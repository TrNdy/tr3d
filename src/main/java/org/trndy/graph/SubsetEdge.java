package org.trndy.graph;

import org.mastodon.graph.ref.AbstractListenableEdge;
import org.mastodon.pool.ByteMappedElement;

public class SubsetEdge extends AbstractListenableEdge< SubsetEdge, SegmentVertex, SubsetEdgePool, ByteMappedElement >
{

	protected SubsetEdge( final SubsetEdgePool pool )
	{
		super( pool );
	}

	public SubsetEdge init()
	{
		super.initDone();
		return this;
	}

	@Override
	public String toString()
	{
		return String.format( "SubsetEdge( %d -> %d )", getSource().getInternalPoolIndex(), getTarget().getInternalPoolIndex() );
	}

	protected void notifyEdgeAdded()
	{
		super.initDone();
	}
}
