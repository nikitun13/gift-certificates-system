package com.epam.esm.entity;

import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class OrderDetail implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_certificate_id", nullable = false)
    private GiftCertificate giftCertificate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public OrderDetail() {
    }

    public OrderDetail(Long id, GiftCertificate giftCertificate, Order order, Long price, Integer quantity) {
        this.id = id;
        this.giftCertificate = giftCertificate;
        this.order = order;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderDetailBuilder builder() {
        return new OrderDetailBuilder();
    }

    public long countTotalPrice() {
        return ObjectUtils.allNotNull(price, quantity)
                ? price * quantity
                : 0L;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public GiftCertificate getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
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
        OrderDetail that = (OrderDetail) o;
        return Objects.equals(id, that.id)
                && Objects.equals(giftCertificate, that.giftCertificate)
                && Objects.equals(order, that.order)
                && Objects.equals(price, that.price)
                && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, giftCertificate, order, price, quantity);
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", giftCertificate=" + giftCertificate +
                ", order=" + order +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

    public static class OrderDetailBuilder {
        private Long id;
        private GiftCertificate giftCertificate;
        private Order order;
        private Long price;
        private Integer quantity;

        public OrderDetailBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderDetailBuilder giftCertificate(GiftCertificate giftCertificate) {
            this.giftCertificate = giftCertificate;
            return this;
        }

        public OrderDetailBuilder order(Order order) {
            this.order = order;
            return this;
        }

        public OrderDetailBuilder price(Long price) {
            this.price = price;
            return this;
        }

        public OrderDetailBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderDetail build() {
            return new OrderDetail(id, giftCertificate, order, price, quantity);
        }
    }
}
