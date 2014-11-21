/*******************************************************************************
 * Copyright (c) 2012 Cosylab.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.trends.databrowser2.propsheet;

import org.csstudio.swt.rtplot.undo.UndoableAction;
import org.csstudio.swt.rtplot.undo.UndoableActionManager;
import org.csstudio.trends.databrowser2.Messages;
import org.csstudio.trends.databrowser2.model.ModelItem;

/** Undo-able command to change item's waveform index
 *  @author Takashi Nakamoto (Cosylab)
 */
public class ChangeWaveformIndexCommand implements UndoableAction
{
    final private ModelItem item;
    final private int old_index, new_index;

    /** Register and perform the command
     *  @param operations_manager OperationsManager where command will be reg'ed
     *  @param item Model item to configure
     *  @param new_index New value
     */
    public ChangeWaveformIndexCommand(final UndoableActionManager operations_manager,
            final ModelItem item, final int new_index)
    {
        this.item = item;
        this.old_index = item.getWaveformIndex();
        this.new_index = new_index;
        operations_manager.perform(this);
    }

    /** {@inheritDoc} */
    @Override
    public void perform()
    {
        item.setWaveformIndex(new_index);
    }

    /** {@inheritDoc} */
    @Override
    public void undo()
    {
        item.setWaveformIndex(old_index);
    }

    /** @return Command name that appears in undo/redo menu */
    @Override
    public String toString()
    {
        return Messages.WaveformIndexColTT;
    }
}
