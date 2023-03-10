package uz.pdp.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.repository.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@Service
public class TradeService {
    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    PaymentStatusRepository paymentStatusRepository;

    @Autowired
    PayMethodRepository payMethodRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TradeProductRepository tradeProductRepository;

    @Autowired
    TradeHistoryRepository tradeHistoryRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    CurrentCourceRepository currentCourceRepository;

    @Autowired
    CurrencyService currencyService;

    /*@Transactional
    public ApiResponse create(TradeDTO tradeDTO) {
        return null;
    }*/

    /*@SneakyThrows
    public ApiResponse create(TradeDTO tradeDTO) {
        Trade trade = new Trade();


        return addTrade(trade, tradeDTO);
    }*/
//
//    public ApiResponse addTrade(Trade trade, TradeDTO tradeDTO) {
//        Currency currency = currencyRepository.findByBusinessIdAndActiveTrue(tradeDTO.getBusinessId());
//        CurrentCource currentCource = currentCourceRepository.getByCurrencyIdAndActive(currency.getId(), true);
//        Optional<Customer> optionalCustomer = customerRepository.findById(tradeDTO.getCustomerId());
//        if (optionalCustomer.isEmpty()) {
//            return new ApiResponse("CUSTOMER NOT FOUND", false);
//        }
//        Customer customer = optionalCustomer.get();
////        trade.setCustomer(optionalCustomer.get());
//
//        /*
//          SOTUVCHI SAQLANDI
//         */
//        Optional<User> optionalUser = userRepository.findById(tradeDTO.getUserId());
//        if (optionalUser.isEmpty()) {
//            return new ApiResponse("TRADER NOT FOUND", false);
//        }
//        trade.setTrader(optionalUser.get());
//
//        /**
//         * BRANCH SAQLANDI
//         */
//        Optional<Branch> optionalBranch = branchRepository.findById(tradeDTO.getBranchId());
//        if (optionalBranch.isEmpty()) {
//            return new ApiResponse("BRANCH NOT FOUND", false);
//        }
//        Branch branch = optionalBranch.get();
//        trade.setBranch(branch);
//
//        /**
//         * SOTILGAN PRODUCT SAQLANDI YANI TRADERPRODUCT
//         */
//        List<ProductTradeDto> productTraderDto = tradeDTO.getProductTraderDto();
//        List<TradeProduct> tradeProducts = new ArrayList<>();
//
//            for (ProductTradeDto productTradeDto : productTraderDto) {
//
//                Double tradedQuantity = productTradeDto.getTradedQuantity();
//                Optional<Product> optionalProduct = productRepository.findById(productTradeDto.getProductTradeId());
//                Product product = optionalProduct.get();
//                TradeProduct tradeProduct = new TradeProduct();
//
//                if (tradedQuantity > product.getQuantity()) {
//                    return new ApiResponse("OMBORDA YETARLICHA MAXSULOT YO'Q", false);
//                }
//                product.setQuantity(product.getQuantity() - tradedQuantity);
//                if (!currency.getName().equalsIgnoreCase("SO'M")){
//                    double salePrice = product.getSalePrice();
//                    salePrice = salePrice * currentCource.getCurrentCourse();
//                    tradeProduct.setSalePrice(salePrice);
//                }else {
//                    tradeProduct.setSalePrice(product.getSalePrice());
//                }
//                productRepository.save(product);
//
//                tradeProduct.setTradedQuantity(productTradeDto.getTradedQuantity());
//                tradeProduct.setProduct(product);
//
//                tradeProduct = tradeProductRepository.save(tradeProduct);
//                tradeProducts.add(tradeProduct);
//
//            }
//
//        /**
//         * SOTILGAN PRODUCT SAQLANDI YANI TRADERPRODUCT
//         */
//        trade.setTradeProductList(tradeProducts);
//
//
//        /**
//         * UMUMIY SUMMA SAQLANDI
//         */
//        if (!currency.getName().equalsIgnoreCase("SO'M")){
//            double totalSum = tradeDTO.getTotalSum();
//            totalSum = totalSum * currentCource.getCurrentCourse();
//            trade.setTotalSum(totalSum);
//        }else {
//            trade.setTotalSum(tradeDTO.getTotalSum());
//        }
////        double sum = 0d;
////        for (ProductTradeDto productTradeDto : productTraderDto) {
////            double salePrice = productRepository.findById(productTradeDto.getProductTradeId()).get().getSalePrice();
////            Double tradedQuantity = productTradeDto.getTradedQuantity();
////            double parseDouble = Double.parseDouble(String.valueOf(tradedQuantity));
////            sum = sum + (salePrice * parseDouble);
////        }
////        trade.setTotalSum(sum);
//
//        /**
//         * AMOUNT LOAN PRICE SAQLANDI
//         */
//        if (tradeDTO.getAmountPaid() == 0){
//            if (!currency.getName().equalsIgnoreCase("SO'M")){
//                double totalSum = tradeDTO.getTotalSum();
//                totalSum = totalSum * currentCource.getCurrentCourse();
//                customer.setDebt(customer.getDebt() + totalSum);
//            }else {
//                customer.setDebt(customer.getDebt() + tradeDTO.getTotalSum());
//            }
//            trade.setAvans(0d);
//            PaymentStatus status = paymentStatusRepository.findByStatus("To'lanmagan");
//            trade.setPaymentStatus(status);
//        }else if (tradeDTO.getAmountPaid() > 0 && tradeDTO.getAmountPaid() < tradeDTO.getTotalSum()){
//            if (!currency.getName().equalsIgnoreCase("SO'M")){
//                Double amountPaid = tradeDTO.getAmountPaid();
//                amountPaid = amountPaid * currentCource.getCurrentCourse();
//                double totalSum = tradeDTO.getTotalSum();
//                totalSum = totalSum * currentCource.getCurrentCourse();
//                trade.setAvans(amountPaid);
//                customer.setDebt(customer.getDebt() + totalSum - amountPaid);
//            }else {
//                trade.setAvans(tradeDTO.getAmountPaid());
//                customer.setDebt(customer.getDebt() + tradeDTO.getTotalSum() - tradeDTO.getAmountPaid());
//            }
//            PaymentStatus status = paymentStatusRepository.findByStatus("Qisman to'langan");
//            trade.setPaymentStatus(status);
//        }else if (tradeDTO.getAmountPaid() == tradeDTO.getTotalSum()){
//            if (!currency. getName().equalsIgnoreCase("SO'M")){
//                Double amountPaid = tradeDTO.getAmountPaid();
//                amountPaid = amountPaid * currentCource.getCurrentCourse();
//                trade.setAvans(amountPaid);
//            }else {
//                trade.setAvans(tradeDTO.getAmountPaid());
//            }
//            PaymentStatus status = paymentStatusRepository.findByStatus("To'langan");
//            trade.setPaymentStatus(status);
//        }else if (tradeDTO.getAmountPaid() > tradeDTO.getTotalSum()){
//            return new ApiResponse("A LOT OF MONEY PAID", false);
//        }
//
////        if (tradeDTO.getAmountPaid() == 0d) {
////
////            trade.setAmountPaid(0.0);
////            trade.setLoan(sum - tradeDTO.getAmountPaid());
////
////            PaymentStatus statusName = paymentStatusRepository.findByStatus("To'langan");
////
////            trade.setPaymentStatus(statusName);
////        } else if (sum < tradeDTO.getAmountPaid()) {
////            return new ApiResponse("A LOT OF MONEY PAID", false);
////        } else {
////            if (trade.getAmountPaid() != null) {
////                if ((trade.getTotalSum() < (trade.getAmountPaid() + tradeDTO.getAmountPaid()))) {
////                    return new ApiResponse("A LOT OF MONEY PAID", false);
////                } else {
////                    trade.setAmountPaid(trade.getAmountPaid() + tradeDTO.getAmountPaid());
////                    trade.setLoan(trade.getLoan() - tradeDTO.getAmountPaid());
////                }
////            } else if (trade.getAmountPaid() == null || trade.getAmountPaid() == 0.0) {
////                trade.setAmountPaid(tradeDTO.getAmountPaid());
////                trade.setLoan(sum - tradeDTO.getAmountPaid());
////            } else {
////                trade.setAmountPaid(trade.getAmountPaid() + tradeDTO.getAmountPaid());
////                trade.setLoan(trade.getLoan() - tradeDTO.getAmountPaid());
////            }
////
////            if ((trade.getTotalSum() - tradeDTO.getAmountPaid() == 0) || trade.getLoan() == 0.0) {
////                PaymentStatus statusName = paymentStatusRepository.findByStatus("To'lanmagan");
////                trade.setPaymentStatus(statusName);
////            } else {
////                PaymentStatus statusName = paymentStatusRepository.findByStatus("Qisman to'langan");
////                trade.setPaymentStatus(statusName);
////
////                customer.setDebt();
////            }
////        }
//
////        /**
////         * CURRENCY SAQALANDI
////         */
////        Optional<Currency> optionalCurrency = currencyRepository.findById(tradeDTO.getCurrencyId());
////        if (optionalCurrency.isEmpty()) {
////            return new ApiResponse("NOT FOUND CURRENCY", false);
////        }
////        trade.setCurrency(optionalCurrency.get());
//
//
//
//        /**
//         * PAYMAENTMETHOD SAQLANDI
//         */
//        Optional<PaymentMethod> optionalPaymentMethod = payMethodRepository.findById(tradeDTO.getPayMethodId());
//        if (optionalPaymentMethod.isEmpty()) {
//            return new ApiResponse("PAYMAENTMETHOD NOT FOUND", false);
//        }
//        trade.setPayMethod(optionalPaymentMethod.get());
//
//
//        /**
//         * ADDRESS SAQLANDI
//         */
//        Optional<Address> optionalAddress = addressRepository.findById(tradeDTO.getAddressId());
//        if (optionalAddress.isEmpty()) {
//            return new ApiResponse("ADDRESS NOT FOUND", false);
//        }
//        trade.setAddress(optionalAddress.get());
//        /**
//         * PAYDATE SAQLANDI
//         */
//        Date payDate = tradeDTO.getPayDate();
//        trade.setPayDate(payDate);
//
////        if (customer.getDebt() != null) {
////            customer.setDebt(customer.getDebt() + trade.getLoan());
////        }else {
////            customer.setDebt(trade.getLoan());
////        }
//        customer = customerRepository.save(customer);
//
//        trade.setCustomer(customer);
//        tradeRepository.save(trade);
//
//        /**
//         * TRADE HISTORY QOSHILYAPTI
//         */
//
//        TradeHistory tradeHistory = new TradeHistory();
//        tradeHistory.setPaidAmount(tradeDTO.getAmountPaid());
//        Date date = new Date(System.currentTimeMillis());
//        tradeHistory.setPaidDate(date);
//        tradeHistory.setTrade(trade);
//        PaymentMethod payMethod = trade.getPayMethod();
//        tradeHistory.setPaymentMethod(payMethod.getType());
//
//        tradeHistoryRepository.save(tradeHistory);
//
//
//        return new ApiResponse("SAVED!", true);
//    }
//
//    public ApiResponse edit(UUID id, TradeDTO tradeDTO) {
//        Optional<Trade> optionalTrade = tradeRepository.findById(id);
//        if (optionalTrade.isEmpty()) {
//            return new ApiResponse("NOT FOUND TRADE", false);
//        }
//        Trade trade = optionalTrade.get();
//        ApiResponse apiResponse = addTrade(trade, tradeDTO);
//
//        if (!apiResponse.isSuccess()) {
//            return new ApiResponse("ERROR", false);
//        }
//        return new ApiResponse("UPDATED", true);
//    }

