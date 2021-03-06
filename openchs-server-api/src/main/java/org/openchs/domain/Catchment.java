package org.openchs.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "catchment")
public class Catchment extends OrganisationAwareEntity {

    @Column
    @NotNull
    private String name;

    @Column(name = "type")
    @NotNull
    private String type;

    @ManyToMany(mappedBy = "catchments", fetch = FetchType.LAZY)
    private Set<AddressLevel> addressLevels = new HashSet<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<AddressLevel> getAddressLevels() {
        if (addressLevels == null) addressLevels = new HashSet<>();
        return addressLevels;
    }

    public void setAddressLevels(Set<AddressLevel> addressLevels) {
        this.addressLevels = addressLevels;
    }

    public AddressLevel findAddressLevel(String addressLevelUUID) {
        return getAddressLevels().stream().filter(x -> x.getUuid().equals(addressLevelUUID)).findAny().orElse(null);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addAddressLevel(AddressLevel addressLevel) {
        getAddressLevels().add(addressLevel);
        addressLevel.addCatchment(this);
    }

    public void remove(AddressLevel addressLevel) {
        getAddressLevels().remove(addressLevel);
        addressLevel.removeCatchment(this);
    }
}
