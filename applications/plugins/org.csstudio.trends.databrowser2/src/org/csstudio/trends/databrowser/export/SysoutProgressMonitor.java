/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.trends.databrowser.export;

import org.eclipse.core.runtime.IProgressMonitor;

/** ProgressMonitor for debugging, prints progress info
 *  @author Kay Kasemir
 */
@SuppressWarnings("nls")
public class SysoutProgressMonitor implements IProgressMonitor
{
    public void beginTask(final String name, final int totalWork)
    {
        System.out.println("START: " + name);
    }

    public void setTaskName(final String name)
    {
    }

    public void subTask(final String name)
    {
        System.out.println(name);
    }

    public void worked(int work)
    {
    }

    public void internalWorked(double work)
    {
    }

    public void done()
    {
        System.out.println("DONE.");
    }

    public boolean isCanceled()
    {
        return false;
    }

    public void setCanceled(boolean value)
    {
    }
}
