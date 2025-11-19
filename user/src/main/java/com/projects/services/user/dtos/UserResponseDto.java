package com.projects.services.user.dtos;

import com.projects.services.user.models.Roles;
import com.projects.services.user.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponseDto {
    private String name;
    private String email;
    private List<Roles> rolesList;
    public static UserResponseDto from(User user){
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setEmail(user.getEmail());
        responseDto.setName(user.getName());
        responseDto.setRolesList(user.getRoles());
        return  responseDto;
    }
}
