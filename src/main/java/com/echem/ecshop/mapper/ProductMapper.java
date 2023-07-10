package com.echem.ecshop.mapper;

import com.echem.ecshop.domain.Product;
import com.echem.ecshop.dto.ProductDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper  {
    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);
    Product toProduct(ProductDTO dto);
    @InheritInverseConfiguration
    ProductDTO fromProduct (Product product);
    List<Product> toProductList (List<ProductDTO> productDTO);
    List<ProductDTO> formProductList (List<Product> products);

}
