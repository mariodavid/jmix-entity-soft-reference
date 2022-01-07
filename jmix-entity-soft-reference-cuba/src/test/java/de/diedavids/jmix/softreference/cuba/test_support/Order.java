package de.diedavids.jmix.softreference.cuba.test_support;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@JmixEntity
@Table(name = "ORDER_")
@Entity(name = "Order_")
public class Order extends BaseUuidEntity {

    @InstanceName
    @Column(name = "ORDER_DATE")
    private LocalDate orderDate;

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }
}