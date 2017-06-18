package org.trndy.graph;

import org.mastodon.pool.ByteMappedElement;
import org.mastodon.pool.ByteMappedElementArray;
import org.mastodon.pool.SingleArrayMemPool;
import org.mastodon.pool.attributes.DoubleArrayAttribute;
import org.mastodon.pool.attributes.DoubleAttribute;
import org.mastodon.properties.ObjPropertyMap;
import org.mastodon.properties.Property;
import org.mastodon.revised.model.AbstractSpotPool;

import com.indago.data.segmentation.LabelData;

import gnu.trove.impl.Constants;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

public class SegmentVertexPool extends AbstractSpotPool< SegmentVertex, SubsetEdge, ByteMappedElement, SegmentGraph >
{

	public static class SegmentVertexLayout extends AbstractSpotLayout
	{
		public SegmentVertexLayout()
		{
			super( 3 );
		}

		final DoubleArrayField covariance = doubleArrayField( 6 );

		final DoubleField boundingSphereRadiusSqu = doubleField();
	}

	public static final SegmentVertexLayout layout = new SegmentVertexLayout();

	private static final int NO_ENTRY_KEY = -1;

	final DoubleArrayAttribute< SegmentVertex > covariance = new DoubleArrayAttribute<>( layout.covariance, this );

	final DoubleAttribute< SegmentVertex > boundingSphereRadiusSqu = new DoubleAttribute<>( layout.boundingSphereRadiusSqu, this );

	final ObjPropertyMap< SegmentVertex, String > vertexName = new ObjPropertyMap<>( this );

	final ObjPropertyMap< SegmentVertex, LabelData > labelData = new ObjPropertyMap<>( this );

	final TObjectIntMap< LabelData > labelDataMap = new TObjectIntHashMap<>( Constants.DEFAULT_CAPACITY, Constants.DEFAULT_LOAD_FACTOR, NO_ENTRY_KEY );

	SegmentVertexPool( final int initialCapacity )
	{
		super( initialCapacity, layout, SegmentVertex.class, SingleArrayMemPool.factory( ByteMappedElementArray.factory ) );
	}

	@Override
	protected SegmentVertex createEmptyRef()
	{
		return new SegmentVertex( this );
	}

	public final Property< SegmentVertex > covarianceProperty()
	{
		return covariance;
	}

	public final Property< SegmentVertex > boundingSphereRadiusSquProperty()
	{
		return boundingSphereRadiusSqu;
	}

	public final Property< SegmentVertex > positionProperty()
	{
		return position;
	}

	public final Property< SegmentVertex > vertexName()
	{
		return vertexName;
	}

	public final Property< SegmentVertex > labelData()
	{
		return labelData();
	}
}
