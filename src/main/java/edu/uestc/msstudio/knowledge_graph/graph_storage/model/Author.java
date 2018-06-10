package edu.uestc.msstudio.knowledge_graph.graph_storage.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Author
{

    @Relationship(type = "TEAMMATE", direction = Relationship.UNDIRECTED)
    public Set<Author> teammates;
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Author() {}

    public Author(String name) {this.name = name;}

    public void worksWith(Author person)
    {
        if (teammates == null) {
            teammates = new HashSet<>();
        }
        if (person.getName() != this.name) {
            teammates.add(person);
        }
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void worksWith(Set<Author> authors)
    {
        for (Author auth : authors) {
            worksWith(auth);
        }
    }

    @JsonBackReference
    public Set<Author> getTeammates()
    {
        return teammates;
    }
}
