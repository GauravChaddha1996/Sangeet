package com.gaurav.domain.usecases.interfaces;

import com.gaurav.domain.models.SearchResult;

import io.reactivex.Observable;

public interface SearchUseCase {
    Observable<SearchResult> search(String query);
}
