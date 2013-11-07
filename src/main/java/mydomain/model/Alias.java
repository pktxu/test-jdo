package mydomain.model;

import javax.jdo.annotations.*;
import java.util.Date;

@PersistenceCapable(detachable="true", cacheable = "false")
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY)
public class Alias {

    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @PrimaryKey
    Long id;

    String name;

    Date date;

    public Alias(String name) {
        this.name = name;
        this.date = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Alias alias = (Alias) o;

        if (name != null ? !name.equals(alias.name) : alias.name != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Alias{");
        if (id != null) {
            sb.append("id=").append(id);
        }
        if (name != null) {
            sb.append(", name='").append(name).append('\'');
        }
        sb.append('}');
        return sb.toString();
    }


}
