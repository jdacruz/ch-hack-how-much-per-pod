package com.cloudhealth.hackaton;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.javamoney.moneta.Money;

import static java.lang.String.format;

public class PodCostCalculator {

    private final String apiKey;
    private final Client client;

    public PodCostCalculator(String cloudHealthApiKey) {
        this.apiKey = cloudHealthApiKey;
        client = Client.create();
    }

    /**
     * Calculates how much a microservice pod costs.
     * @param cluster the k8s cluster
     * @param granularity time granularity
     * @param replicas number of replicas of the pod
     * @param cpuMillicores cpu millicore size of the pod
     * @return The cost per pod.
     */
    public Money calculateHowMuchPerPod(String cluster, Granularity granularity, int replicas, int cpuMillicores) {

        int totalClusterCpuMillicores = calculateTotalCpuInCluster(cluster, granularity);

        Money totalClusterCost = calculateTotalClusterCost(cluster, granularity);

        Money costPerMillicore = totalClusterCost.divide(totalClusterCpuMillicores);

        int ourTotalServiceCpuRequirements = replicas * cpuMillicores;

        return costPerMillicore.multiply(ourTotalServiceCpuRequirements);
    }

    private Money calculateTotalClusterCost(String cluster, Granularity granularity) {
        // @TODO: find correct API (make cluster and granularity tokenizable)
        String API_URL_TOTAL_AVAILABLE_CPU_IN_CLUSTER = "";

        WebResource webResource = client.resource(format(API_URL_TOTAL_AVAILABLE_CPU_IN_CLUSTER, cluster, granularity));
        ClientResponse response = webResource
                .accept("application/json")
                .header("Authorization", format("Bearer: %s", apiKey))
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        String output = response.getEntity(String.class);
        // @TODO: parse output to find "most recent?" total by granularity requested
        return Money.of(450, "USD");
    }

    private int calculateTotalCpuInCluster(String cluster, Granularity granularity) {
        // @TODO: find correct API (make cluster and granularity tokenizable)
        String API_URL_TOTAL_AVAILABLE_CPU_IN_CLUSTER = "";

        WebResource webResource = client.resource(format(API_URL_TOTAL_AVAILABLE_CPU_IN_CLUSTER, cluster, granularity));
        ClientResponse response = webResource
                .accept("application/json")
                .header("Authorization", format("Bearer: %s", apiKey))
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        String output = response.getEntity(String.class);
        // @TODO: parse output to find "most recent?" total by granularity requested
        return 1000;
    }
}