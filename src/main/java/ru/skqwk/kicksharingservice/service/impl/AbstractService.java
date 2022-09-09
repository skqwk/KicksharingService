package ru.skqwk.kicksharingservice.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;

public class AbstractService<T> {
    private final JpaRepository<T, Long> repository;

    public AbstractService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }
}
