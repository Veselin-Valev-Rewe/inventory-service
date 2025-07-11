package com.example.inventory_service.mapper;

import com.example.inventory_service.data.entity.Product;
import com.example.inventory_service.dto.product.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    void updateProductFromDto(ProductDto dto, @MappingTarget Product product);

    Product toProduct(ProductDto productDto);
}
