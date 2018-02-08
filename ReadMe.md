# Flow of setup engine that collects data from google analytics

## API

[tool for checking request](https://ga-dev-tools.appspot.com/query-explorer/)

The API version that is going to be use will be [Google Analytics Reporting API v4 API](https://developers.google.com/analytics/devguides/reporting/core/v4/).

> The Google Analytics Reporting API v4 is the most advanced programmatic method to access report data in Google Analytics. With the Google Analytics Reporting API, you can:
>
> Build custom dashboards to display Google Analytics data.
> Automate complex reporting tasks to save time.
> Integrate your Google Analytics data with other business applications.```

### Google Analytics Reporting APIv4 Features:

> Google Analytics is built upon a powerful data reporting infrastructure. 
> The Google Analytics Reporting API v4 gives you access to the power of the Google Analytics platform. 
> The API provides these key features:
>
> - Metric expressions
>
> The API allows you to request not only built-in metrics but also combination of metrics expressed in mathematical operations. 
> For example, you can use the expression `ga:goal1completions/ga:sessions` to request the goal completions per number of sessions.
>
> - Multiple date ranges
>
> The API allows you in a single request to get data in two date ranges.
>
> - Cohorts and Lifetime value
>
> The API has a rich vocabulary to request Cohort and Lifetime value reports.
> 
> - Multiple segments
> 
> The API enables you to get multiple segments in a single request. 

## Authorization Flows

There are are several ways to authorize your app in order to access Google account data.
[Read here for details](https://developers.google.com/analytics/devguides/reporting/core/v4/authorization)

The one that best fits our need is:

[**Service Accounts**](https://developers.google.com/analytics/devguides/reporting/core/v4/quickstart/service-java)
> To get started using Analytics Reporting API v4, you need to first [use the setup tool](https://console.developers.google.com/start/api?id=analyticsreporting.googleapis.com&credential=client_key), 
> which guides you through creating a project in the Google API Console, 
> enabling the API, and creating credentials.

---------------------

Core Reporting API and Management API

Service accounts are useful for automated, offline, or scheduled access to Google Analytics data for your own account. 
For example, to build a live dash-board of your own Google Analytics data and share it with other users.

To get started using Analytics API, you need to first use the setup tool, which guides you through creating a project in the Google API Console, 
enabling the API, and creating credentials.

To set up a new service account, do the following:

1. Click Create credentials > Service account key.
2. Choose whether to download the service account's public/private key as a standard P12 file, or as a JSON file that can be loaded by a Google API client library.

Your new public/private key pair is generated and downloaded to your machine; it serves as the only copy of this key. 
You are responsible for storing it securely.