package com.r00ta.ffm.manager.models;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.r00ta.ffm.core.models.ManagedEntity;

@NamedQueries({
        @NamedQuery(name = "DINOSAUR.findByStatusesAndShardId",
                query = "from Dinosaur where status IN :statuses and shard_id=:shardId"),
        @NamedQuery(name = "DINOSAUR.findByNameAndCustomerId",
                query = "from Dinosaur where name=:name and customer_id=:customerId"),
        @NamedQuery(name = "DINOSAUR.findByIdAndCustomerId",
                query = "from Dinosaur where id=:id and customer_id=:customerId"),
        @NamedQuery(name = "DINOSAUR.findByCustomerId",
                query = "from Dinosaur where customer_id=:customerId order by submitted_at desc"),
})
@Entity
@Table(name = "DINOSAUR")
public class Dinosaur extends ManagedEntity {

    public static final String CUSTOMER_ID_PARAM = "customerId";

    @Column(name = "endpoint")
    private String endpoint;

    public Dinosaur() {
    }

    public Dinosaur(String name) {
        super(name);
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /*
     * See: https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
     * In the context of JPA equality, our id is our unique business key as we generate it via UUID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dinosaur dinosaur = (Dinosaur) o;
        return getId().equals(dinosaur.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
