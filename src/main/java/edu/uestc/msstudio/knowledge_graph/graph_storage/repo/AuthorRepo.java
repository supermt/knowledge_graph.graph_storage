package edu.uestc.msstudio.knowledge_graph.graph_storage.repo;

import edu.uestc.msstudio.knowledge_graph.graph_storage.model.Author;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepo
        extends PagingAndSortingRepository<Author, Long>
{
    List<Author> findAllByName(String name);
}
