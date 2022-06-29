package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class TaskPageDto extends PageDto {
    private List<TaskOutDto> entity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskPageDto that)) return false;
        if (!super.equals(o)) return false;

        return Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (entity != null ? entity.hashCode() : 0);
        return result;
    }
}
