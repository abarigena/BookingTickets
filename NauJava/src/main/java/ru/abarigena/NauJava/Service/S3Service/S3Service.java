package ru.abarigena.NauJava.Service.S3Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Autowired
    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    /**
     * Загружает изображение в S3 и возвращает URL.
     *
     * @param file загружаемый файл
     * @return URL изображения на S3
     * @throws IOException
     */
    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        // Загружаем файл в бакет
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), null));

        // Формируем URL для доступа к файлу
        return amazonS3.getUrl(bucketName, fileName).toString();
    }
}
