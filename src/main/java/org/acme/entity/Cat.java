package org.acme.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import org.acme.entity.dto.CatDTO;
import org.acme.entity.dto.RecordCatDTO;

import java.util.List;

@Entity()
@Table(name = "cat")
@NamedQueries({
        @NamedQuery(name = "JoinPerson.getId", query = "from Cat where id= ?1"),
})
@NamedNativeQueries({
        @NamedNativeQuery(name = "Cat.findByName", query = "select * from cat", resultSetMapping = "Cat")
})
public class Cat extends PanacheEntity {
    private String name;

    @ManyToOne
    private Person person;


    public Cat() {
    }

    public Cat(String name, Person person) {
        this.name = name;
        this.person = person;
    }

    public static Uni<Cat> findJoinPerson(Long id) {
        return find("#JoinPerson.getId", id).firstResult();
    }

    public static Uni<Cat> findWithPerson(Long id) {
        return find("""
                        FROM Cat c
                        join person p
                        on c.masterId=p.id
                        WHERE c.id = :id""",
                Parameters.with("id", id))
                .firstResult();
    }


    public static Uni<Cat> findByName(String name) {
        return find("#Cat.findByName").firstResult();
    }

    public static Uni<Cat> findByPerson(Person person) {
        return find("person", person).firstResult();
    }

    public static Uni<CatDTO> findByPersonResultDTO(Person person) {
        return find("person", person).project(CatDTO.class).firstResult();
    }
    public static Uni<List<RecordCatDTO>> findByPersonResultRecordDTO(Person person) {
        return find("person", person).project(RecordCatDTO.class).list();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Cat{" +
               "name='" + name + '\'' +
               ", person=" + person +
               ", id=" + id +
               '}';
    }
}
