package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.springsecurity.entity.Purchase;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {
    List<Purchase> findAllByPurchaseStatus_Id(UUID purchaseStatus_id);
    List<Purchase> findAllByPaymentStatus_Id(UUID paymentStatus_id);
    List<Purchase> findAllByBranch_Id(UUID branch_id);
    List<Purchase> findAllByDate(Date date);
    List<Purchase> findAllByTotalSum(double totalSum);
    List<Purchase> findAllBySupplierId(UUID dealer_id);

    @Query(value = "select * from purchase inner join branches b on b.business_id = ?1",nativeQuery = true)
    List<Purchase> findAllByBusinessId(UUID businessId);

}
