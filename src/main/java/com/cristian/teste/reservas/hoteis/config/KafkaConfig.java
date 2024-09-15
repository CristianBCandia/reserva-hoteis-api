package com.cristian.teste.reservas.hoteis.config;

import com.cristian.teste.reservas.hoteis.dto.NotificacaoDTO;
import com.cristian.teste.reservas.hoteis.dto.ReservaDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
public class KafkaConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ReservaDTO> reservaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ReservaDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        // Configure other properties if needed
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NotificacaoDTO> notificacaoListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NotificacaoDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(notificacaoConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, NotificacaoDTO> notificacaoConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notificacao-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, NotificacaoDTO.class.getName());
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(NotificacaoDTO.class));
    }

    @Bean
    public ConsumerFactory<String, ReservaDTO> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "criacao-reserva-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ReservaDTO.class.getName());
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(ReservaDTO.class));
    }

}

