package ${package}.infrastructure.database.mapper;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @since 2025-12-28
 */
@Configuration
public class DatasourceEntityMapperConfiguration {

    @Bean
    public UserEntityMapper userEntityMapper(){
        return Mappers.getMapper(UserEntityMapper.class);
    }
}
