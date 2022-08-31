package entities;

import java.io.Serializable;


public abstract class MasterEntity implements Serializable {

    private Long id;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
