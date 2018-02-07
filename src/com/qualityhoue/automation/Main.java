package com.qualityhoue.automation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;

public class Main {

	private static HttpTransport httpTransport;
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String APPLICATION_NAME = "DAIMTO-GoogleAnalyticsReportingSample/1.0";
	private static FileDataStoreFactory dataStoreFactory;

	private static String TOKEN = "ya29.GltaBeqGKdJD_T1UCauws3k90vYaqI7mkqx6zj-4FomWqJk_"
			+ "CbS9JOnOkXBBL-vYsusUvxEFV9HmV9f5GQU9oiFKl65Ckezgua" + "FWHrxjDd9eRatfvnvLEeot9kd3";

	public static void main(String[] args) {
		GoogleCredential credentaial = new GoogleCredential().setAccessToken(TOKEN);
		DateRange dateRange = new DateRange();
		dateRange.setStartDate("2018-02-06");
		dateRange.setEndDate("2018-02-07");

		Metric sessions = new Metric().setExpression("ga:sessions").setAlias("sessions");

		Dimension browser = new Dimension().setName("ga:browser");

		ReportRequest request = new ReportRequest().setViewId("ga:135032645").setDateRanges(Arrays.asList(dateRange))
				.setDimensions(Arrays.asList(browser)).setMetrics(Arrays.asList(sessions));

		ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
		requests.add(request);

		GetReportsRequest getReport = new GetReportsRequest().setReportRequests(requests);

		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();

			Credential credential = authorize();
			AnalyticsReporting analyticsreporting = new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY,
					credential).setApplicationName(APPLICATION_NAME).build();

			GetReportsResponse response = analyticsreporting.reports().batchGet(getReport).execute();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static Credential authorize() throws Exception {
		// load client secrets

		String workingDir = System.getProperty("user.dir");
		System.out.println("Current working directory : " + workingDir);
		String b;
		BufferedReader io = new BufferedReader(new FileReader("./client_secrets.json"));

		try {
			StringBuilder sb = new StringBuilder();
			String line = io.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = io.readLine();
			}
			String everything = sb.toString();
			System.out.println(everything);
		} finally {
			io.close();
		}

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
				new InputStreamReader(Main.class.getResourceAsStream("./client_secrets.json")));

		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
				clientSecrets, Collections.singleton(AnalyticsReportingScopes.ANALYTICS_READONLY))
						.setDataStoreFactory(dataStoreFactory).build();
		// authorize
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}

}
