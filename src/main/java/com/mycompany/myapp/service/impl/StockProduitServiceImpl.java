package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.StockProduit;
import com.mycompany.myapp.repository.StockProduitRepository;
import com.mycompany.myapp.service.StockProduitService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StockProduit}.
 */
@Service
@Transactional
public class StockProduitServiceImpl implements StockProduitService {

    private final Logger log = LoggerFactory.getLogger(StockProduitServiceImpl.class);

    private final StockProduitRepository stockProduitRepository;

    public StockProduitServiceImpl(StockProduitRepository stockProduitRepository) {
        this.stockProduitRepository = stockProduitRepository;
    }

    @Override
    public StockProduit save(StockProduit stockProduit) {
        log.debug("Request to save StockProduit : {}", stockProduit);
        return stockProduitRepository.save(stockProduit);
    }

    @Override
    public Optional<StockProduit> partialUpdate(StockProduit stockProduit) {
        log.debug("Request to partially update StockProduit : {}", stockProduit);

        return stockProduitRepository
            .findById(stockProduit.getId())
            .map(
                existingStockProduit -> {
                    if (stockProduit.getQuantite() != null) {
                        existingStockProduit.setQuantite(stockProduit.getQuantite());
                    }
                    if (stockProduit.getDateCreation() != null) {
                        existingStockProduit.setDateCreation(stockProduit.getDateCreation());
                    }
                    if (stockProduit.getDateModification() != null) {
                        existingStockProduit.setDateModification(stockProduit.getDateModification());
                    }
                    if (stockProduit.getDateSuppression() != null) {
                        existingStockProduit.setDateSuppression(stockProduit.getDateSuppression());
                    }

                    return existingStockProduit;
                }
            )
            .map(stockProduitRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockProduit> findAll() {
        log.debug("Request to get all StockProduits");
        return stockProduitRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StockProduit> findOne(Long id) {
        log.debug("Request to get StockProduit : {}", id);
        return stockProduitRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockProduit : {}", id);
        stockProduitRepository.deleteById(id);
    }
}
