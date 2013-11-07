package mydomain.model;

import javax.jdo.annotations.*;
import java.util.List;

@PersistenceCapable(detachable="true", cacheable = "false")
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY)
public class Person
{

    @Persistent(column = "id", valueStrategy = IdGeneratorStrategy.IDENTITY)
    @PrimaryKey
    Long id;

    String name;

    @Persistent(defaultFetchGroup = "true")
    @Order(extensions = @Extension(vendorName = "datanucleus", key = "list-ordering", value = "date ASC"))
    List<Alias> aliases;


    public Person(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public Long getId()
    {
        return id;
    }

    public List<Alias> getAliases() {
        return aliases;
    }

    public void setAliases(final List<Alias> aliases) {
        this.aliases = aliases;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Person{");
        if (id != null) {
            sb.append("id=").append(id);
        }
        if (name != null) {
            sb.append(", name='").append(name).append('\'');
        }
        if (aliases != null) {
            sb.append(", aliases=").append(aliases);
        }
        sb.append('}');
        return sb.toString();
    }
}
