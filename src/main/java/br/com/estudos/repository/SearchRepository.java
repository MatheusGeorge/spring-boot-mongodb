package br.com.estudos.repository;

import br.com.estudos.model.Post;

import java.util.List;

public interface SearchRepository {

    List<Post> findByText(String text);

}
