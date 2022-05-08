package kg.itschool.model;

import java.time.LocalDateTime;

abstract class BaseEntity {

    protected Long id;
    protected LocalDateTime dateCreated;

    protected BaseEntity(Long id) {
        this.id = id;
        dateCreated = LocalDateTime.now();
    }

    protected BaseEntity() {
        dateCreated = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }
}