    public ApiResponse getOne(UUID id) {
        Optional<Trade> optionalTrade = tradeRepository.findById(id);
        if (optionalTrade.isEmpty()){
            return new ApiResponse("NOT FOUND", false);
        }
        Trade trade = generateTradeByActiveCourse(optionalTrade.get());
        return  new ApiResponse(true, trade);
    }

    public ApiResponse deleteTrade(UUID id) {
        Optional<Trade> byId = tradeRepository.findById(id);
        if (byId.isEmpty()) return new ApiResponse("NOT FOUND",false);
        tradeRepository.deleteById(id);
        return new ApiResponse("DELATED", true);
    }

    public ApiResponse deleteByTraderId(UUID trader_id) {
        tradeRepository.deleteByTrader_Id(trader_id);
        return new ApiResponse("DELETED", true);
    }

    public ApiResponse deleteAllByTraderId(UUID trader_id) {

        tradeRepository.deleteAllByTrader_Id(trader_id);
        return new ApiResponse("DELETED", true);
    }

    public ApiResponse getAllByTraderId(UUID trader_id) {
        List<Trade> allByTrader_id = tradeRepository.findAllByTrader_Id(trader_id);
        if (allByTrader_id.isEmpty()) return new ApiResponse("NOT FOUND", false);
        List<Trade> tradeList = new ArrayList<>();
        for (Trade trade : allByTrader_id) {
            Trade generateTradeByActiveCourse = generateTradeByActiveCourse(trade);
            tradeList.add(generateTradeByActiveCourse);
        }
        return new ApiResponse("FOUND", true, tradeList);
    }

