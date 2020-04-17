package storytime.parent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParentService {
    Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    private ParentRepository repo;

    public ParentService() {
        super();
    }

    public ParentService(ParentRepository parentRepository) {
        super();
        this.repo = parentRepository;
    }

    public Optional<Parent> getParentById(long id) {
        log.info("getParentById({})", id);
        return repo.findById(id).stream().findFirst();
    }

    public Optional<Parent> getParentByUsername(String username){
        log.info("getParentByUsername({})", username);
        return repo.findByUsername(username).stream().findFirst();

    }

    public boolean persist(Parent parent) {
        try {
            repo.save(parent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
