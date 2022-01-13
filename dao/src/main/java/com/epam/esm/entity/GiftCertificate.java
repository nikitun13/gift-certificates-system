package com.epam.esm.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class GiftCertificate {

    private Long id;
    private String name;
    private String description;
    private Long price;
    private Integer duration;
    private LocalDateTime createTime;
    private LocalDateTime lastUpdateTime;

    public GiftCertificate(Long id, String name, String description, Long price,
                           Integer duration, LocalDateTime createTime, LocalDateTime lastUpdateTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createTime = createTime;
        this.lastUpdateTime = lastUpdateTime;
    }

    public GiftCertificate() {
    }

    public static GiftCertificateBuilder builder() {
        return new GiftCertificateBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Long getPrice() {
        return this.price;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    public LocalDateTime getLastUpdateTime() {
        return this.lastUpdateTime;
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

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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
                && Objects.equals(createTime, that.createTime)
                && Objects.equals(lastUpdateTime, that.lastUpdateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration, createTime, lastUpdateTime);
    }

    @Override
    public String toString() {
        return "GiftCertificate{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", description='" + description + '\''
                + ", price=" + price
                + ", duration=" + duration
                + ", createTime=" + createTime
                + ", lastUpdateTime=" + lastUpdateTime
                + '}';
    }

    public static class GiftCertificateBuilder {

        private Long id;
        private String name;
        private String description;
        private Long price;
        private Integer duration;
        private LocalDateTime createTime;
        private LocalDateTime lastUpdateTime;

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

        public GiftCertificateBuilder createTime(LocalDateTime createTime) {
            this.createTime = createTime;
            return this;
        }

        public GiftCertificateBuilder lastUpdateTime(LocalDateTime lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
            return this;
        }

        public GiftCertificate build() {
            return new GiftCertificate(id, name, description, price, duration, createTime, lastUpdateTime);
        }
    }
}
