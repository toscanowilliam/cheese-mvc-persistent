package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by LaunchCode and Sarah Hendrickson
 */

@Controller
@RequestMapping("cheese")
public class CheeseController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private CategoryDao categoryDao;

    // Request path: /cheese
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "My Cheeses");

        return "cheese/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Category category, Model model) {
        model.addAttribute("title", "Add Cheese");
        model.addAttribute(new Cheese());
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(@ModelAttribute  @Valid Cheese newCheese,
                                       Errors errors, @RequestParam int categoryId, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            return "cheese/add";
        }

        Category cat = categoryDao.findOne(categoryId);
        newCheese.setCategory(cat);
        cheeseDao.save(newCheese);
        return "redirect:";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Remove Cheese");
        return "cheese/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] cheeseIds) {

        // When removing a cheese from list,
        // find all menus that the cheese is in and
        // remove the cheese in each
        // Them, remove the cheese from the list

        for (int cheeseId : cheeseIds) {
            Cheese cheese = cheeseDao.findOne(cheeseId);
            List<Menu> menus = cheese.getMenus();
            for (Menu menu : menus) {
                menu.removeItem(cheese);
            }
        }
        for (int cheeseId : cheeseIds) {
            cheeseDao.delete(cheeseId);
        }

        return "redirect:/cheese";
    }

    @RequestMapping(value = "edit/{cheeseId}", method=RequestMethod.GET)
    public String displayEditForm(Model model, @PathVariable int cheeseId) {

        Cheese cheeseToEdit = cheeseDao.findOne(cheeseId);

        model.addAttribute("cheese", cheeseToEdit);
        model.addAttribute("cheeseTypes", categoryDao.findAll());

        return "cheese/edit";

    }

    @RequestMapping(value = "edit", method=RequestMethod.POST)
    public String processEditCheeseForm(@RequestParam String name, @RequestParam String description,
                                        @RequestParam int id, @RequestParam int category) {

        Cheese cheeseToEdit = cheeseDao.findOne(id);
        Category newCategory = categoryDao.findOne(category);

        cheeseToEdit.setName(name);
        cheeseToEdit.setDescription(description);
        cheeseToEdit.setCategory(newCategory);

        cheeseDao.save(cheeseToEdit);

        return "redirect:/cheese";

    }

    @RequestMapping(value = "category/{categoryId}", method = RequestMethod.GET)
    public String displayCategoryIndex(Model model, Cheese cheese, Category category, @PathVariable int categoryId) {

        Category cat = categoryDao.findOne(categoryId);
        List<Cheese> cheeses = cat.getCheeses();

        model.addAttribute("cheeses", cheeses);
        model.addAttribute("title", "Cheese By Category: " + cat.getName());

        return "category/index";
    }

}