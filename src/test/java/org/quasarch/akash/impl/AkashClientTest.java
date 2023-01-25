package org.quasarch.akash.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AkashClientTest {

    @Test
    void createDeployment() {
        var instance = new AkashClient("some-address", clientUrl);

    }

    @Test
    void createDeploymentShouldThrowOnEmptySdlFile() {
        var instance = new AkashClient("some-address", clientUrl);
        Assertions.assertThrows(NullPointerException.class, () -> instance.createDeployment(null));
    }

    @Test
    void closedDeployment() {
    }

    @Test
    void listDeployments() {
    }

    @Test
    void listDeploymentsShouldThrowOnEmpty() {
        var instance = new AkashClient("some-address", clientUrl);
        Assertions.assertThrows(NullPointerException.class,
                () -> instance.listDeployments(null, null, null));
    }

    @Test
    void testListDeployments() {
    }

    @Test
    void getDeployment() {
    }

    @Test
    void listBids() {
    }

    @Test
    void testListBids() {
    }

    @Test
    void getLease() {
    }
}
