package org.trndy.demo;

import java.io.IOException;

import org.scijava.ui.behaviour.ScrollBehaviour;
import org.scijava.ui.behaviour.io.InputTriggerConfig;
import org.scijava.ui.behaviour.io.yaml.YamlConfigIO;
import org.scijava.ui.behaviour.util.Behaviours;
import org.scijava.ui.behaviour.util.TriggerBehaviourBindings;
import org.trndy.Tr3dLog;

import com.indago.data.segmentation.LabelData;
import com.indago.data.segmentation.LabelingPlus;
import com.indago.data.segmentation.XmlIoLabelingPlus;
import com.indago.data.segmentation.visualization.ColorStream;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import net.imglib2.RandomAccess;
import net.imglib2.converter.Converter;
import net.imglib2.converter.Converters;
import net.imglib2.interpolation.randomaccess.NearestNeighborInterpolatorFactory;
import net.imglib2.realtransform.AffineTransform2D;
import net.imglib2.realtransform.RealViews;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.view.Views;

public class SelectSegments {

	LabelingPlus labelingPlus;

	Bdv bdv;

	BdvListener bdvListener;

	public void run() throws IOException
	{
		final String folder = "samples/labeling_frames/";
		final InputTriggerConfig config = new InputTriggerConfig( YamlConfigIO.read( "samples/tr3d.yaml" ) );
		final String fLabeling = folder + "labeling_frame0000.xml";
		labelingPlus = new XmlIoLabelingPlus().load( fLabeling );

//		Converters.convert( lp.getLabeling().getIndexImg(), new ColorFragments(), new ARGBType() );
//		final Bdv bdv = BdvFunctions.show( lp.getLabeling().getIndexImg(), "img", Bdv.options().is2D() );

		bdv = BdvFunctions.show(
				Converters.convert(
						labelingPlus.getLabeling().getIndexImg(),
						( i, o ) -> o.set( ColorStream.get( i.get() ) ),
						new ARGBType() ),
				"fragments",
				Bdv.options().inputTriggerConfig( config ).is2D() );

		bdvListener = new BdvListener( bdv );

		final TriggerBehaviourBindings bindings = bdv.getBdvHandle().getTriggerbindings();
		final Behaviours behaviours = new Behaviours( config, new String[] { "tr2d" } );
		behaviours.behaviour(
				new ScrollBehaviour() {
					@Override
					public void scroll( final double wheelRotation, final boolean isHorizontal, final int x, final int y ) {
						findSegments( x, y );
					}
				},
				"browse segments",
				"scroll" );
		behaviours.install( bindings, "segments" );
	}

	public void findSegments( final int x, final int y )
	{
		Tr3dLog.log.trace( "findSegments (" + x + ", " + y + "):" );

		final ImgLabeling< LabelData, IntType > labeling = labelingPlus.getLabeling();

		final AffineTransform2D transform = new AffineTransform2D();
		bdvListener.getViewerTransform2D( transform );

		final RandomAccess< LabelingType< LabelData > > a =
				RealViews.affine(
						Views.interpolate(
								Views.extendValue(
										labeling,
										labeling.firstElement().createVariable() ),
								new NearestNeighborInterpolatorFactory< >() ),
						transform ).randomAccess();

		a.setPosition( new int[] { x, y } );
		for ( final LabelData label : a.get() )
			Tr3dLog.log.trace( "   " + label.getSegment() );
	}

	public static void main( final String[] args ) throws IOException {
		new SelectSegments().run();
	}
}

class ColorFragments implements Converter< IntType, ARGBType > {
	@Override
	public void convert( final IntType input, final ARGBType output ) {
		output.set( ColorStream.get( input.get() ) );
	}
}
