package ru.abarigena.NauJava.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * Конфигурация для работы с S3-совместимым хранилищем (Yandex Object Storage).
 */
@Configuration
public class S3Config {

    @Value("${aws.s3.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.s3.secretAccessKey}")
    private String secretAccessKey;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    /**
     * Создание и настройка клиента Amazon S3.
     *
     * @return экземпляр AmazonS3 для взаимодействия с хранилищем
     */
    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new AmazonS3ClientBuilder.EndpointConfiguration(
                                "https://storage.yandexcloud.net",  // Endpoint для Yandex S3
                                "ru-central1"                      // Регион Yandex Cloud
                        )
                )
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

}
