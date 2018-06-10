package edu.uestc.msstudio.knowledge_graph.graph_storage.controller;

import edu.uestc.msstudio.knowledge_graph.graph_storage.model.Author;
import edu.uestc.msstudio.knowledge_graph.graph_storage.model.Paper;
import edu.uestc.msstudio.knowledge_graph.graph_storage.repo.AuthorRepo;
import edu.uestc.msstudio.knowledge_graph.graph_storage.repo.PaperRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/import")
public class ImportController
{
    @Autowired
    private AuthorRepo authorRepo;

    @Autowired
    private PaperRepo paperRepo;

    @GetMapping("/author")
    public Author findAuthor(@RequestParam String name)
    {
        List<Author> authors = authorRepo.findAllByName(name);
        if (authors.size() == 1) { return authors.get(0);}
        else { return null; }
    }

    @DeleteMapping("/author")
    public List<Author> deleteAuthor(@RequestParam String name)
    {
        List<Author> authors = authorRepo.findAllByName(name);
        if (authors != null) {
            for (Author auth : authors) {
                authorRepo.delete(auth);
            }
            return authors;
        }
        else { return null; }
    }

    @PostMapping("/author")
    public Author createAuthor(@RequestBody Author author)
    {
        List<Author> authors = authorRepo.findAllByName(author.getName());

        if (authors.size() > 1) {
            for (Author auth : authors) {
                authorRepo.delete(auth);
            }
        }
        else if (authors.size() == 1) {
            author.setId(authors.get(0).getId());
        }

        author = authorRepo.save(author);

        return author;
    }

    @PostMapping("/list/author")
    public List<Author> createAuthors(@RequestBody List<Author> authors)
    {
        List<Author> results = new ArrayList<>();
        for (Author author : authors) {
            results.add(createAuthor(author));
        }
        return results;
    }

    @PostMapping("/list/papers")
    public List<Paper> createPapers(@RequestBody List<Paper> papers)
    {
        return null;
    }

    @PostMapping("/paper")
    public Paper createPaper(@RequestBody Paper paper)
    {
        Set<Author> authors = paper.getAuthors();

        for (Author author : authors) {
            author = createAuthor(author);
        }
        for (Author author : authors) {
            author.worksWith(authors);
            authorRepo.save(author);
        }
        paperRepo.save(paper);

        return paper;
    }

    @GetMapping("/paper")
    public Page<Paper> getPapers(@RequestParam String name,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "15") Integer size)

    {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        return paperRepo.findByAuthorTextContains(name, pageable);
    }
}
