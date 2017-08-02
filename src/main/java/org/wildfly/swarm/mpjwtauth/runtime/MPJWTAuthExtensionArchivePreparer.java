/**
 * Copyright 2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.swarm.mpjwtauth.runtime;

import javax.inject.Inject;

import io.undertow.servlet.ServletExtension;
import org.eclipse.microprofile.jwt.impl.DefaultJWTCallerPrincipalFactory;
import org.eclipse.microprofile.jwt.principal.JWTCallerPrincipalFactory;
import org.eclipse.microprofile.jwt.wfswarm.JWTAuthMethodExtension;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.wildfly.swarm.mpjwtauth.MicroProfileJWTAuthFraction;
import org.wildfly.swarm.spi.api.DeploymentProcessor;
import org.wildfly.swarm.spi.runtime.annotations.DeploymentScoped;
import org.wildfly.swarm.undertow.WARArchive;

/**
 * A DeploymentProcessor implementation for the MP-JWT custom authentication mechanism that adds support
 * for that mechanism to any war the declares a login-config/auth-method = MP-JWT.
 */
@DeploymentScoped
public class MPJWTAuthExtensionArchivePreparer implements DeploymentProcessor {
    private static Logger log = Logger.getLogger(MPJWTAuthExtensionArchivePreparer.class);
    private final Archive archive;

    @Inject
    private MicroProfileJWTAuthFraction fraction;
    @Inject
    public MPJWTAuthExtensionArchivePreparer(Archive archive) {
        this.archive = archive;
    }

    @Override
    public void process() throws Exception {
        // This is really a work around addAsServiceProvider not supporting multiple addAsServiceProvider calls (https://github.com/shrinkwrap/shrinkwrap/issues/112)
        JavaArchive jwtAuthJar = ShrinkWrap.create(JavaArchive.class, "jwt-auth-wfswarm.jar")
                .addAsServiceProvider(ServletExtension.class, JWTAuthMethodExtension.class)
                .addAsServiceProvider(JWTCallerPrincipalFactory.class, DefaultJWTCallerPrincipalFactory.class);
        WARArchive war = archive.as(WARArchive.class);
        war.addAsLibraries(jwtAuthJar);
        if(fraction.getTokenIssuer().isPresent()) {
            log.debugf("Issuer: %s", fraction.getTokenIssuer().get());
            war.addAsManifestResource(new StringAsset(fraction.getTokenIssuer().get()), "MP-JWT-ISSUER");
        }
        if(fraction.getPublicKey() != null) {
            log.debugf("PublicKey: %s", fraction.getPublicKey());
            war.addAsManifestResource(new StringAsset(fraction.getPublicKey()), "MP-JWT-SIGNER");
        }
        if(log.isDebugEnabled()) {
            log.debug("jar: " + jwtAuthJar.toString(true));
            log.debug("war: " + war.toString(true));
        }
    }
}
