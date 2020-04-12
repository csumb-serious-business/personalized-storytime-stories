package storytime.child;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChildService {
    Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    private ChildRepository repo;

    public ChildService() {
        super();
    }

    public ChildService(ChildRepository childRepository) {
        super();
        this.repo = childRepository;
    }

    public Optional<Child> getChildById(long id) {
        log.info("getChildById -- {}", id);
        return repo.findById(id).stream().findFirst();
    }

    public boolean persist(Child child) {
        try {
            repo.save(child);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
