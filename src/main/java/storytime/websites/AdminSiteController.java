package storytime.websites;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import storytime.child.Child;
import storytime.story.Story;
import storytime.story.StoryService;

@Controller
@EnableAutoConfiguration
public class AdminSiteController {
	Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	StoryService storyService;

	@GetMapping("/admin/stories")
	public String stories(Model model) {
		log.info("getting /admin/stories");
		return "admin/story-all-show";
	}

	@GetMapping("/admin/story/edit/{id}")
	public String story__edit(ModelMap model, @PathVariable("id") long id) {
		log.info("getting /admin/story/edit/{}", id);
		model.addAttribute("id", id);
		return "admin/story-one-edit";
	}

	@GetMapping("/admin/story/new")
	public String story__new(ModelMap model) {
		log.info("GET admin/story/new");
		model.addAttribute("story", new Story());
		return "admin/story-one-new";
	}

	@PostMapping("/admin/story/new")
	public String story__new(@Valid @ModelAttribute("story") Story story, BindingResult result) {
		if (result.hasErrors()) {
			return "admin/story-one-new";
		}
		log.info("POST /admin/story/new -- story");
		String url = "/admin/story/" + story.getId();
		log.info("redirecting to: {}", url);
		return "redirect:" + url;
	}

	@GetMapping("/admin/story/{id}")
	public String admin__id__new(Model model, @PathVariable("id") long id) {
		log.info("GET /admin/story/{}", id);
		model.addAttribute("story", new Story());
		return "admin/story-show";
	}

	@PostMapping("/admin/story/{id}")
	public String admin__id__new(Model model, @PathVariable("id") long id, @Valid @ModelAttribute("story") Story story,
			BindingResult result) {
		storyService.getStoryById(id).ifPresent(model::addAttribute);
		if (result.hasErrors()) {
			log.info("result: {}", result);
			return "admin/story-all-show";
		}
		log.info("POST /admin/story/{} -- story {}", id, story);
		storyService.persist(story);
		return stories(model);
	}

}
