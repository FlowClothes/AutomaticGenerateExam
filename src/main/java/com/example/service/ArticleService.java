package com.example.service;

import com.example.bean.Article;
import com.example.bean.Content;
import com.example.bean.Predicate;
import com.example.bean.Relation;

import java.util.List;


public interface ArticleService {

    void articleInsert(Article article);

    List<Article> articleList();

    void deleteById(int id);

    String selectAddressById(int id);

    Content selectContent(String content);

    void contentInsert(String nounid,String content);

    Predicate selectPred(String content);

    void PredInsert(String predicateid,String content);

    void relationInsert(Relation relation);

    List<Relation> selectRelationByArticleId(int id);

    String selectNounContentById(String id);

    String selectPredicateContentById(String id);

    List<Content> selectAllContent();

    void updateArticleState(Article article);

}
