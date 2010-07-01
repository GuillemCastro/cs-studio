/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.trends.databrowser.model;

import java.util.ArrayList;

import org.csstudio.platform.data.ITimestamp;
import org.csstudio.platform.data.IValue;

/** Holder for 'historic' samples.
 *  <p>
 *  In addition to holding 'all' historic samples, this class
 *  allows for a 'border' time beyond which no samples will
 *  be provided.
 *  When setting this border to the start of the 'live' samples,
 *  this class will thus assert that the live samples have
 *  precedence because no 'historic' sample is provided
 *  for the 'live' time range.
 *  When the start of the 'live' time range moves because
 *  the live data ring buffer rolls around, the 'border' time adjustments
 *  might then uncover historic samples that were previously
 *  hidden below the 'live' time range.
 *  
 *  @author Kay Kasemir
 */
public class HistoricSamples extends PlotSamples
{
    /** "All" historic samples */
    private PlotSample samples[] = new PlotSample[0];
    
    /** If non-null, samples beyond this time are hidden from access */
    private ITimestamp border_time = null;
    
    /** Subset of samples.length that's below border_time
     *  @see #computeVisibleSize()
     */
    private int visible_size = 0;
    
    /** Define a new 'border' time beyond which no samples
     *  are returned from the history
     *  @param border_time New time or <code>null</code> to access all samples
     */
    public void setBorderTime(final ITimestamp border_time)
    {   // Anything new?
        if (border_time == null)
        {
            if (this.border_time == null)
                return;
        }
        else if (border_time.equals(this.border_time))
                return;
        // New border, recompute, mark as 'new data'
        this.border_time = border_time;
        computeVisibleSize();
        have_new_samples = true;
    }

    /** Update visible size */
    synchronized private void computeVisibleSize()
    {
        if (border_time == null)
            visible_size = samples.length;
        else
        {
            final int last_index = PlotSampleSearch.findSampleLessThan(
                                        samples, border_time);
            visible_size = (last_index < 0)   ?   0   :   last_index + 1;
        }
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings("nls")
    @Override
    synchronized public PlotSample getSample(final int i)
    {
        if (i >= visible_size)
            throw new IndexOutOfBoundsException("Index " + i + " exceeds visible size " + visible_size);
        return samples[i];
    }

    /** {@inheritDoc} */
    @Override
    synchronized public int getSize()
    {
        return visible_size;
    }

    /** Merge newly received archive data into historic samples
     *  @param source Info about data source
     *  @param result Samples to add/merge
     */
    synchronized public void mergeArchivedData(final String source, final ArrayList<IValue> result)
    {
        // Anything new at all? 
        if (result.size() <= 0)
            return;
        // Turn IValues into PlotSamples
        final PlotSample new_samples[] = new PlotSample[result.size()];
        for (int i=0; i<new_samples.length; ++i)
            new_samples[i] = new PlotSample(source, result.get(i));
        // Merge with existing samples
        final PlotSample merged[] = PlotSampleMerger.merge(samples, new_samples);
        if (merged == samples)
            return;
        samples = merged;
        computeVisibleSize();
        have_new_samples = true;
    }

    /** Delete all samples */
    synchronized public void clear()
    {
        visible_size = 0;
        samples = new PlotSample[0];
        have_new_samples = true;
    }
}
