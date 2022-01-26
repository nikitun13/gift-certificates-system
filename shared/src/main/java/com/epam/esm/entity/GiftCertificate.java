package com.epam.esm.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class GiftCertificate {

    private Long id;
    private String name;
    private String description;
    private Long price;
    private Integer duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    public GiftCertificate(Long id, String name, String description, Long price,
                           Integer duration, LocalDateTime createDate, LocalDateTime lastUpdateDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public GiftCertificate() {
    }

    public static GiftCertificateBuilder builder() {
        return new GiftCertificateBuilder();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getPrice() {
        return price;
    }

    public Integer getDuration() {
        return duration;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificate that = (GiftCertificate) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(price, that.price)
                && Objects.equals(duration, that.duration)
                && Objects.equals(createDate, that.createDate)
                && Objects.equals(lastUpdateDate, that.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration, createDate, lastUpdateDate);
    }

    @Override
    public String toString() {
        return "GiftCertificate{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", description='" + description + '\''
                + ", price=" + price
                + ", duration=" + duration
                + ", createDate=" + createDate
                + ", lastUpdateDate=" + lastUpdateDate
                + '}';
    }

    public static class GiftCertificateBuilder {

        private Long id;
        private String name;
        private String description;
        private Long price;
        private Integer duration;
        private LocalDateTime createDate;
        private LocalDateTime lastUpdateDate;

        GiftCertificateBuilder() {
        }

        public GiftCertificateBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public GiftCertificateBuilder name(String name) {
            this.name = name;
            return this;
        }

        public GiftCertificateBuilder description(String description) {
            this.description = description;
            return this;
        }

        public GiftCertificateBuilder price(Long price) {
            this.price = price;
            return this;
        }

        public GiftCertificateBuilder duration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public GiftCertificateBuilder createDate(LocalDateTime createDate) {
            this.createDate = createDate;
            return this;
        }

        public GiftCertificateBuilder lastUpdateDate(LocalDateTime lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
            return this;
        }

        public GiftCertificate build() {
            return new GiftCertificate(id, name, description, price, duration, createDate, lastUpdateDate);
        }
    }
}