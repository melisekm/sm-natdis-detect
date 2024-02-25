package com.pep.ProxyEntryPoint.converter;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public abstract class AbstractConverter<I, O, E> {

    public abstract E convertToEntity(I input);
    public abstract O convertToOutput(E entity);

    public List<O> convertToOutputList(List<E> entities) {
        return entities.stream()
                .map(this::convertToOutput)
                .collect(Collectors.toList());
    }
}