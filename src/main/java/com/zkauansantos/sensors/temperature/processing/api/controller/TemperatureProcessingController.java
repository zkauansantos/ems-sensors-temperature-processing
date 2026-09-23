package com.zkauansantos.sensors.temperature.processing.api.controller;

import com.zkauansantos.sensors.temperature.processing.api.model.TemperatureLogOutput;
import com.zkauansantos.sensors.temperature.processing.common.IdGenerator;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;

import static com.zkauansantos.sensors.temperature.processing.infra.rabbitmq.RabbitMQConfig.FANOUT_EXCHANGE_NAME;


@RestController
@RequestMapping("/api/sensors/{sensorId}/temperatures/data")
@Slf4j
@RequiredArgsConstructor
public class TemperatureProcessingController {
    private final RabbitTemplate rabbitTemplate;

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public void data(@PathVariable TSID sensorId, @RequestBody String input) {
        if(input == null || input.isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Double temperature;

        try {
            temperature = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        TemperatureLogOutput output = TemperatureLogOutput.builder()
                .id(IdGenerator.generateTimeBaseUUID())
                .sensorId(sensorId)
                .value(temperature)
                .registeredAt(OffsetDateTime.now())
                .build();

        log.info(output.toString());

        String exchange = FANOUT_EXCHANGE_NAME;
        String routingKey = "sensorTemperature";

        rabbitTemplate.convertAndSend(exchange, routingKey, output);
    }
}
