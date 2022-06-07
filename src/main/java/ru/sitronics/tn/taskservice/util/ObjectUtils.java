package ru.sitronics.tn.taskservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ObjectUtils {

    public static <T, D> D convertObject(T source, D target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }
}
