# Android Goo.gl Shortener Library

A simple tested Android library to shorten long urls in your app!

# How to include it
This library can be found in maven central repo. If you're using Android studio you can include it by writing the following in the corresponding _dependencies_ block

#### Gradle:
```groovy
dependencies {
	// ...
	compile 'com.andreabaccega:googlshortenerlib:1.0.0'
	// ...
}
```

####Maven
```xml
		<dependency>
			<groupId>com.andreabaccega</groupId>
			<artifactId>googlshortenerlib</artifactId>
			<version>${version}</version>
			<type>aar</type>
			<scope>provided</scope>
		</dependency>
```


# How to use


```java
	GoogleShortenerPerformer shortener = new GoogleShortenerPerformer(new OkHttpClient());

	String longUrl = "http://www.andreabaccega.com/";

	GooglShortenerResult result = shortener.shortenUrl(
		new GooglShortenerRequestBuilder()
			.buildRequest(longUrl)
		);

	if ( Status.SUCCESS.equals(result.getStatus()) ) {
		// all ok result.getShortenedUrl() contains the shortened url!
	} else if ( Status.IO_EXCEPTION.equals(result.getStatus()) ) {
		// connectivity error. result.getException() returns the thrown exception while performing
		// the request to google servers!
	} else {
		// Status.RESPONSE_ERROR
		// this happens if google replies with an unexpected response or if there are some other issues processing
		// the result.

		// result.getException() contains a GooglShortenerException containing a message that can help resolve the issue!
	}
```

We do recommend to [request an apiKey](https://developers.google.com/url-shortener/v1/getting_started#APIKey) and use the following method instead of the above!

```java
	new GooglShortenerRequestBuilder()
			.buildRequest(
				longUrl,
				apiKey)
		)
```
# Dependencies

This library depends on the following artifacts:
*  com.squareup.okhttp:okhttp:2.0.0
*  com.google.code.gson:gson:2.3

They're automatically resolved by gradle but you can always exclude them (if you're using another compatible version) by doing so:

```groovy
dependencies {
	// ...
	compile ('com.andreabaccega:googlshortenerlib:1.0.0') {
		exclude module: 'okhttp'
		exclude module: 'gson'
	}
	// ...
}
```

# Author

*  Andrea Baccega <me@andreabaccega.com> - _Author/Ideator of the library_

