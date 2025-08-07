package dev.gutemberg.rate.limiter.vendors.spring.uploader;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.File;
import java.net.URI;

public class UploadRateLimitConfigFile {
    public static void main(final String[] args) {
        final var credentials = AwsBasicCredentials.create("test", "test");
        final var s3Client = S3Client.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .endpointOverride(URI.create("http://localhost:4566"))
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
                .build();
        final var file = new File("C:\\Users\\gutem\\workspace\\rate-limiter\\limits.yml");
        System.out.println(file.exists());
        final var request = PutObjectRequest.builder()
                .bucket("ratelimiter-configs")
                .key("limits.yml")
                .build();
        s3Client.putObject(request, RequestBody.fromFile(file));
    }
}
