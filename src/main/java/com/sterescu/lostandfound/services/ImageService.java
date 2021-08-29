package com.sterescu.lostandfound.services;

import com.sterescu.lostandfound.entities.Image;
import com.sterescu.lostandfound.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image save(MultipartFile file) throws IOException {
        Image fileEntity = Image.builder()
                .name(StringUtils.cleanPath(file.getOriginalFilename()))
                .contentType(file.getContentType())
                .data(file.getBytes())
                .size(file.getSize())
                .build();

        return imageRepository.save(fileEntity);
    }

    public Optional<Image> getImage(String id) {
        return imageRepository.findById(id);
    }

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }
}