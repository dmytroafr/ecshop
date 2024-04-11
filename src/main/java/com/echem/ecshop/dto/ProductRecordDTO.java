package com.echem.ecshop.dto;

import com.echem.ecshop.domain.Category;
import com.echem.ecshop.domain.OnStock;

import java.math.BigDecimal;
import java.util.List;

public record ProductRecordDTO  (Long id,
                                 String title,
                                 BigDecimal price,
                                 String productDescription,
                                 String photoUrl,
                                 String producer,
                                 String countryProducer,
                                 OnStock onStock
){
}
