package BidHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import BidHub.entity.BidItem;

@Repository
public interface BidItemRepository extends JpaRepository<BidItem, Long> {
}
