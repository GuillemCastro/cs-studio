/*
 * Copyright (c) 2008 Stiftung Deutsches Elektronen-Synchrotron,
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
 */
package org.csstudio.utility.ldapUpdater;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.csstudio.platform.service.osgi.OsgiServiceUnavailableException;
import org.csstudio.utility.ldap.service.ILdapService;
import org.csstudio.utility.ldap.service.LdapServiceTracker;
import org.csstudio.utility.ldapUpdater.service.ILdapServiceProvider;
import org.csstudio.utility.ldapUpdater.service.ILdapUpdaterFileService;
import org.csstudio.utility.ldapUpdater.service.ILdapUpdaterService;
import org.csstudio.utility.ldapUpdater.service.impl.LdapUpdaterFileServiceImpl;
import org.csstudio.utility.ldapUpdater.service.impl.LdapUpdaterServiceImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.remotercp.common.tracker.GenericServiceTracker;
import org.remotercp.common.tracker.IGenericServiceListener;
import org.remotercp.service.connection.session.ISessionService;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * The activator class controls the plug-in life cycle
 */
public class LdapUpdaterActivator implements BundleActivator {

    /**
     * The id of this Java plug-in (value <code>{@value}</code> as defined in MANIFEST.MF.
     */
    public static final String PLUGIN_ID = "org.csstudio.utility.ldapUpdater";

    /**
     *  The shared instance
     */
    private static LdapUpdaterActivator INSTANCE;

    private ServiceTracker _ldapServiceTracker;

	private GenericServiceTracker<ISessionService> _genericServiceTracker;

    private LdapUpdaterServiceImpl _ldapUpdaterService;
    private LdapUpdaterFileServiceImpl _ldapUpdaterFileService;


    /**
     * Don't instantiate.
     * Called by framework.
     */
    public LdapUpdaterActivator() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Activator " + PLUGIN_ID + " does already exist.");
        }
        INSTANCE = this; // Antipattern is required by the framework!
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    @Nonnull
    public static LdapUpdaterActivator getDefault() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(@Nullable final BundleContext context) throws Exception {

        _ldapServiceTracker = new LdapServiceTracker(context);
        _ldapServiceTracker.open();

        _genericServiceTracker =
            new GenericServiceTracker<ISessionService>(context, ISessionService.class);
        _genericServiceTracker.open();

        final ILdapServiceProvider provider =
            new ILdapServiceProvider() {
                @Override
                @Nonnull
                public ILdapService getLdapService() throws OsgiServiceUnavailableException {
                    @SuppressWarnings("synthetic-access")
                    final ILdapService service =  (ILdapService) _ldapServiceTracker.getService();
                    if (service == null) {
                        throw new OsgiServiceUnavailableException("LDAP service could not be retrieved. Please try again later or check LDAP connection.");
                    }
                    return service;
                }
            };

        final Injector injector = Guice.createInjector(new LdapUpdaterModule(provider));
        _ldapUpdaterFileService = injector.getInstance(LdapUpdaterFileServiceImpl.class);
        _ldapUpdaterService = injector.getInstance(LdapUpdaterServiceImpl.class);
    }




    /**
     * {@inheritDoc}
     */
    @Override
    public void stop(@Nullable final BundleContext context) throws Exception {
        _ldapServiceTracker.close();
    }


    @Nonnull
    public static String getPluginId() {
        return PLUGIN_ID;
    }

	public void addSessionServiceListener(@Nonnull final IGenericServiceListener<ISessionService> sessionServiceListener) {
		_genericServiceTracker.addServiceListener(sessionServiceListener);
	}

    /**
     * ANTI-PATTERN due to extension points. Cannot inject services in extension point classes.
     */
	@Nonnull
    public ILdapUpdaterService getLdapUpdaterService() throws OsgiServiceUnavailableException {
	    if (_ldapUpdaterService == null) {
	        throw new OsgiServiceUnavailableException("Service field has not been set. Hasnt' the framework called Activator.start before?");
	    }
        return _ldapUpdaterService;
    }
	/**
	 * ANTI-PATTERN due to extension points. Cannot inject services in extension point classes.
	 */
	@Nonnull
	public ILdapUpdaterFileService getLdapUpdaterFileService() throws OsgiServiceUnavailableException {
	    if (_ldapUpdaterFileService == null) {
	        throw new OsgiServiceUnavailableException("Service field has not been set. Hasnt' the framework called Activator.start before?");
	    }
	    return _ldapUpdaterFileService;
	}
}
