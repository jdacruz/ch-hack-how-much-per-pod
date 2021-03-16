package com.cloudhealth.hackaton;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import net.minidev.json.JSONArray;

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
    public Double calculateHowMuchPerPod(String cluster, int replicas, double cpuMillicores) {

        int totalClusterCpuMillicores = queryTotalCpuInCluster(cluster);

        Double totalClusterCost = queryTotalClusterCost();

        Double costPerMillicore = totalClusterCost/totalClusterCpuMillicores;

        double ourTotalServiceCpuRequirements = replicas * cpuMillicores;

        return costPerMillicore*ourTotalServiceCpuRequirements;
    }

    private Double queryTotalClusterCost() {
        String API_URL_TOTAL_AVAILABLE_CPU_IN_CLUSTER = "https://chapi.cloudhealthtech.com/olap_reports/containers/cost_history";

        WebResource webResource = client.resource(API_URL_TOTAL_AVAILABLE_CPU_IN_CLUSTER)
            .queryParam("interval", "monthly")
            .queryParam("filters[]", "Groupset-5841155525403:select:5841155596072")
            .queryParam("filters[]", "time:select:-2");

        ClientResponse response = webResource
                .accept("application/json")
                .header("Authorization", format("Bearer %s", apiKey))
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        String output = response.getEntity(String.class);
        DocumentContext parsed = JsonPath.parse(output);
        Object value = parsed.read("['data'][0]", JSONArray.class).get(0);
        return Double.valueOf(value.toString());
    }

    private int queryTotalCpuInCluster(String cluster) {
        String API_URL_TOTAL_AVAILABLE_CPU_IN_CLUSTER = "https://chapi.cloudhealthtech.com/olap_reports/containers/k8s_resources";

        WebResource webResource = client.resource(API_URL_TOTAL_AVAILABLE_CPU_IN_CLUSTER)
                .queryParam("interval", "monthly")
                .queryParam("measures[]", "avail_cpus_normalized")
                .queryParam("filters[]", format("ContainerCluster:select:%s", cluster))
                .queryParam("filters[]", "time:select:-2");

        ClientResponse response = webResource
                .accept("application/json")
                .header("Authorization", format("Bearer %s", apiKey))
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        // @TODO: probably add some defensive error handling
        String output = response.getEntity(String.class);
        DocumentContext parsed = JsonPath.parse(output);
        Object value = parsed.read("['data'][0]", JSONArray.class).get(0);
        return Double.valueOf(value.toString()).intValue();
    }
}