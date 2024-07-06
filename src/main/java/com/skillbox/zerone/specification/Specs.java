package com.skillbox.zerone.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface Specs {
    private static <T, V> Specification<T> isEmptyValue(V value, Supplier<Specification<T>> supplier) {
        if ((value == null) || (value instanceof String str && str.isEmpty()) ||
            (value instanceof Integer num && num < 1) || value instanceof List<?> list && list.isEmpty() ||
            (value instanceof Map<?, ?> map && map.size() < 2)) {
            return (r, q, c) -> null;
        }
        return supplier.get();
    }

    static <T, V> Specification<T> eq(SingularAttribute<T, V> field, V value) {
        return isEmptyValue(value, () -> (root, query, cb) -> {
            if (value instanceof String str) {
                return cb.equal(cb.lower(root.get(field.getName())), str.toLowerCase());
            }
            return cb.equal(root.get(field), value);
        });
    }

    static <T, T2, V> Specification<T> eq(SingularAttribute<T, T2> table, SingularAttribute<T2, V> field, V value) {
        return isEmptyValue(value, () -> (root, query, cb) -> {
            if (value instanceof String str) {
                return cb.equal(cb.lower(root.join(table.getName()).get(field.getName())), str.toLowerCase());
            }
            return cb.equal(root.join(table).get(field), value);
        });
    }

    static <T, T2, V> Specification<T> eq(ListAttribute<T, T2> table, SingularAttribute<T2, V> field, V value) {
        return isEmptyValue(value, () -> (root, query, cb) -> {
            if (value instanceof String str) {
                return cb.equal(cb.lower(root.get(table.getName()).get(field.getName())), str.toLowerCase());
            }
            return cb.equal(root.join(table).get(field), value);
        });
    }

    static <T, V> Specification<T> notEq(SingularAttribute<T, V> field, V value) {
        return isEmptyValue(value, () -> (root, query, cb) -> {
            if (value instanceof String str) {
                return cb.notEqual(cb.lower(root.get(field.getName())), str.toLowerCase());
            }
            return cb.notEqual(root.get(field), value);
        });
    }

    static <T> Specification<T> like(SingularAttribute<T, String> field, String value) {
        return isEmptyValue(value, () -> (root, query, cb) -> cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%"));
    }

    static <T, T2> Specification<T> like(SingularAttribute<T, T2> table, SingularAttribute<T2, String> field, String value) {
        return isEmptyValue(value, () -> (root, query, cb) -> cb.like(cb.lower(root.join(table).get(field)), "%" + value.toLowerCase() + "%"));
    }

    static <T, V> Specification<T> lessEq(SingularAttribute<T, V> field, Integer value) {
        return isEmptyValue(value, () -> (root, query, cb) -> cb.lessThanOrEqualTo(root.get(field.getName()), LocalDateTime.now().minusYears(value)));
    }

    static <T, V> Specification<T> lessEq(SingularAttribute<T, V> field, LocalDateTime value) {
        return isEmptyValue(value, () -> (root, query, cb) -> cb.lessThanOrEqualTo(root.get(field.getName()), value));
    }

    static <T, V> Specification<T> greaterEq(SingularAttribute<T, V> field, Integer value) {
        return isEmptyValue(value, () -> (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(field.getName()), LocalDateTime.now().minusYears(value)));
    }

    static <T, V> Specification<T> greaterEq(SingularAttribute<T, V> field, LocalDateTime value) {
        return isEmptyValue(value, () -> (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(field.getName()), value));
    }

    static <T, V> Specification<T> between(SingularAttribute<T, V> field, LocalDateTime value) {
        return isEmptyValue(value, () -> (root, query, cb) -> cb.between(root.get(field.getName()), value.minusYears(5), value.plusYears(5)));
    }

    static <T, T2, V> Specification<T> in(ListAttribute<T, T2> table, SingularAttribute<T2, V> field, List<V> list) {
        return isEmptyValue(list, () -> (root, query, cb) -> root.join(table).get(field).in(list));
    }

    static <T, T2> Specification<T> joinOr(SingularAttribute<T, T2> table, Map<SingularAttribute<T2, String>, String> columns) {
        return isEmptyValue(columns, () -> (root, query, cb) -> {
            final Join<T, T2> joinTable = root.join(table);
            final List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<SingularAttribute<T2, String>, String> column : columns.entrySet()) {
                if (!column.getValue().isEmpty()){
                    predicates.add(cb.like(cb.lower(joinTable.get(column.getKey())), "%" + column.getValue().toLowerCase() + "%"));
                }
            }
            return predicates.isEmpty() ? null : cb.or(predicates.toArray(new Predicate[0]));
        });
    }

    static <T, V> Specification<T> isNull(SingularAttribute<T, V> field) {
        return (root, query, cb) -> cb.isNull(root.get(field));
    }

    static <T, V> Specification<T> isNotNull(SingularAttribute<T, V> field) {
        return (root, query, cb) -> cb.isNotNull(root.get(field));
    }

    static <T, V, V2> Specification<T> funcEq(String funcName, String unit, SingularAttribute<T, V> field, V2 value) {
        return (root, query, cb) -> cb.equal(cb.function(funcName, value.getClass(), cb.literal(unit), root.get(field)), value);
    }
}
