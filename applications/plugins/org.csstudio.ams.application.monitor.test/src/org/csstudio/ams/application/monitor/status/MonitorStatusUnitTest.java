
/*
 * Copyright (c) 2012 Stiftung Deutsches Elektronen-Synchrotron,
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS.
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE.
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS,
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION,
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 *
 * $Id: DesyKrykCodeTemplates.xml,v 1.7 2010/04/20 11:43:22 bknerr Exp $
 */

package org.csstudio.ams.application.monitor.status;

import org.csstudio.ams.application.monitor.status.CheckStatus;
import org.csstudio.ams.application.monitor.status.ErrorReason;
import org.csstudio.ams.application.monitor.status.MonitorStatus;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mmoeller
 * @version 1.0
 * @since 07.05.2012
 */
public class MonitorStatusUnitTest {
    
    @Test
    public void testAddingCheckStatus() {
        
        MonitorStatus o = new MonitorStatus(3, 3);
        Assert.assertEquals(0, o.getErrorCount());
        
        o.addCheckStatusInfo(1L, CheckStatus.ERROR, ErrorReason.AMS);
        Assert.assertEquals(1, o.getErrorCount());
        
        o.addCheckStatusInfo(1L << 1, CheckStatus.ERROR, ErrorReason.AMS);
        Assert.assertEquals(2, o.getErrorCount());

        o.addCheckStatusInfo(1L << 2, CheckStatus.ERROR, ErrorReason.AMS);
        Assert.assertEquals(3, o.getErrorCount());
        Assert.assertTrue(o.hasMaxErrorCount());
        
        o = new MonitorStatus(3, 3);
        Assert.assertEquals(0, o.getWarnCount());
        
        o.addCheckStatusInfo(1L, CheckStatus.WARN, ErrorReason.AMS);
        Assert.assertEquals(1, o.getWarnCount());
        
        o.addCheckStatusInfo(1L << 1, CheckStatus.WARN, ErrorReason.AMS);
        Assert.assertEquals(2, o.getWarnCount());

        o.addCheckStatusInfo(1L << 2, CheckStatus.WARN, ErrorReason.AMS);
        Assert.assertEquals(3, o.getWarnCount());
        Assert.assertTrue(o.hasMaxWarnCount());
    }
}