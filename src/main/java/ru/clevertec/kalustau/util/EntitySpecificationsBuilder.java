package ru.clevertec.kalustau.util;

import com.google.common.base.Joiner;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.kalustau.dto.criteria.SearchCriteria;
import ru.clevertec.kalustau.dto.criteria.SearchOperation;
import ru.clevertec.kalustau.model.News;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntitySpecificationsBuilder<E> {

    private List<SearchCriteria> params = new ArrayList<>();

    public EntitySpecificationsBuilder with(
            String key, String operation, Object value, String prefix, String suffix) {

        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) {
                boolean startWithAsterisk = prefix.contains("*");
                boolean endWithAsterisk = suffix.contains("*");

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH;
                }
            }
            params.add(new SearchCriteria(key, op, value));
        }
        return this;
    }

    public Specification<E> build() {
        if (params.size() == 0) {
            return null;
        }

        Specification result = new EntitySpecification(params.get(0));

        Specification finalResult = result;
        params.stream().forEach(criteria -> {
            if (criteria.isOrPredicate()) {
                Specification.where(finalResult).or(new EntitySpecification(criteria));
            } else
                Specification.where(finalResult).and(new EntitySpecification(criteria));
        });

        return result;
    }

    public Specification<E> getSpecification(String search) {
        EntitySpecificationsBuilder builder = new EntitySpecificationsBuilder();
        String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(4),
                    matcher.group(3),
                    matcher.group(5));
        }
        return builder.build();
    }
}
