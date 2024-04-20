package com.echem.ecshop.mapper;

import com.echem.ecshop.domain.Product;
import com.echem.ecshop.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper  {
    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

    Product toProduct(ProductDTO dto);
    ProductDTO fromProduct (Product product);
    List<Product> toProductList (List<ProductDTO> productDTOList);
    List<ProductDTO> fromProductList (List<Product> products);
}