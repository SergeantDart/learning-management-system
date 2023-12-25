package csie.bdsa.lms.authservice.mapper;

import csie.bdsa.lms.authservice.model.User;
import csie.bdsa.lms.shared.dto.UserDetailsDto;
import csie.bdsa.lms.shared.dto.UserDto;
import csie.bdsa.lms.shared.mapper.BaseMapper;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserDetailsDto, Long> {

    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);

    List<UserDto> userToUserDtoList(List<User> users);

    List<User> userDtoToUserList(List<UserDto> userDtos);
}
