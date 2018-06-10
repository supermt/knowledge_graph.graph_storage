package edu.uestc.msstudio.knowledge_graph.graph_storage.repo;

import edu.uestc.msstudio.knowledge_graph.graph_storage.model.Author;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AuthorRepo
        extends PagingAndSortingRepository<Author, Long>
{
    List<Author> findAllByName(String name);

    List<Author> findByNameIn(Set<String> name);
}
