package com.example.elevendash.global.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Uploader s3Uploader;

    public static final String CATEGORY_MEMBER = "member";
    public static final String CATEGORY_STORE = "store";
    public static final String CATEGORY_MENU = "menu";

    public UploadImageInfo uploadMemberProfileImage(MultipartFile image) {
        return s3Uploader.uploadMultipartFileToBucket(CATEGORY_MEMBER, image);
    }

    public UploadImageInfo uploadStoreImage(MultipartFile image) {
        return s3Uploader.uploadMultipartFileToBucket(CATEGORY_STORE, image);
    }

    public UploadImageInfo uploadMenuImage(MultipartFile image) {
        return s3Uploader.uploadMultipartFileToBucket(CATEGORY_MENU, image);
    }
}