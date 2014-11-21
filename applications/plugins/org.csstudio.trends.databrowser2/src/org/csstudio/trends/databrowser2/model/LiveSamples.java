/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.trends.databrowser2.model;

import org.csstudio.apputil.ringbuffer.RingBuffer;
import org.csstudio.trends.databrowser2.preferences.Preferences;

/** Ring buffer for 'live' samples.
 *  <p>
 *  New samples are always added to the end of a ring buffer.
 *
 *  @author Kay Kasemir
 *  @author Takashi Nakamoto changed LiveSamples to handle waveform index.
 */
public class LiveSamples extends PlotSamples
{
    private RingBuffer<PlotSample> samples =
        new RingBuffer<PlotSample>(Preferences.getLiveSampleBufferSize());

    /** Waveform index */
    private int waveform_index = 0;

    /** @param index Waveform index to show */
    public void setWaveformIndex(int index)
    {   // TODO Don't duplicate the waveform_index from PVSamples in LiveSamples and HistoricSamples
        //      Just assert that the samples have correct index as they're added
    	waveform_index = index;

    	// Change the index of all samples in this instance
    	for (int i=0; i<samples.size(); i++) {
    		samples.get(i).setWaveformIndex(waveform_index);
    	}
    }

    /** @return Maximum number of samples in ring buffer */
    public int getCapacity()
    {
        return samples.getCapacity();
    }

    /** Set new capacity.
     *  <p>
     *  Tries to preserve the newest samples.
     *  @param new_capacity New sample count capacity
     *  @throws Exception on out-of-memory error
     */
    public void setCapacity(int new_capacity) throws Exception
    {
        if (new_capacity < 10)
            new_capacity = 10;
        samples.setCapacity(new_capacity);
    }

    /** @param sample Sample to add to ring buffer */
    void add(final PlotSample sample)
    {
    	sample.setWaveformIndex(waveform_index);
        samples.add(sample);
        have_new_samples.set(true);
    }

    @Override
    public int size()
    {
        return samples.size();
    }

    @Override
    public PlotSample get(final int i)
    {
        return samples.get(i);
    }

    /** Delete all samples */
    public void clear()
    {
        samples.clear();
        have_new_samples.set(true);
    }
}
