package ${package}.interfaces.core.domain.user.mapper;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @since 2025-12-29
 */
@Configuration
public class UserRestMapperConfiguration {
    @Bean
    public UserAPIMapper userAPIMapper() {
        return Mappers.getMapper(UserAPIMapper.class);
    }
}
