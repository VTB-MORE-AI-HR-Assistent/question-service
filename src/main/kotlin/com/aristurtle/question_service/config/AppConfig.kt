package com.aristurtle.question_service.config

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    @Bean
    fun modelMapper() : ModelMapper {
        val modelMapper = ModelMapper()
        modelMapper.configuration.apply {
            isSkipNullEnabled = true
            isFieldMatchingEnabled = true
            fieldAccessLevel = org.modelmapper.config.Configuration.AccessLevel.PRIVATE
        }
        return modelMapper
    }

}