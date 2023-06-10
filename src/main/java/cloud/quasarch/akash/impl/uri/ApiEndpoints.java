package cloud.quasarch.akash.impl.uri;

/**
 * A list of all available Akash REST API endpoints
 */
public final class ApiEndpoints {

    private static final String BASE_PATH = "/akash";

    /**
     * Deployments-related endpoints
     */
    public static class Deployments {

        private static final String DEPLOYMENTS = BASE_PATH + "/deployment/v1beta2/deployments/";

        /**
         * Get deployments list
         */
        public static final String LIST = DEPLOYMENTS + "list";

        /**
         * Get info on a deployment
         */
        public static final String INFO = DEPLOYMENTS + "info";
    }

    /**
     * Bids-related endpoints
     */
    public static class Bids {

        private static final String BID = BASE_PATH + "/market/v1beta2/bids/";

        /**
         * Get bids list
         */
        public static final String LIST = BID + "list";

        /**
         * Get info on a bid
         */
        public static final String INFO = BID + "info";

    }

    /**
     * Leases-related endpoints
     */
    public static class Leases {

        private static final String LEASE = BASE_PATH + "/market/v1beta2/leases/";

        /**
         * Get leases list
         */
        public static final String LIST = LEASE + "list";

        /**
         * Get info on a lease
         */
        public static final String INFO = LEASE + "info";
    }

    private ApiEndpoints() {}
}
