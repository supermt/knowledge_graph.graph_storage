package edu.uestc.msstudio.knowledge_graph.graph_storage.repo;

import edu.uestc.msstudio.knowledge_graph.graph_storage.model.Paper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaperRepo
        extends PagingAndSortingRepository<Paper, Long>
{
    Page<Paper> findByAuthorTextContains(String authorName, Pageable pageable);
}
