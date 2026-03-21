package com.lumiera.shop.lumierashop.service;

import com.lumiera.shop.lumierashop.domain.Product;
import com.lumiera.shop.lumierashop.dto.request.CreateProductForm;
import com.lumiera.shop.lumierashop.dto.request.UpdateProductForm;
import com.lumiera.shop.lumierashop.dto.response.ProductListResponse;
import com.lumiera.shop.lumierashop.dto.response.ProductResponse;
import com.lumiera.shop.lumierashop.global.exception.exception.CustomException;
import com.lumiera.shop.lumierashop.mapper.AdminProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.lumiera.shop.lumierashop.global.exception.code.ErrorCode.PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminProductService {

    private final AdminProductMapper productMapper;
    private final ProductDetailImageService productDetailImageService;
    private final FileUploadService fileUploadService;

    public List<ProductListResponse> getProductList(
            int offset, int limit, @Nullable String keyword, boolean deleted, @Nullable Long categoryId
    ) {
        return productMapper.findAll(offset, limit, keyword, deleted, categoryId);
    }

    public int getTotalCount(@Nullable String keyword, boolean deleted, @Nullable Long categoryId) {
        return productMapper.count(keyword, deleted, categoryId);
    }

    @Transactional
    public Long createProduct(CreateProductForm form) {
        String thumbnailUrl = fileUploadService.saveImage(form.getThumbnail());

        Product product = new Product(
                thumbnailUrl,
                form.getCategoryId(),
                form.getName(),
                form.getPrice(),
                form.getStockQuantity()
        );

        productMapper.save(product);

        List<String> detailImageUrls = fileUploadService.saveImages(form.getDetailImages());
        productDetailImageService.saveDetailImages(product.getId(), detailImageUrls);

        return product.getId();
    }

    public ProductResponse getProduct(Long productId) {
        ProductResponse product = productMapper.findById(productId);

        if (product == null) {
            throw new CustomException(PRODUCT_NOT_FOUND);
        }

        return product;
    }

    @Transactional
    public void updateProduct(Long productId, UpdateProductForm form) {
        Product product = new Product(
                getThumbnailUrl(form.getThumbnail()),
                form.getCategoryId(),
                form.getName(),
                form.getPrice(),
                form.getStockQuantity()
        );

        validateAffectedRows(productMapper.update(productId, product));
        replaceDetailImages(productId, form.getDetailImages());
    }

    @Transactional
    public void deleteProduct(Long productId) {
        validateAffectedRows(productMapper.delete(List.of(productId)));
        productDetailImageService.deleteDetailImages(productId);
    }

    @Transactional
    public int deleteProducts(List<Long> productIds) {
        List<Long> distinctIds = productIds.stream().distinct().toList();
        int deletedCount = productMapper.delete(distinctIds);

        if (deletedCount > 0) {
            productDetailImageService.deleteDetailImages(distinctIds);
        }

        return deletedCount;
    }

    private void validateAffectedRows(int affectedRows) {
        if (affectedRows == 0) {
            throw new CustomException(PRODUCT_NOT_FOUND);
        }
    }

    private String getThumbnailUrl(MultipartFile thumbnail) {
        if (thumbnail == null || thumbnail.isEmpty()) {
            return null;
        }

        return fileUploadService.saveImage(thumbnail);
    }

    private void replaceDetailImages(Long productId, List<MultipartFile> detailImages) {
        List<MultipartFile> uploadFiles = getUploadFiles(detailImages);

        if (uploadFiles.isEmpty()) {
            return;
        }

        productDetailImageService.deleteDetailImages(productId);

        List<String> imageUrls = fileUploadService.saveImages(uploadFiles);
        productDetailImageService.saveDetailImages(productId, imageUrls);
    }

    private List<MultipartFile> getUploadFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return List.of();
        }

        List<MultipartFile> uploadFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                uploadFiles.add(file);
            }
        }

        return uploadFiles;
    }
}