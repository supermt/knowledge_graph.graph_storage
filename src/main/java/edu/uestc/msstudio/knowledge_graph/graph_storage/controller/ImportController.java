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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

    private List<Author> solveAllAuthor(@RequestBody List<Author> authors)
    {
        Set<String> names = new HashSet<>();
        Map<String, Author> name_author_map = new HashMap<>();
        authors.forEach(n -> {
            String name = n.getName();
            name_author_map.put(name, n);
            names.add(name);
        });
        List<Author> existAuthors = authorRepo.findByNameIn(names);
        List<Author> allAuthors = new ArrayList<>();
        List<Author> results = new ArrayList<>();

        for (Author author : existAuthors) {
            name_author_map.put(author.getName(), author);
        }
        name_author_map.entrySet().forEach(
                set -> {
                    allAuthors.add(set.getValue());
                }
        );

        authorRepo.saveAll(allAuthors).forEach(
                single -> {
                    results.add(single);
                }
        );

        return results;
    }

    @PostMapping("/list/paper")
    public List<Paper> createPapers(@RequestBody List<Paper> papers)
    {
        if (papers.size() < 1) {
            return papers;
        }

        List<Paper> results = new ArrayList<>();

        Set<Author> authorSet = new HashSet<>();
        papers.forEach(
                paper -> {
                    authorSet.addAll(paper.getAuthors());
                }
        );
        solveAllAuthor(new ArrayList<>(authorSet));

        for (Paper paper : papers) {
            Set<Author> paperAuthors = paper.getAuthors();
            for (Author author : paperAuthors) {
                author.worksWith(paperAuthors);
            }
            authorRepo.saveAll(paperAuthors);
        }
        paperRepo.saveAll(papers).forEach(
                result -> {
                    results.add(result);
                }
        );

        return results;
    }

    @PostMapping("/paper")
    public Paper createPaper(@RequestBody Paper paper)
    {
        Set<Author> authors = paper.getAuthors();

        solveAllAuthor(new ArrayList<>(authors));

        for (Author author : authors) {
            author.worksWith(authors);
        }
        authorRepo.saveAll(authors);
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

    @DeleteMapping("/paper")
    public List<Paper> deletePaper(@RequestParam String url)
    {
        List<Paper> papers = paperRepo.findAllByUrl(url);
        if (papers != null) {
            for (Paper paper : papers) {
                paperRepo.delete(paper);
            }
            return papers;
        }
        else { return null; }
    }
}
