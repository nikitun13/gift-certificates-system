package com.epam.esm.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order extends AuditableEntity implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long totalPrice;

    @OneToMany(mappedBy = "order",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<OrderDetail> details = new ArrayList<>();

    public Order() {
    }

    public Order(Long id, User user, LocalDateTime createDate, LocalDateTime lastUpdateDate, Long totalPrice) {
        this.id = id;
        this.user = user;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.totalPrice = totalPrice;
    }

    public Order(Long id, User user, LocalDateTime createDate,
                 LocalDateTime lastUpdateDate, Long totalPrice, List<OrderDetail> details) {
        this.id = id;
        this.user = user;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;

        this.totalPrice = totalPrice;
        this.details = details;
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public void addDetail(OrderDetail detail) {
        details.add(detail);
        detail.setOrder(this);
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderDetail> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetail> details) {
        this.details = details;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id)
                && Objects.equals(user, order.user)
                && Objects.equals(createDate, order.createDate)
                && Objects.equals(lastUpdateDate, order.lastUpdateDate)
                && Objects.equals(totalPrice, order.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, createDate, lastUpdateDate, totalPrice);
    }

    @Override
    public String toString() {
        return "Order{"
                + "id=" + id
                + ", user=" + user
                + ", createDate=" + createDate
                + ", lastUpdateDate=" + lastUpdateDate
                + ", totalPrice=" + totalPrice
                + '}';
    }

    public static class OrderBuilder {

        private Long id;
        private User user;
        private LocalDateTime createDate;
        private LocalDateTime lastUpdateDate;
        private Long totalPrice;
        private List<OrderDetail> details = new ArrayList<>();

        public OrderBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder setUser(User user) {
            this.user = user;
            return this;
        }

        public OrderBuilder setCreateDate(LocalDateTime createDate) {
            this.createDate = createDate;
            return this;
        }

        public OrderBuilder setLastUpdateDate(LocalDateTime lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
            return this;
        }

        public OrderBuilder setTotalPrice(Long totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public OrderBuilder setDetails(List<OrderDetail> details) {
            this.details = details;
            return this;
        }

        public Order build() {
            return new Order(id, user, createDate, lastUpdateDate, totalPrice, details);
        }
    }
}
