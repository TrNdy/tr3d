package org.trndy.graph;

import org.mastodon.pool.ByteMappedElement;
import org.mastodon.revised.model.AbstractSpot;
import org.mastodon.revised.model.HasLabel;

import com.indago.data.segmentation.LabelData;

public class SegmentVertex extends AbstractSpot< SegmentVertex, SubsetEdge, SegmentVertexPool, ByteMappedElement, SegmentGraph > implements HasLabel
{

	protected SegmentVertex( final SegmentVertexPool pool )
	{
		super( pool );
	}

	// Temp fields to each covariance and position calculation.
	private final double[] pos = new double[ numDimensions() ];
	private final double[][] cov = new double[ numDimensions() ][ numDimensions() ];

	public synchronized SegmentVertex init( final LabelData labelData, final int timepoint )
	{
		labelData.getSegment().getCenterOfMass().localize( pos );
		super.partialInit( timepoint, pos );
		pool.labelData.set( this, labelData );
		pool.labelDataMap.put( labelData, this.getInternalPoolIndex() );

		covarianceFromLabelData( labelData, cov );
		setCovarianceInternal( cov );
		final double radius = radiusFromLabelData( labelData );
		pool.boundingSphereRadiusSqu.setQuiet( this, radius * radius );

		super.initDone();
		return this;
	}

	public void setTimepoint( final int tp )
	{
		timepoint.set( tp );
	}

	public LabelData getLabelData()
	{
		return pool.labelData.get( this );
	}

	@Override
	public void setLabel( final String label )
	{
		pool.vertexName.set( this, label );
	}

	@Override
	public String getLabel()
	{
		if ( pool.vertexName.isSet( this ) )
			return pool.vertexName.get( this );
		else
			return "v" + getLabelData().getId() + " t" + getTimepoint();
	}

	/**
	 * Determines the covariance matrix from the specified {@link LabelData}
	 * object.
	 *
	 * @param labelData
	 *            the {@link LabelData} to inspect.
	 * @param cov
	 *            the array to fill.
	 */
	private void covarianceFromLabelData( final LabelData labelData, final double[][] cov )
	{
		// TODO We can do better than this.
		final double r = radiusFromLabelData( labelData );
		final double rsqu = r * r;
		covarianceFromRadiusSquared( rsqu, cov );
	}

	/**
	 * Returns the radius determined from the specified {@link LabelData}.
	 *
	 * @param labelData
	 *            the labelData object.
	 * @return the radius.
	 */
	private double radiusFromLabelData( final LabelData labelData )
	{
		final long area = labelData.getSegment().getArea();
		final double radius = Math.pow( 3. / 4. / Math.PI * area, 1. / 3. );
		return radius;
	}

	private void covarianceFromRadiusSquared( final double rsqu, final double[][] cov )
	{
		for ( int row = 0; row < 3; ++row )
			for ( int col = 0; col < 3; ++col )
				cov[ row ][ col ] = ( row == col ) ? rsqu : 0;
	}

	private void setCovarianceInternal( final double[][] cov )
	{
		int i = 0;
		for ( int row = 0; row < 3; ++row )
			for ( int col = row; col < 3; ++col )
				pool.covariance.setQuiet( this, i++, cov[ row ][ col ] );
	}
}
