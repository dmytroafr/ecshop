package com.echem.ecshop.mapper;

import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    UserDTO fromUser (User user);
    List<UserDTO> fromUserList (List<User> users);

//    UserDetailsDTO detailsFromUser(User user);
}
