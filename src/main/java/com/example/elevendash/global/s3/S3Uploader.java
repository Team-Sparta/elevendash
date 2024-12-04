package com.example.elevendash.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    public static final String DATE_FORMAT_YYYYMMDD = "yyyy/MM/dd";

    public UploadImageInfo uploadMultipartFileToBucket(String category, MultipartFile file) {
        String filePath = getFilePath(category, file.getName());
        ObjectMetadata metadata = createMetadataFromFile(file);

        try (var inputStream = file.getInputStream()) {
            amazonS3.putObject(
                    new PutObjectRequest(bucket, filePath, inputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );
        } catch (Exception e) {
            log.error("S3 파일 업로드 실패. category: {}, fileName: {}, error: {}",
                    category, file.getName(), e.getMessage(), e);
            throw new BaseException(ErrorCode.S3_UPLOADER_ERROR);
        }

        return new UploadImageInfo(getUrlFromBucket(filePath));
    }

    private String getFilePath(String category, String fileName) {
        return category + File.separator + createDatePath() + File.separator + generateRandomFilePrefix() + fileName;
    }

    private String createDatePath() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_YYYYMMDD);

        return now.format(dateTimeFormatter);
    }

    private ObjectMetadata createMetadataFromFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BaseException(ErrorCode.S3_UPLOADER_ERROR);
        }
        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        return metadata;
    }

    private String generateRandomFilePrefix() {
        String randomUUID = UUID.randomUUID().toString();
        String cleanedUUID = randomUUID.replace("-", "");
        return cleanedUUID.substring(0, 16);
    }

    private String getUrlFromBucket(String fileKey) {
        try {
            return amazonS3.getUrl(bucket, fileKey).toString();
        } catch (Exception e) {
            log.error("S3 URL 생성 실패. bucket: {}, fileKey: {}, error: {}",
                    bucket, fileKey, e.getMessage(), e);
            throw new BaseException(ErrorCode.S3_UPLOADER_ERROR);
        }
    }

}