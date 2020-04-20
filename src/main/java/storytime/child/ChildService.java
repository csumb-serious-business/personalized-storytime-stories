package storytime.child;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import storytime.common.CrudService;

@Service
public class ChildService extends CrudService<Child, ChildRepository> {
  private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

  @Autowired
  public ChildService(ChildRepository childRepository) {
    super(childRepository);
  }
}
