package com.rental.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rental.movie.models.entity.Invoice;

public interface InvoiceRepository extends MongoRepository<Invoice, String> {

}
