/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.trends.databrowser2.propsheet;

import org.csstudio.swt.rtplot.undo.UndoableAction;
import org.csstudio.swt.rtplot.undo.UndoableActionManager;
import org.csstudio.trends.databrowser2.Messages;
import org.csstudio.trends.databrowser2.model.Model;
import org.eclipse.swt.graphics.RGB;

/** Undo-able command to change plot background color
 *  @author Kay Kasemir
 */
public class ChangePlotBackgroundCommand implements UndoableAction
{
    final private Model model;
    final private RGB old_color, new_color;

    /** Register and perform the command
     *  @param model Model to configure
     *  @param operations_manager OperationsManager where command will be reg'ed
     *  @param new_color New value
     */
    public ChangePlotBackgroundCommand(final Model model,
            final UndoableActionManager operations_manager,
            final RGB new_color)
    {
        this.model = model;
        this.old_color = model.getPlotBackground();
        this.new_color = new_color;
        operations_manager.perform(this);
    }

    /** {@inheritDoc} */
    @Override
    public void perform()
    {
        model.setPlotBackground(new_color);
    }

    /** {@inheritDoc} */
    @Override
    public void undo()
    {
        model.setPlotBackground(old_color);
    }

    /** @return Command name that appears in undo/redo menu */
    @Override
    public String toString()
    {
        return Messages.Color;
    }
}