    public ApiResponse getAllByBranchId(UUID branch_id) {
        List<Trade> allByBranch_id = tradeRepository.findAllByBranch_Id(branch_id);
        if (allByBranch_id.isEmpty()) return new ApiResponse("NOT FOUND", false);
        List<Trade> tradeList = new ArrayList<>();
        for (Trade trade : allByBranch_id) {
            Trade generateTradeByActiveCourse = generateTradeByActiveCourse(trade);
            tradeList.add(generateTradeByActiveCourse);
        }
        return new ApiResponse("FOUND", true, tradeList);
    }

    public ApiResponse getByCustomerId(UUID customer_id) {
        List<Trade> allByCustomer_id = tradeRepository.findAllByCustomer_Id(customer_id);
        if (allByCustomer_id.isEmpty()) return new ApiResponse("NOT FOUND", false);
        List<Trade> tradeList = new ArrayList<>();
        for (Trade trade : allByCustomer_id) {
            Trade generateTradeByActiveCourse = generateTradeByActiveCourse(trade);
            tradeList.add(generateTradeByActiveCourse);
        }
        return new ApiResponse("FOUND", true, tradeList);
    }

    public ApiResponse getByPayDate(Timestamp payDate) throws ParseException {
        List<Trade> allByPayDate = tradeRepository.findTradeByOneDate(payDate);
        if (allByPayDate.isEmpty()) return new ApiResponse("NOT FOUND", false);
        List<Trade> tradeList = new ArrayList<>();
        for (Trade trade : allByPayDate) {
            Trade trade1 = generateTradeByActiveCourse(trade);
            tradeList.add(trade1);
        }
        return new ApiResponse("FOUND", true, tradeList);
    }

