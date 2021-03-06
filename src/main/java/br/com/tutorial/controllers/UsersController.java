package br.com.tutorial.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.tutorial.dto.Message;
import br.com.tutorial.entities.Role;
import br.com.tutorial.entities.User;
import br.com.tutorial.repositories.RoleRepository;
import br.com.tutorial.repositories.UserRepository;

@Controller
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository repositoryRole;
	
	@GetMapping("/index")
	public ModelAndView index()
	{
		ModelAndView result = new ModelAndView("user/index");
		
		List<User> userList = repository.findAll();			
		result.addObject("users", userList);
		
		return result;
	}
	
	@GetMapping("/add")
	public ModelAndView add()
	{
		ModelAndView result = new ModelAndView("user/add");
		
		User user = new User();
		user.setRole(new Role());		
		
		result.addObject("user", user);
		result.addObject("roles", repositoryRole.findAll());
		
		return result;
	}

	@GetMapping("/edit/{id}")
	public ModelAndView edit(@PathVariable Long id)
	{
		User user = repository.getOne(id);
		
		ModelAndView result = new ModelAndView("user/edit");
		result.addObject("user", user);
		result.addObject("roles", repositoryRole.findAll());
		
		return result;
	}
	
	@PostMapping("/save")
	public String save(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model)
	{   
		if(user.getId() == null && user.getPasswordEditable().isEmpty()) {
			bindingResult.rejectValue("passwordEditable", "error.user", "Tha password can't not be blank.");
		}
        
        if (bindingResult.hasErrors()) {       
        	model.addAttribute("roles", repositoryRole.findAll());
            return (user.getId() == null) ? "user/add" :"user/edit"; 
        }
        
		if(!user.getPasswordEditable().isEmpty()) {
			PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			user.setPassword(encoder.encode(user.getPasswordEditable()));
		}else {
			User userDatabase = repository.getOne(user.getId());
			user.setPassword(userDatabase.getPassword());
		}
		
        Message message = (new Message()).setSuccess("Role inserted successfully");        
        redirectAttributes.addFlashAttribute("message", message);
        
        user.setActive(true);
		repository.save(user);		
		return "redirect:/users/index";		
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id)
	{
		User user = repository.getOne(id);
		repository.delete(user);
		
		return "redirect:/uses/index";
	}	
	
	@GetMapping({"/searchByName/{name}", "/searchByName"})
	public @ResponseBody List<User> searchByName(@PathVariable Optional<String> name){
		return (name.isPresent())
			? repository.findByNameContaining(name.get())
			: repository.findAll();
	}	
}
