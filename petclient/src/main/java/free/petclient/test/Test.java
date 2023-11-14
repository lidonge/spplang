package free.petclient.test;

import okhttp3.OkHttpClient;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.PetApi;
import org.openapitools.client.model.Pet;

/**
 * @author lidong@date 2023-11-12@version 1.0
 */
public class Test {
    public static void main(String[] args) throws ApiException {
        OkHttpClient okHttpClient = new OkHttpClient();
        ApiClient apiClient = new ApiClient();
        apiClient.setHttpClient(okHttpClient);
        PetApi petApi = new PetApi(apiClient);
        petApi.setCustomBaseUrl("http://localhost:8080/v3");
        Pet pet = new Pet();
        pet = petApi.addPet(pet);
        System.out.println(pet);
    }
}
