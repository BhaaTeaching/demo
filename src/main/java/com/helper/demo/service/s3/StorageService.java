package com.helper.demo.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.HttpMethod;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

@Service
@Slf4j
public class StorageService {

    @Value("${app.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    public String uploadFile(MultipartFile file) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        PutObjectResult putObjectResult = s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();

        return "File uploaded : " + fileName;
    }


    public byte[] downloadFile(String fileName) {
        Date expiration = new Date();
        long expirationMillis = expiration.getTime();
        expirationMillis += 1000 * 60 * 10; // Add 10 minutes
        expiration.setTime(expirationMillis);

        GeneratePresignedUrlRequest presignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
                .withMethod(HttpMethod.PUT) // Use PUT for uploading files
                .withExpiration(expiration);

        URL presignedUrl = s3Client.generatePresignedUrl(presignedUrlRequest);

        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " removed ...";
    }


    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}
