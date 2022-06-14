package ru.sitronics.tn.taskservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ObjectUtils {

    public static <T, D> D convertObject(T source, D target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static <T, D> D convertObject(T source, D target, String fields) {
        if (fields != null) {
            Set<String> selectedFields = Arrays.stream(fields.split(" *, *")).collect(Collectors.toSet());
            String[] excludedProperties =
                    Arrays.stream(BeanUtils.getPropertyDescriptors(target.getClass()))
                            .map(PropertyDescriptor::getName)
                            .filter(name -> !selectedFields.contains(name))
                            .toArray(String[]::new);
            BeanUtils.copyProperties(source, target, excludedProperties);
            return target;
        }
        return target;
    }
}
