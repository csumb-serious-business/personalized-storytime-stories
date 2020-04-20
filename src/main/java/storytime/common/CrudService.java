package storytime.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public abstract class CrudService<T extends HasId, R extends JpaRepository<T, Long>> {
  private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

  protected R repository;

  public CrudService(R repository) {
    this.repository = repository;
  }

  public boolean create(T t) {
    try {
      log.info("create({})", t);
      repository.save(t);
    } catch (Exception e) {
      log.info("error -- {}, {}", e.getMessage(), e.getStackTrace());
      return false;
    }
    return true;
  }

  public Optional<T> read(Long id) {
    log.info("read({})", id);
    return repository.findById(id).stream().findFirst();
  }

  public boolean update(T t) {
    try {
      log.info("update({})", t);
      repository.save(t);
    } catch (Exception e) {
      log.info("error -- {}, {}", e.getMessage(), e.getStackTrace());
      return false;
    }
    return true;
  }

  public boolean delete(T t) {
    try {
      log.info("delete({})", t);
      repository.delete(t);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

}
