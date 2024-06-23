package com.echem.ecshop.mapper;

import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);
//    User toUser(UserDTO userDto);
    UserDTO fromUser (User user);
//    List<User> toUserList (List<UserDTO> userDTOList);
//    List<UserDTO> fromUserList (List<User> users);
}
