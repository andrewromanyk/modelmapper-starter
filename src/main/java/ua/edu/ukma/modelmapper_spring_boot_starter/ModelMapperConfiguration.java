package ua.edu.ukma.modelmapper_spring_boot_starter;

import org.modelmapper.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({Event.class, EventDto.class, EventEntity.class, Building.class, BuildingEntity.class, BuildingDto.class, BuildingServiceImpl.class})
public class ModelMapperConfiguration {

    @Bean
    public BuildingService buildingService() {
        System.out.println("created dummy");
        return new BuildingServiceImpl();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapperResult = new ModelMapper();

        TypeMap<Event, EventDto> eventMapper = mapperResult.createTypeMap(Event.class, EventDto.class);
        eventMapper.addMappings(mapper -> mapper.map(src -> src.getBuilding().getId(), EventDto::setBuilding));

        Converter<Long, BuildingEntity> toBuildingConverter = ctx -> mapperResult.map(buildingService().getBuildingById(ctx.getSource()), BuildingEntity.class);
        TypeMap<EventDto, EventEntity> eventMapperRev = mapperResult.createTypeMap(EventDto.class, EventEntity.class);
        eventMapperRev.addMappings(mapper -> mapper.using(toBuildingConverter).map(EventDto::getBuilding, EventEntity::setBuilding));

        System.out.println("Created mm bean!");

        return mapperResult;
    }

}
