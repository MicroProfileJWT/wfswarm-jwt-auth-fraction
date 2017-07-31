package org.wildfly.swarm.mpjwtauth;

import java.security.PublicKey;

import org.wildfly.swarm.config.runtime.AttributeDocumentation;
import org.wildfly.swarm.spi.api.Defaultable;
import org.wildfly.swarm.spi.api.Fraction;
import org.wildfly.swarm.spi.api.annotations.Configurable;

import static org.wildfly.swarm.spi.api.Defaultable.integer;
import static org.wildfly.swarm.spi.api.Defaultable.string;

/**
 * Created by starksm on 7/28/17.
 */
@Configurable("swarm.mpjwtauth")
public class MicroProfileJWTAuthFraction implements Fraction<MicroProfileJWTAuthFraction> {

    @AttributeDocumentation("The URI of the JWT token issuer")
    @Configurable("swarm.mpjwtauth.token.issuer")
    private Defaultable<String> tokenIssuer = string("http://localhost");

    @AttributeDocumentation("The public key of the JWT token issuer")
    @Configurable("swarm.mpjwtauth.token.publicKey")
    private PublicKey publicKey;

    @AttributeDocumentation("The JWT token expiration grace period in seconds ")
    @Configurable("swarm.mpjwtauth.token.expGracePeriod")
    private Defaultable<Integer> expGracePeriodSecs = integer(60);


    public Defaultable<String> getTokenIssuer() {
        return tokenIssuer;
    }

    public void setTokenIssuer(Defaultable<String> tokenIssuer) {
        this.tokenIssuer = tokenIssuer;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public Defaultable<Integer> getExpGracePeriodSecs() {
        return expGracePeriodSecs;
    }

    public void setExpGracePeriodSecs(Defaultable<Integer> expGracePeriodSecs) {
        this.expGracePeriodSecs = expGracePeriodSecs;
    }
}
