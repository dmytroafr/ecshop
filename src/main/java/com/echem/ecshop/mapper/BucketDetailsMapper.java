package com.echem.ecshop.mapper;

import com.echem.ecshop.domain.BucketDetails;
import com.echem.ecshop.dto.BucketDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BucketDetailsMapper {
    BucketDetailsMapper MAPPER = Mappers.getMapper(BucketDetailsMapper.class);

    BucketDetails toBucketDetails (BucketDetailsDTO bucketDetailsDTO);
    BucketDetailsDTO fromBucketDetails (BucketDetails bucketDetails);
    List<BucketDetails> toBucketDetailsList (List<BucketDetailsDTO> bucketDetailsDTOList);
    List<BucketDetailsDTO> fromBucketDetailsList (List<BucketDetails> bucketDetailsList);
}
