package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CheeseDao cheeseDao;

    @Autowired
    MenuDao menuDao;

    @RequestMapping(value="")
    public String index(Model model) {
        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Menus");
        return "menu/index";
    }

    @RequestMapping(value="add", method=RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new Menu());
        model.addAttribute("title", "Add Menu");
        return "menu/add";
    }

    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid Menu menu, Errors errors) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        } else {
            menuDao.save(menu);
            return "redirect:/menu/view/" + menu.getId();
        }
    }

    @RequestMapping(value="view/{id}", method=RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int id) {

        Menu menu = menuDao.findOne(id);

        model.addAttribute("title", menu.getName());
        model.addAttribute("menu", menuDao.findOne(id));
        return "menu/view";

    }

    @RequestMapping(value="add-item/{menuId}", method=RequestMethod.GET)
    public String addItem(Model model, @PathVariable int menuId, Menu menu, Errors errors) {

        AddMenuItemForm newMenuItemForm = new AddMenuItemForm(menu, cheeseDao.findAll());
        Menu menuToEdit = menuDao.findOne(menuId);
        String menuName = menuToEdit.getName();

        model.addAttribute("menu", menuToEdit);
        model.addAttribute("form", newMenuItemForm);
        model.addAttribute("title", "Add item to Menu: " + menuName);
        return "menu/add-item";
    }

    @RequestMapping(value="add-item/{menuId}", method=RequestMethod.POST)
    public String addItem(Model model,
                          @PathVariable int menuId,
                          @ModelAttribute @Valid AddMenuItemForm form,
                          Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("form", form);
            return "menu/add-item";
        } else {

            Cheese addCheese = cheeseDao.findOne(form.getCheeseId());
            Menu theMenu = menuDao.findOne(menuId);

            theMenu.addItem(addCheese);
            menuDao.save(theMenu);
            return "redirect:/menu/view/" + menuId;
        }
    }
}
