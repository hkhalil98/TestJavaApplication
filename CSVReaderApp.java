import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.io.Reader;
import java.util.List;

public class CSVReaderApp {

    private static final String CSV_FILE_PATH = "path/to/your/csvfile.csv";
    private static final String API_ENDPOINT = "http://localhost:8080/api/customers";

    public static void main(String[] args) {
        try {
            List<Customer> customers = readCSVFile(CSV_FILE_PATH);
            sendToApi(customers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Customer> readCSVFile(String filePath) throws Exception {
        Reader reader = new FileReader(filePath);
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());

        List<Customer> customers = CustomerMapper.mapToCustomers(csvParser.getRecords());

        csvParser.close();
        return customers;
    }

    private static void sendToApi(List<Customer> customers) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        for (Customer customer : customers) {
            HttpEntity<String> request = new HttpEntity<>(new ObjectMapper().writeValueAsString(customer), headers);
            restTemplate.exchange(API_ENDPOINT, HttpMethod.POST, request, String.class);
        }
    }
}
