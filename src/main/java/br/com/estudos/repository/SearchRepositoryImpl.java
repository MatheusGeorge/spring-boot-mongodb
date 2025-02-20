package br.com.estudos.repository;

import br.com.estudos.model.Post;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class SearchRepositoryImpl implements SearchRepository{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    MongoConverter converter;

    @Override
    public List<Post> findByText(String text) {
        MongoCollection<Document> collection = mongoTemplate.getCollection("JobPost");
        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(
                        new Document("$search",
                        new Document("text", new Document("query", text)
                                .append("path", Arrays.asList("techs", "desc", "profile")))),
                        new Document("$sort", new Document("exp", 1L)),
                        new Document("$limit", 5L)
                )
        );
        return StreamSupport.stream(result.spliterator(), false)
                .map(document -> converter.read(Post.class, document))
                .toList();
    }
}
