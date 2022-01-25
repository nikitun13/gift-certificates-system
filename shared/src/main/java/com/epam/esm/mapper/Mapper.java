package com.epam.esm.mapper;

/**
 * The class {@code Mapper} is an interface
 * that maps {@code Entities} to {@code DTOs} and vice versa.
 *
 * @param <E> type of the {@code Entity}.
 * @param <T> type of the {@code Dto}.
 */
public interface Mapper<E, T> {

    /**
     * Maps {@code Dto} to {@code Entity}.
     *
     * @param dto to be mapped to {@code Entity}.
     * @return mapped {@code entity} from the {@code dto}.
     */
    E mapToEntity(T dto);

    /**
     * Maps {@code Entity} to {@code Dto}.
     *
     * @param entity to be mapped to {@code dto}.
     * @return mapped {@code dto} from the {@code entity}.
     */
    T mapToDto(E entity);
}
