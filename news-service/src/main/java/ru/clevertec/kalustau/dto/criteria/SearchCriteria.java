package ru.clevertec.kalustau.dto.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class representing simple constraints for an entity specification
 * @author Dzmitry Kalustau
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;

    private boolean orPredicate;

    public SearchCriteria(String key, SearchOperation operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public boolean isOrPredicate() {
        return orPredicate;
    }
}