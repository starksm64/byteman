/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package test;

import com.sun.tools.attach.VirtualMachine;
import junit.framework.Assert;
import org.jboss.byteman.agent.install.Install;
import org.jboss.byteman.contrib.bmunit.BMUnit;
import org.jboss.byteman.contrib.bmunit.BMUnitConfig;
import org.jboss.byteman.contrib.bmunit.BMUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Properties;

/**
 * Validate that the byteman agent has the expected configuration properties from the class BMUnitConfig config
 * settings.
 *
 * @author Scott stark (sstark@redhat.com) (C) 2011 Red Hat Inc.
 * @version $Revision:$
 */
@RunWith(BMUnitRunner.class)
@BMUnitConfig(agentHost = "localhost", agentPort = 10999,
    isBmunitVerbose = true, isBytemanVerbose = true,
    isBytemanDebug = true,
    // Assumes test runs in root of source tree with install directory built
    bytemanHome = "install"
)
public class ConfigPropertyJunitTest {

    /** Validate that the @BMUnitConfig settings on this class are seen by the agent
     * virtual machine as the corresponding system properties.
     * @throws IOException
     */
    @Test
    public void validateAgentProperties() throws IOException {
        Install agent = BMUnit.getInstalledAgent();
        Assert.assertNotNull("Installed agent", agent);
        VirtualMachine vm = agent.getVm();
        Assert.assertNotNull("Agent VM", vm);
        Properties agentProps = agent.getSysProps();
        Assert.assertNotNull("Agent VM system properties", agentProps);
        // byteman.verbose
        String prop = agentProps.getProperty("org.jboss.byteman.verbose");
        Assert.assertEquals("org.jboss.byteman.verbose", "true", prop);
        // byteman.debug
        prop = agentProps.getProperty("org.jboss.byteman.debug");
        Assert.assertEquals("org.jboss.byteman.debug", "true", prop);
        // byteman.home
        prop = agentProps.getProperty("org.jboss.byteman.home");
        Assert.assertEquals("org.jboss.byteman.home", "install", prop);
        // bmunit.verbose
        prop = agentProps.getProperty("org.jboss.byteman.contrib.bmunit.verbose");
        Assert.assertEquals("org.jboss.byteman.contrib.bmunit.verbose", "true", prop);
        // host
        prop = agentProps.getProperty("org.jboss.byteman.contrib.bmunit.agent.host");
        Assert.assertEquals("org.jboss.byteman.contrib.bmunit.agent.host", "localhost", prop);
        // port
        prop = agentProps.getProperty("org.jboss.byteman.contrib.bmunit.agent.port");
        Assert.assertEquals("org.jboss.byteman.contrib.bmunit.agent.port", "10999", prop);
        // inhibit
        prop = agentProps.getProperty("org.jboss.byteman.contrib.bmunit.agent.inhibit");
        Assert.assertEquals("org.jboss.byteman.contrib.bmunit.agent.inhibit", "false", prop);
    }
}