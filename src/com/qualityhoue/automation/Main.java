package com.qualityhoue.automation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.model.ColumnHeader;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.DateRangeValues;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;

public class Main {

	private static final String APPLICATION_NAME = "Hello Analytics Reporting";
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String KEY_FILE_LOCATION = "<REPLACE_WITH_JSON_FILE>";
	private static final String VIEW_ID = "<REPLACE_WITH_VIEW_ID>";

	public static void main(String[] args) {
		try {
			AnalyticsReporting service = initializeAnalyticsReporting();

			GetReportsResponse response = getReport(service);
			printResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//
	// GoogleCredential credentaial = new
	// GoogleCredential().setAccessToken(TOKEN);
	// DateRange dateRange = new
	// DateRange();dateRange.setStartDate("2018-02-06");dateRange.setEndDate("2018-02-07");
	//
	// Metric sessions = new
	// Metric().setExpression("ga:sessions").setAlias("sessions");
	//
	// Dimension browser = new Dimension().setName("ga:browser");
	//
	// ReportRequest request = new
	// ReportRequest().setViewId("ga:135032645").setDateRanges(Arrays.asList(dateRange))
	// .setDimensions(Arrays.asList(browser)).setMetrics(Arrays.asList(sessions));
	//
	// ArrayList<ReportRequest> requests = new
	// ArrayList<ReportRequest>();requests.add(request);
	//
	// GetReportsRequest getReport = new
	// GetReportsRequest().setReportRequests(requests);
	//
	// try
	// {
	// httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	//
	// Credential credential = authorize();
	// AnalyticsReporting analyticsreporting = new
	// AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
	// .setApplicationName(APPLICATION_NAME).build();
	//
	// GetReportsResponse response =
	// analyticsreporting.reports().batchGet(getReport).execute();
	// }catch(
	// GeneralSecurityException e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }catch(
	// IOException e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }catch(
	// Exception e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

	/**
	 * Initializes an Analytics Reporting API V4 service object.
	 *
	 * @return An authorized Analytics Reporting API V4 service object.
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	private static AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException {

		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(KEY_FILE_LOCATION))
				.createScoped(AnalyticsReportingScopes.all());

		// Construct the Analytics Reporting service object.
		return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
	}

	/**
	 * Queries the Analytics Reporting API V4.
	 *
	 * @param service
	 *            An authorized Analytics Reporting API V4 service object.
	 * @return GetReportResponse The Analytics Reporting API V4 response.
	 * @throws IOException
	 */
	private static GetReportsResponse getReport(AnalyticsReporting service) throws IOException {
		// Create the DateRange object.
		DateRange dateRange = new DateRange();
		dateRange.setStartDate("7DaysAgo");
		dateRange.setEndDate("today");

		// Create the Metrics object.
		Metric sessions = new Metric().setExpression("ga:sessions").setAlias("sessions");

		Dimension pageTitle = new Dimension().setName("ga:pageTitle");

		// Create the ReportRequest object.
		ReportRequest request = new ReportRequest().setViewId(VIEW_ID).setDateRanges(Arrays.asList(dateRange))
				.setMetrics(Arrays.asList(sessions)).setDimensions(Arrays.asList(pageTitle));

		ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
		requests.add(request);

		// Create the GetReportsRequest object.
		GetReportsRequest getReport = new GetReportsRequest().setReportRequests(requests);

		// Call the batchGet method.
		GetReportsResponse response = service.reports().batchGet(getReport).execute();

		// Return the response.
		return response;
	}

	/**
	 * Parses and prints the Analytics Reporting API V4 response.
	 *
	 * @param response
	 *            An Analytics Reporting API V4 response.
	 */
	private static void printResponse(GetReportsResponse response) {

		for (Report report : response.getReports()) {
			ColumnHeader header = report.getColumnHeader();
			List<String> dimensionHeaders = header.getDimensions();
			List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
			List<ReportRow> rows = report.getData().getRows();

			if (rows == null) {
				System.out.println("No data found for " + VIEW_ID);
				return;
			}

			for (ReportRow row : rows) {
				List<String> dimensions = row.getDimensions();
				List<DateRangeValues> metrics = row.getMetrics();

				for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
					System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
				}

				for (int j = 0; j < metrics.size(); j++) {
					System.out.print("Date Range (" + j + "): ");
					DateRangeValues values = metrics.get(j);
					for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
						System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
					}
				}
			}
		}
	}
}