package kr.ac.hansung.cse.controller;

import jakarta.validation.Valid;
import kr.ac.hansung.cse.model.Offer;
import kr.ac.hansung.cse.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class OfferController {
    //Controller -> service -> Dao
    @Autowired
    private OfferService offerService;

    @GetMapping("/offers")
    public String showOffers(Model model) {
        List<Offer> offers = offerService.getAllOffers();  // 비즈니스 로직 수행
        model.addAttribute("id_offers", offers);  // 모델에 넣음

        return "offers";
    }

    @GetMapping("/createoffer")
    public String createOffer(Model model) {
        model.addAttribute("offer", new Offer());
        return "createoffer";
    }
    @PostMapping("docreate")
    public String doCreate(Model model, @Valid Offer offer, BindingResult result) {
        // system.out.println(offer);

        if(result.hasErrors()) {
            System.out.println("==Form data does not validated ==");
            List<ObjectError> errors = result.getAllErrors();
            for(ObjectError error : errors) {
                System.out.println(error.getDefaultMessage());
            }
            return "createoffer";
        }

        // Controller -> Service -> DAO 호출
        offerService.insertOffer(offer);
        return "offercreated";
    }

}
