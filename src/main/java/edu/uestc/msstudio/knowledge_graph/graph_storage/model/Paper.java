package edu.uestc.msstudio.knowledge_graph.graph_storage.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

public class Paper
{

    @Relationship(type = "AUTHORS", direction = Relationship.UNDIRECTED)
    public Set<Author> authors = new HashSet<>();
    String authorText;
    String url;
    String title;
    String abstractContent;
    @Id
    @GeneratedValue
    private Long id;

    private Paper()
    {

    }

    public Paper(String url, String title, String abstract_content, Set<Author> authors)
    {
        this.url = url;
        this.title = title;
        this.abstractContent = abstract_content;
        this.authors = authors;
    }

    public String getAuthorText()
    {
        return authorText;
    }

    public void setAuthorText(String authorText)
    {
        this.authorText = authorText;
    }

    public String getAbstractContent()
    {
        return abstractContent;
    }

    public void setAbstractContent(String abstractContent)
    {
        this.abstractContent = abstractContent;
    }

    public Set<Author> getAuthors()
    {
        return authors;
    }

    public void setAuthors(Set<Author> authors)
    {
        this.authors = authors;
        StringBuilder sb = new StringBuilder();
        for (Author auth : authors) {
            sb.append(auth.getName() + "\n");
        }
        this.authorText = sb.toString();
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
}