    public ApiResponse getByPayStatus(UUID paymentStatus_id) {
        List<Trade> allByPaymentStatus_id = tradeRepository.findAllByPaymentStatus_Id(paymentStatus_id);
        if (allByPaymentStatus_id.isEmpty()) return new ApiResponse("NOT FOUND", false);
        List<Trade> tradeList = new ArrayList<>();
        for (Trade trade : allByPaymentStatus_id) {
            Trade generateTradeByActiveCourse = generateTradeByActiveCourse(trade);
            tradeList.add(generateTradeByActiveCourse);
        }
        return new ApiResponse("FOUND", true, tradeList);
    }

    public ApiResponse getByPayMethod(UUID payMethod_id) {
        List<Trade> allByPaymentMethod_id = tradeRepository.findAllByPayMethod_Id(payMethod_id);
        if (allByPaymentMethod_id.isEmpty()) return new ApiResponse("NOT FOUND", false);
        List<Trade> tradeList = new ArrayList<>();
        for (Trade trade : allByPaymentMethod_id) {
            Trade trade1 = generateTradeByActiveCourse(trade);
            tradeList.add(trade1);
        }
        return new ApiResponse("FOUND", true, tradeList);
    }

    public ApiResponse getByAddress(UUID address_id) {
        List<Trade> allByAddress_id = tradeRepository.findAllByAddress_Id(address_id);
        if (allByAddress_id.isEmpty()) return new ApiResponse("NOT FOUND", false);
        List<Trade> tradeList = new ArrayList<>();
        for (Trade trade : allByAddress_id) {
            Trade trade1 = generateTradeByActiveCourse(trade);
            tradeList.add(trade1);
        }
        return new ApiResponse("FOUND", true, tradeList);
    }

    public ApiResponse createPdf(UUID id, HttpServletResponse response) throws IOException {

        Optional<Trade> tradeOptional = tradeRepository.findById(id);
        PDFService pdfService = new PDFService();

        pdfService.createPdf(tradeOptional.get(), response);

        return new ApiResponse("CREATED", true);
    }

    public ApiResponse getAllByBusinessId(UUID businessId) {
        List<Trade> allByBusinessId = tradeRepository.findAllByBusinessId(businessId);
        if (allByBusinessId.isEmpty()) return new ApiResponse("NOT FOUND",false);
        List<Trade> tradeList = new ArrayList<>();
        for (Trade trade : allByBusinessId) {
            Trade trade1 = generateTradeByActiveCourse(trade);
            tradeList.add(trade1);
        }
        return new ApiResponse("FOUND",true,tradeList);
    }

    private Trade generateTradeByActiveCourse(Trade trade){
        UUID busnessId = trade.getBranch().getBusiness().getId();
        double avans = currencyService.getValueByActiveCourse(trade.getAvans(), busnessId);
        trade.setAvans(avans);
        double totalSum = currencyService.getValueByActiveCourse(trade.getTotalSum(), busnessId);
        trade.setTotalSum(totalSum);
//        sac

        for (TradeProduct tradeProduct : trade.getTradeProductList()) {
            double salePrice = currencyService.getValueByActiveCourse(tradeProduct.getSalePrice(), busnessId);
            tradeProduct.setSalePrice(salePrice);
            Product product = tradeProduct.getProduct();
            product.setSalePrice(salePrice);
            double buyPrice = currencyService.getValueByActiveCourse(product.getBuyPrice(), busnessId);
            product.setBuyPrice(buyPrice);
        }
        return trade;
    }
}
