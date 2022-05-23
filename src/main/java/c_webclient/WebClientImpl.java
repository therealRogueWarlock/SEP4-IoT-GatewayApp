package c_webclient;

import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import util.ConsoleLogger;

public class WebClientImpl implements WebClient {
	private static WebClient instance;
	private final String ROOT = "http://sep4webapi-env.eba-2fmcgiei.eu-west-1.elasticbeanstalk.com/api/v1";
	private final RestTemplate rest = new RestTemplate();

	private WebClientImpl() {
	}

	public static WebClient getInstance() {
		if (instance == null) {
			instance = new WebClientImpl();
			return instance;
		}
		return instance;
	}

	@Override
	public Object put(String restUrl, Object obj) throws RestClientException {
		// Post is used the get back result-- spring put returns void
		ConsoleLogger.clDebug("Sending to URL: %s\nData\n%s\n", ROOT + restUrl, obj.toString()); // SOUT

		// HTTP Header
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		//
		ResponseEntity<String> result;
		result = rest.exchange(ROOT + restUrl, HttpMethod.POST, new HttpEntity<>(obj, headers), String.class);
		ConsoleLogger.clLog("Data has been sent to %s", ROOT); // SOUT
		return result.getBody();
	}

	@Override
	public Object post(String restUrl, Object obj) throws RestClientException {
		try {
			return rest.postForEntity(ROOT + restUrl, obj, String.class)
			           .getBody();
		} catch (RestClientException e) {
			ConsoleLogger.clWarn(e.getMessage());
			return null;
		}
	}

	@Override
	public Object get(String restUrl) throws RestClientException {
		try {
			return rest.getForEntity(ROOT + restUrl, String.class)
			           .getBody();
		} catch (RestClientException e) {
			ConsoleLogger.clWarn(e.getMessage());
			return null;
		}
	}

	@Override
	public boolean delete(String restUrl) throws RestClientException {

		try {
			rest.delete(ROOT + restUrl);
			return true;
		} catch (RestClientException e) {
			ConsoleLogger.clWarn(e.getMessage());
			return false;
		}
	}
}
